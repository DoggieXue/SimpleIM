package org.cloudxue.imClient.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.bean.User;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imClient.protoConverter.HeartBeatMsgConverter;
import org.cloudxue.imClient.session.ClientSession;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName HeartBeatClientHandler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/9 下午10:19
 * @Version 1.0
 **/
@Slf4j
@ChannelHandler.Sharable
@Service("heartBeatClientHandler")
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {
    //心跳间隔时间，单位为s
    private static final int HEARTBEAT_INTERVAL = 50;

    /**
     * 在Handler被加入到PipeLine时，开始发送心跳
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ClientSession session = ClientSession.getSession(ctx);
        User user = session.getUser();
        HeartBeatMsgConverter builder = new HeartBeatMsgConverter(ProtoMsg.HeadType.KEEPALIVE_REQUEST, session, user);

        ProtoMsg.Message heartBeatMsg = builder.build();
        heartBeat(ctx, heartBeatMsg);
    }

    /**
     * 使用的定时器发送心跳报文
     * @param heartBeatMsg
     */
    public void heartBeat(ChannelHandlerContext ctx, ProtoMsg.Message heartBeatMsg) {
        ctx.executor().schedule(new Runnable() {
            @Override
            public void run() {
                log.info("发送 HEART_BEAT 消息 to server" );
                heartBeat(ctx, heartBeatMsg);
            }
        }, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * 接收服务器的心跳回写
     * @param ctx
     * @param msg
     * @throws Exception
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType headType = pkg.getType();
        if (headType.equals(ProtoMsg.HeadType.KEEPALIVE_REQUEST)) {
            log.info("收到回写的HEART_BEAT from server");
            return;
        } else {
            super.channelRead(ctx, msg);
        }

    }
}
