package org.cloudxue.imClient.protoConverter;

import org.cloudxue.im.common.bean.User;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imClient.session.ClientSession;

/**
 * @ClassName HeartBeatMsgConverter
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/9 下午10:25
 * @Version 1.0
 **/
public class HeartBeatMsgConverter extends BaseConverter{

    private final User user;

    public HeartBeatMsgConverter(ProtoMsg.HeadType type, ClientSession session, User user) {
        super(type, session);
        this.user = user;
    }

    public ProtoMsg.Message build () {
        ProtoMsg.Message.Builder outerBuilder = getOuterBuilder(-1);

        ProtoMsg.MessageHeartBeat.Builder inner = ProtoMsg.MessageHeartBeat.newBuilder()
                .setSeq(0)
                .setJson("{\"from\":\"client\"}")
                .setUid(user.getUid());
        return outerBuilder.setHeartBeat(inner).build();
    }
}
