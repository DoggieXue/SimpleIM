package org.cloudxue.imClient.sender;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.bean.User;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imClient.session.ClientSession;

/**
 * @ClassName BaseSender
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/23 下午9:59
 * @Version 1.0
 **/
@Slf4j
@Data
public abstract class BaseSender {
    private User user;
    private ClientSession session;

    protected void sendSucced(ProtoMsg.Message message) {
        log.info("发送成功");

    }

    protected void sendfailed(ProtoMsg.Message message) {
        log.info("发送失败");
    }
}
