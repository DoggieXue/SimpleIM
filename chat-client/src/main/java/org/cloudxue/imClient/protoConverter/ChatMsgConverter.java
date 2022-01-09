package org.cloudxue.imClient.protoConverter;

import org.cloudxue.im.common.bean.ChatMsg;
import org.cloudxue.im.common.bean.User;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imClient.session.ClientSession;

/**
 * @ClassName ChatMsgConverter
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/7 下午4:06
 * @Version 1.0
 **/
public class ChatMsgConverter extends BaseConverter{

    private ChatMsg chatMsg;
    private User user;

    private ChatMsgConverter(ClientSession session) {
        super(ProtoMsg.HeadType.MESSAGE_REQUEST, session);
    }

    public ProtoMsg.Message build(ChatMsg chatMsg, User user) {
        this.chatMsg = chatMsg;
        this.user = user;

        ProtoMsg.Message.Builder outerBuilder = getOuterBuilder(-1);

        ProtoMsg.MessageRequest.Builder cb = ProtoMsg.MessageRequest.newBuilder();
        this.chatMsg.fillMsg(cb);
        ProtoMsg.Message requestMsg = outerBuilder.setMessageRequest(cb).build();

        return requestMsg;
    }

    public static ProtoMsg.Message build(ChatMsg chatMsg, User user, ClientSession session) {
        ChatMsgConverter chatMsgConverter = new ChatMsgConverter(session);

        return chatMsgConverter.build(chatMsg, user);
    }
}
