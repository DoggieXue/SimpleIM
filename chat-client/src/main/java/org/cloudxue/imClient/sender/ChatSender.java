package org.cloudxue.imClient.sender;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.bean.ChatMsg;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imClient.protoConverter.ChatMsgConverter;
import org.springframework.stereotype.Service;

/**
 * @ClassName ChatSender
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/7 下午3:58
 * @Version 1.0
 **/
@Slf4j
@Service("chatSender")
public class ChatSender extends BaseSender{
    public void sendChatMsg(String toUid, String content) {
        log.info("构造单体聊天信息");
        ChatMsg chatMsg = new ChatMsg(getUser());
        chatMsg.setContent(content);
        chatMsg.setMsgType(ChatMsg.MSGTYPE.TEXT);
        chatMsg.setTo(toUid);
        chatMsg.setMsgId(System.currentTimeMillis());
        ProtoMsg.Message message = ChatMsgConverter.build(chatMsg, getUser(), getSession());
        log.info("发送聊天消息");
        super.sendMsg(message);
    }

    @Override
    protected void sendSuccess(ProtoMsg.Message message) {
        log.info("发送成功：" + message.getMessageRequest().getContent());
    }

    @Override
    protected void sendFailed(ProtoMsg.Message message) {
        log.info("发送失败： " + message.getMessageRequest().getContent());
    }
}
