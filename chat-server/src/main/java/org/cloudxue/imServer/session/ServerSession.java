package org.cloudxue.imServer.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.bean.User;

import java.util.UUID;

/**
 * @ClassName ServerSession
 * @Description 自定义Session
 * @Author xuexiao
 * @Date 2021/12/22 上午9:23
 * @Version 1.0
 **/
@Data
@Slf4j
public class ServerSession {

    public static final AttributeKey<String> KEY_USER_ID = AttributeKey.valueOf("key_user_id");

    public static final AttributeKey<ServerSession> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");

    /**
     * 用户实现服务端会话管理的核心
     */
    private Channel channel;
    /**
     * 用户
     */
    private User user;
    /**
     * session的唯一标识
     */
    private String sessionId;
    /**
     * 登录状态
     */
    private boolean isLogin = false;

    public ServerSession (Channel channel) {
        this.channel = channel;
        this.sessionId = buildNewSessionId();
    }

    private static String buildNewSessionId () {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }

    /**
     * 反向导航
     * @param ctx
     * @return
     */
    public static ServerSession getSession (ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        return channel.attr(ServerSession.SESSION_KEY).get();
    }

    /**
     * 关闭连接
     */
    public static void closeSession (ChannelHandlerContext ctx) {
        ServerSession session = ctx.channel().attr(ServerSession.SESSION_KEY).get();

        if (null != session && session.isValid()) {
            session.close();
            SessionMap.inst().removeSession(session.getSessionId());
        }
    }

    /**
     * 反向绑定：最终ServerSession和Channel通道实现双向绑定
     * 顺便加入到会话集合中
     * @return
     */
    public ServerSession reverseBind() {
        log.info("ServerSession 绑定会话 " + channel.remoteAddress());
        channel.attr(ServerSession.SESSION_KEY).set(this);
        SessionMap.inst().addSession(this);
        isLogin = true;
        return this;
    }

    public ServerSession unBind () {
        isLogin = false;
        SessionMap.inst().removeSession(getSessionId());
        this.close();
        return this;
    }

    public String getSessionId () {
        return sessionId;
    }

    public boolean isValid() {
        return getUser() != null ? true : false;
    }

    /**
     *  写ProtoBuf数据帧
     * @param pkg
     */
    public synchronized void writeAndFlush(Object pkg) {
        //当系统水位过高时，系统应不继续发送消息，防止发送队列积压
        if (channel.isWritable()) { //低水位
            channel.writeAndFlush(pkg);
        } else {    //高水位
            log.debug("通道很忙， 消息被暂存了");
            //TODO
            //写入消息暂存的分布式存储，如mongo

            //等channel空闲之后，再写出去
        }
    }

    /**
     * 关闭连接
     */
    public synchronized void close () {
        ChannelFuture future = channel.close();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (!channelFuture.isSuccess()) {
                    log.error("CHANNEL_CLOSED error");
                }
            }
        });
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        user.setSessionId(sessionId);
    }
}