package org.cloudxue.im.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.ProtoInstant;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.util.Logger;

/**
 * @ClassName SimpleProtobufEncoder
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/21 上午10:16
 * @Version 1.0
 **/
@Slf4j
public class SimpleProtobufEncoder extends MessageToByteEncoder<ProtoMsg .Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ProtoMsg.Message msg, ByteBuf out) throws Exception {
        out.writeShort(ProtoInstant.MAGIC_CODE);
        out.writeShort(ProtoInstant.VERSION_CODE);

        //将ProtoMsg.Message对象转换为byte
        byte[] bytes = msg.toByteArray();

        //TODO： 加密消息体

        int length = bytes.length;
        log.info("encoder length = " + length);

        //先将消息长度（消息头）写入
        out.writeInt(length);
        //再将消息体中包含的要发送的数据写入
        out.writeBytes(bytes);
    }
}
