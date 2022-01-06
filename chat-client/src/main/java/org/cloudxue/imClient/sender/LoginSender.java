package org.cloudxue.imClient.sender;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imClient.protoConverter.LoginMsgConverter;
import org.springframework.stereotype.Service;

/**
 * @ClassName LoginSender
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/24 上午10:18
 * @Version 1.0
 **/
@Slf4j
@Service("loginSender")
public class LoginSender extends BaseSender{

    public void sendLoginMsg() {
        if (!isConnected()) {
            log.info("连接还未建立");
            return;
        }

        log.info("开始构造登录消息");

        ProtoMsg.Message message = LoginMsgConverter.build(getUser(), getSession());

        log.info("发送登录消息");
        super.sendMsg(message);
    }
}
