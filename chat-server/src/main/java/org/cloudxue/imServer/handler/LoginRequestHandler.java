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
 * @Description 服务端登录处理器
 *              1、登录消息校验：非空校验、消息类型校验
 *              2、创建Session
 *              3、提交登录业务异步处理任务
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
    @Autowired
    ChatRedirectHandler chatRedirectHandler;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("收到一个新的连接，但是没有登录 {}", ctx.channel().id());
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 1、登录消息校验
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

        //2、创建客户端Session
        ServerSession session = new ServerSession(ctx.channel());

        //3、使用独立的、异步的业务线程 来处理登录时的校验逻辑
        CallbackTaskScheduler.add(new CallBackTask<Boolean>() {

            @Override
            public Boolean execute() throws Exception {
                boolean r = loginProcessor.action(session, pkg);
                return r;
            }
            //异步任务执行成功后返回
            @Override
            public void onBack(Boolean r) {
                if (r) {
                    ctx.pipeline().addAfter("login", "chat", chatRedirectHandler);
                    ctx.pipeline().addAfter("chat", "heartBeat", new HeartBeatServerHandler());
                    ctx.pipeline().remove("login");
                    log.info("登录成功： " + session.getUser());
                } else {
                    ServerSession.closeSession(ctx);
                    log.info("登录失败： " + session.getUser());
                }
            }
            //异步任务执行异常
            @Override
            public void onException(Throwable t) {
                t.printStackTrace();
                ServerSession.closeSession(ctx);
                log.info("登录异常： " + session.getUser());
            }
        });

    }
}
