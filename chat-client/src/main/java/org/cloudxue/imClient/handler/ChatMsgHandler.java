package org.cloudxue.imClient.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.springframework.stereotype.Service;

/**
 * @ClassName ChatMsgHandler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/9 下午9:15
 * @Version 1.0
 **/
@Service("chatMsgHandler")
@ChannelHandler.Sharable
@Slf4j
public class ChatMsgHandler extends ChannelInboundHandlerAdapter {

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
        //不是聊天消息
        if (!headType.equals(ProtoMsg.HeadType.MESSAGE_REQUEST)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMsg.MessageRequest request = pkg.getMessageRequest();
        String content = request.getContent();
        String uid = request.getFrom();
        log.info("收到消息 from uid: " + uid + " -> " + content);

    }
}
