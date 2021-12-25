package org.cloudxue.imClient.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.exception.InvalidFrameException;
import org.cloudxue.imClient.client.CommandController;
import org.cloudxue.imClient.session.ClientSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ExceptionHandler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/24 下午3:57
 * @Version 1.0
 **/
@Slf4j
@ChannelHandler.Sharable
@Service("exceptionHandler")
public class ExceptionHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private CommandController commandController;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        if (cause instanceof InvalidFrameException) {
            log.error(cause.getMessage());
            ClientSession.getSession(ctx).close();
        } else {
            log.error(cause.getMessage());
            ctx.close();

            if (null == commandController) {
                return;
            }
            commandController.setConnectFlag(false);
            commandController.startConnectServer();
        }
    }

    /**
     * 通道Read 读取 Complete 完成
     * 刷新操作：ctx.flush()
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
