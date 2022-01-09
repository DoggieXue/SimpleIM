package org.cloudxue.imServer.processer;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imServer.session.ServerSession;
import org.cloudxue.imServer.session.SessionMap;
import org.cloudxue.util.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ChatRedirectProcessor
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/9 下午8:48
 * @Version 1.0
 **/
@Slf4j
@Service("chatRedirectProcessor")
public class ChatRedirectProcessor implements ServerProcessor{

    @Override
    public ProtoMsg.HeadType type() {
        return ProtoMsg.HeadType.MESSAGE_REQUEST;
    }

    @Override
    public boolean action(ServerSession fromSession, ProtoMsg.Message proto) {
        //处理聊天
        ProtoMsg.MessageRequest msg = proto.getMessageRequest();
        log.info("chatMsg | from = " + msg.getFrom() + ", to = " + msg.getTo() + ", content = " + msg.getContent());

        String to = msg.getTo();

        List<ServerSession> toSessions = SessionMap.inst().getSessionBy(to);
        if (toSessions == null) {
            Logger.tcfo("[" + to + "] 不在线，发送失败！！" );
        } else {
            toSessions.forEach(serverSession -> {
                //将IM发送到接收方
                serverSession.writeAndFlush(proto);
            });
        }
        return true;
    }
}
