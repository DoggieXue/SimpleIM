package org.cloudxue.imServer.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.concurrent.FutureTaskScheduler;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imServer.session.ServerSession;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName HeartBeatServerHandler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/10 上午11:28
 * @Version 1.0
 **/
@Slf4j
public class HeartBeatServerHandler extends IdleStateHandler {

    private static final int READ_IDLE_GAP = 150;

    public HeartBeatServerHandler () {
        super(READ_IDLE_GAP, 0, 0, TimeUnit.SECONDS);
    }

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
        if (headType.equals(ProtoMsg.HeadType.KEEPALIVE_REQUEST)) {
            //异步处理，将心跳包直接回复给客户端
            FutureTaskScheduler.add(() -> {
                if (ctx.channel().isActive()) {
                    ctx.writeAndFlush(msg);
                }
            });
        }
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        log.info(READ_IDLE_GAP + "秒内未读到数据，关闭连接");
        ServerSession.closeSession(ctx);
    }
}
