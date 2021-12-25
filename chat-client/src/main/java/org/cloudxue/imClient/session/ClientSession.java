package org.cloudxue.imClient.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.bean.User;
import org.cloudxue.im.common.bean.msg.ProtoMsg;

import java.util.UUID;


/**
 * @ClassName ClientSession
 * @Description 客户端Session会话
 * @Author xuexiao
 * @Date 2021/12/23 下午9:56
 * @Version 1.0
 **/
@Data
@Slf4j
public class ClientSession {

    public static final AttributeKey<ClientSession> SESSION_LEY = AttributeKey.valueOf("SESSION_KEY");
    /**
     * 用户实现客户端会话管理的核心
     */
    private Channel channel;
    private User user;

    /**
     * 保存登录后的服务端sessionId
     */
    private String sessionId;

    private boolean isConnected = false;
    private boolean isLogin = false;

    public ClientSession(Channel channel) {
        //正向绑定
        this.channel = channel;
        this.sessionId = UUID.randomUUID().toString();
        //反向绑定
        channel.attr(SESSION_LEY).set(this);
    }

    /**
     * 登录成功之后，设置sessionId
     * @param ctx
     * @param pkg
     */
    public static void loginSuccess (ChannelHandlerContext ctx, ProtoMsg.Message pkg) {
        Channel channel = ctx.channel();
        ClientSession session = getSession(ctx);
        session.setSessionId(pkg.getSessionId());
        session.setLogin(true);
        log.info("登录成功");
    }

    public static ClientSession getSession(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_LEY).get();
        return session;
    }

    public String getRemoteAddress () {
        return channel.remoteAddress().toString();
    }

    public ChannelFuture writeAndFlush(Object pkg) {
        ChannelFuture future = channel.writeAndFlush(pkg);
        return future;
    }

    public void writeAndClose (Object pkg) {
        ChannelFuture future = channel.writeAndFlush(pkg);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 关闭通道
     */
    public void close () {
        isConnected = false;

        ChannelFuture future = channel.close();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    log.info("连接顺利断开");
                }
            }
        });
    }
}
