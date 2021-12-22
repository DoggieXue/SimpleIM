package org.cloudxue.imServer.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.exception.InvalidFrameException;
import org.cloudxue.imServer.session.ServerSession;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @ClassName ServerExceptionHandler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/22 下午3:35
 * @Version 1.0
 **/
@Slf4j
@ChannelHandler.Sharable
@Service("serviceExceptionHandler")
public class ServerExceptionHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof InvalidFrameException) {
            cause.printStackTrace();
            log.error(cause.getMessage());
        }

        if (cause instanceof IOException) {
            log.error(cause.getMessage());
            log.error("客户端已经关闭连接，做下线处理 ");
            ServerSession.closeSession(ctx);
        } else {
            cause.printStackTrace();

            log.error(cause.getMessage());
        }
    }

    /**
     * 通道 Read 读取 Complete 完成
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ServerSession.closeSession(ctx);
    }
}
