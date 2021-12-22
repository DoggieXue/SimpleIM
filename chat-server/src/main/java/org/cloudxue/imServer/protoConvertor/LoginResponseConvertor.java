package org.cloudxue.imServer.protoConvertor;

import org.cloudxue.im.common.ProtoInstant;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.springframework.stereotype.Service;

/**
 * @ClassName LoginResponseConvertor
 * @Description 登录响应转换器
 * @Author xuexiao
 * @Date 2021/12/22 上午10:42
 * @Version 1.0
 **/
@Service("LoginResponseBuilder")
public class LoginResponseConvertor {

    public ProtoMsg.Message build (ProtoInstant.ResultCodeEnum en, long seqId, String sessionId) {
        ProtoMsg.Message.Builder outer = ProtoMsg.Message.newBuilder()
                .setType(ProtoMsg.HeadType.LOGIN_RESPONSE)
                .setSequence(seqId)
                .setSessionId(sessionId);
        ProtoMsg.LoginResponse.Builder builder = ProtoMsg.LoginResponse.newBuilder()
                .setCode(en.getCode())
                .setInfo(en.getDesc())
                .setExpose(1);

        outer.setLoginResponse(builder.build());
        return outer.build();
    }
}
