package org.cloudxue.imClient.protoConvertor;

import org.cloudxue.im.common.bean.User;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imClient.session.ClientSession;

/**
 * @ClassName LoginMsgConvertor
 * @Description 登录消息 Convertor
 * @Author xuexiao
 * @Date 2021/12/24 上午10:20
 * @Version 1.0
 **/
public class LoginMsgConvertor extends BaseConvertor {
    private final User user;

    public LoginMsgConvertor(User user, ClientSession session) {
        super(ProtoMsg.HeadType.LOGIN_REQUEST, session);
        this.user = user;
    }

    public ProtoMsg.Message build() {
        ProtoMsg.Message.Builder outerBuilder = getOuterBuilder(-1);

        ProtoMsg.LoginRequest.Builder lb = ProtoMsg.LoginRequest.newBuilder()
                .setDeviceId(user.getDeviceId())
                .setPlatform(user.getPlatform().ordinal())
                .setToken(user.getToken())
                .setUid(user.getUid());

        ProtoMsg.Message requestMsg = outerBuilder.setLoginRequest(lb).build();
        return requestMsg;
    }

    public static ProtoMsg.Message build (User user, ClientSession session) {
        LoginMsgConvertor convertor = new LoginMsgConvertor(user, session);
        return convertor.build();
    }
}
