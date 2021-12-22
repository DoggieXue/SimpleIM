package org.cloudxue.im.common.codec;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.ProtoInstant;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.im.common.exception.InvalidFrameException;
import org.cloudxue.util.Logger;

import java.util.List;

/**
 * @ClassName SimpleProtobufDecoder
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/21 上午10:15
 * @Version 1.0
 **/
@Slf4j
public class SimpleProtobufDecoder extends ByteToMessageDecoder
{

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object outMsg = decode0(ctx, in);
        if (outMsg != null) {
            //获取业务消息
            out.add(outMsg);
        }
    }

    public static Object decode0(ChannelHandlerContext ctx, ByteBuf in) throws InvalidFrameException, InvalidProtocolBufferException {
        //标记一下当前的readerIndex的位置
        in.markReaderIndex();
        //判断包头长度
        if (in.readableBytes() < 8) {
            return null;
        }
        //读取魔数
        short magic = in.readShort();
        if (magic != ProtoInstant.MAGIC_CODE) {
            String error = "客户端口令不对： " + ctx.channel().remoteAddress();
            throw new InvalidFrameException(error);
        }
        //读取版本
        short version = in.readShort();
        if (version != ProtoInstant.VERSION_CODE) {
            String error = "协议的版本不对： " + ctx.channel().remoteAddress();
            throw new InvalidFrameException(error);
        }
        //读取传送过来的消息长度
        int length = in.readInt();
        //如果长度小于0：数据非法，关闭连接
        if (length < 0) {
            ctx.close();
        }

        if (length > in.readableBytes()) { //如果读到的消息体长度小于传过来的消息长度
            in.resetReaderIndex();
            return null;
        }
        Logger.cfo("decoder length = " + in.readableBytes());

        byte[] array;
        if (in.hasArray()) {
            //堆缓存
            ByteBuf slice = in.slice(in.readerIndex(), length);
            array = slice.array();
//            array = new byte[length];
//            in.readBytes(array, 0, length);
        } else {
            array = new byte[length];
            in.readBytes(array, 0, length);
        }

        //字节转换成对象
        ProtoMsg.Message outMsg = ProtoMsg.Message.parseFrom(array);

        return outMsg;
    }
}
