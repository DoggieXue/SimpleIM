package org.cloudxue.im.common.protoBuilder;

import org.cloudxue.im.common.bean.msg.ProtoMsg;

/**
 * @ClassName NotificationMsgBuilder
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/21 上午10:03
 * @Version 1.0
 **/
public class NotificationMsgBuilder {
    public static ProtoMsg.Message buildNotification(String json) {
        ProtoMsg.Message.Builder mb = ProtoMsg.Message.newBuilder().setType(ProtoMsg.HeadType.MESSAGE_NOTIFICATION);
        ProtoMsg.MessageNotification.Builder rb = ProtoMsg.MessageNotification.newBuilder().setJson(json);
        mb.setNotification(rb.build());
        return mb.build();
    }
}
