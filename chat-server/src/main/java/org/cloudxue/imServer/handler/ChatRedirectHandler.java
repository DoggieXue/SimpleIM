package org.cloudxue.imServer.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.concurrent.FutureTaskScheduler;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imServer.processer.ChatRedirectProcessor;
import org.cloudxue.imServer.session.ServerSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ChatRedirectHandler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/9 下午8:46
 * @Version 1.0
 **/
@Slf4j
@Service("chatRedirectHandler")
@ChannelHandler.Sharable
public class ChatRedirectHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ChatRedirectProcessor chatRedirectProcessor;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }
        //判断消息类型
        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType headType = pkg.getType();
        if (!headType.equals(chatRedirectProcessor.type())) {
            super.channelRead(ctx, msg);
            return;
        }
        //反向导航
        ServerSession session = ctx.channel().attr(ServerSession.SESSION_KEY).get();

        //判读是否登录
        if (null == session || !session.isLogin()) {
            log.error("用户尚未登录，不能发送消息");
            return;
        }
        //异步处理消息IM消息转发的逻辑
        FutureTaskScheduler.add(() -> {
            chatRedirectProcessor.action(session, pkg);
        });
    }
}
