package org.cloudxue.imClient.sender;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.bean.User;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imClient.session.ClientSession;


/**
 * @ClassName BaseSender
 * @Description 消息发送器
 * @Author xuexiao
 * @Date 2021/12/23 下午9:59
 * @Version 1.0
 **/
@Slf4j
@Data
public abstract class BaseSender {
    private User user;
    private ClientSession session;

    public boolean isConnected () {
        if (null == session) {
            log.info("session is null");
            return false;
        }
        return session.isConnected();
    }

    public boolean isLogin() {
        if (null == session) {
            log.info("session is null");
            return false;
        }
        return session.isLogin();
    }

    public void sendMsg(ProtoMsg.Message message) {
        if (null == getSession() || !isConnected()) {
            log.info("未连接成功!");
            return;
        }
        Channel channel = getSession().getChannel();
        ChannelFuture f = channel.writeAndFlush(message);
        f.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                //消息发送完成后的回调
                if (future.isSuccess()) {
                    sendSuccess(message);
                } else {
                    sendFailed(message);
                }
            }
        });
    }

    protected void sendSuccess(ProtoMsg.Message message) {
        log.info("发送成功");

    }

    protected void sendFailed(ProtoMsg.Message message) {
        log.info("发送失败");
    }
}
