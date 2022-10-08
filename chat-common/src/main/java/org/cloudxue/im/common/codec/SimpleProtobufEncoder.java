package org.cloudxue.im.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.ProtoInstant;
import org.cloudxue.im.common.bean.msg.ProtoMsg;

/**
 * @ClassName SimpleProtobufEncoder
 * @Description 自定义Protobuf编码器，用于解决复杂Head-Content协议的解析
 *              负责Head-Content协议的数据包结构
 *              魔数|版本|长度-Content
 * @Author xuexiao
 * @Date 2021/12/21 上午10:16
 * @Version 1.0
 **/
@Slf4j
public class SimpleProtobufEncoder extends MessageToByteEncoder<ProtoMsg.Message> {

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
//        out.writeInt(length);
        //short类型包含两个字节，每个字节由8个二进制位构成，数据包的最大的净负荷长度为：32767（约32K）
        out.writeShort(length);
        //再将消息体中包含的要发送的数据写入
        out.writeBytes(bytes);
    }
}
