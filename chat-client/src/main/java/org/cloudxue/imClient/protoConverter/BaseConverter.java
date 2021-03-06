package org.cloudxue.imClient.protoConverter;

import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imClient.session.ClientSession;

/**
 * @ClassName BaseConventer
 * @Description 基类消息构造器：完成业务数据bean对象--->protobuf消息数据报的构造
 *              bean --->  Builder ---> protobuf数据包
 * @Author xuexiao
 * @Date 2021/12/24 上午10:23
 * @Version 1.0
 **/
public class BaseConverter {
    protected ProtoMsg.HeadType type;

    private long seqId;
    private ClientSession session;

    public BaseConverter(ProtoMsg.HeadType type, ClientSession session) {
        this.type = type;
        this.session = session;
    }

    public ProtoMsg.Message buildOuter (long seqId) {
        return getOuterBuilder(seqId).buildPartial();
    }

    public ProtoMsg.Message.Builder getOuterBuilder(long seqId) {
        this.seqId = seqId;

        ProtoMsg.Message.Builder mb = ProtoMsg.Message.newBuilder()
                .setType(type)
                .setSessionId(session.getSessionId())
                .setSequence(seqId);
        return mb;
    }

    public ProtoMsg.Message.Builder baseBuilder(long seqId) {
        this.seqId = seqId;
        ProtoMsg.Message.Builder mb = ProtoMsg.Message.newBuilder()
                .setType(type)
                .setSessionId(session.getSessionId())
                .setSequence(seqId);
        return mb;
    }
}
