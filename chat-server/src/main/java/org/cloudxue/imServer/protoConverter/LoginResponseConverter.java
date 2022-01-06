package org.cloudxue.imServer.protoConverter;

import org.cloudxue.im.common.ProtoInstant;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.springframework.stereotype.Service;

/**
 * @ClassName LoginResponseConverter
 * @Description 登录响应转换器：将响应数据转换成proto数据
 * @Author xuexiao
 * @Date 2021/12/22 上午10:42
 * @Version 1.0
 **/
@Service("loginResponseBuilder")
public class LoginResponseConverter {

    public ProtoMsg.Message build (ProtoInstant.ResultCodeEnum en, long seqId, String sessionId) {
        ProtoMsg.Message.Builder outer = ProtoMsg.Message.newBuilder()
                .setType(ProtoMsg.HeadType.LOGIN_RESPONSE) //设置消息类型
                .setSequence(seqId)
                .setSessionId(sessionId); //设置应答流水，与请求对应
        ProtoMsg.LoginResponse.Builder builder = ProtoMsg.LoginResponse.newBuilder()
                .setResult("-1".equals(sessionId) ? false : true)   //登录响应结果
                .setCode(en.getCode())
                .setInfo(en.getDesc())
                .setExpose(1);

        outer.setLoginResponse(builder.build());
        return outer.build();
    }
}
