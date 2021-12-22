package org.cloudxue.imServer.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.concurrent.CallBackTask;
import org.cloudxue.concurrent.CallbackTaskScheduler;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imServer.processer.LoginProcessor;
import org.cloudxue.imServer.session.ServerSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName LoginRequestHandler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/22 下午2:31
 * @Version 1.0
 **/
@Slf4j
@Service("LoginRequestHandler")
@ChannelHandler.Sharable
public class LoginRequestHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    LoginProcessor loginProcessor;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("收到一个新的连接，但是没有登录 {}", ctx.channel().id());
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        //获取请求类型
        ProtoMsg.HeadType headType = pkg.getType();
        if (!headType.equals(loginProcessor.type())) {
            super.channelRead(ctx, msg);
            return;
        }

        ServerSession session = new ServerSession(ctx.channel());

        //异步任务
        CallbackTaskScheduler.add(new CallBackTask<Boolean>() {

            @Override
            public Boolean extcute() throws Exception {
                boolean r = loginProcessor.action(session, pkg);
                return r;
            }

            @Override
            public void onBack(Boolean r) {
                if (r) {

                    ctx.pipeline().remove("login");
                    log.info("登录成功： " + session.getUser());
                } else {
                    ServerSession.closeSession(ctx);
                    log.info("登录失败： " + session.getUser());
                }
            }

            @Override
            public void onException(Throwable t) {
                ServerSession.closeSession(ctx);
                log.info("登录失败： " + session.getUser());
            }
        });

    }
}
