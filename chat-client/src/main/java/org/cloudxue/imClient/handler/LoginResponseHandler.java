package org.cloudxue.imClient.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @ClassName LoginResponseHandler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/23 下午9:55
 * @Version 1.0
 **/
public class LoginResponseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}
