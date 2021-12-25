package org.cloudxue.imClient.protoConvertor;

import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imClient.session.ClientSession;

/**
 * @ClassName LoginMsgContentor
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/24 上午10:20
 * @Version 1.0
 **/
public class LoginMsgConventor extends BaseConventor{

    public LoginMsgConventor(ProtoMsg.HeadType type, ClientSession session) {
        super(type, session);
    }
}
