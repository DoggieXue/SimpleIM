package org.cloudxue.imServer.processer;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.ProtoInstant;
import org.cloudxue.im.common.bean.User;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imServer.protoConverter.LoginResponseConverter;
import org.cloudxue.imServer.session.ServerSession;
import org.cloudxue.imServer.session.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName LoginProcessor
 * @Description 登录业务逻辑处理器
 *              1、将ProtoBuf数据--->POJO业务数据
 *              2、登录校验
 *              3、完成session与channel的反向绑定
 *              4、回写响应（成功/失败）
 * @Author xuexiao
 * @Date 2021/12/22 上午11:13
 * @Version 1.0
 **/
@Slf4j
@Service("loginProcessor")
public class LoginProcessor implements ServerProcessor{
    @Autowired
    LoginResponseConverter loginResponseConverter;

    @Override
    public ProtoMsg.HeadType type() {
        return ProtoMsg.HeadType.LOGIN_REQUEST;
    }

    /**
     * 此处进行具体的登录请求处理逻辑
     * @param session
     * @param proto
     * @return
     */
    @Override
    public boolean action(ServerSession session, ProtoMsg.Message proto) {

        //取出token验证
        ProtoMsg.LoginRequest info = proto.getLoginRequest();
        long seqNo = proto.getSequence();

        User user = User.fromMsg(info);

        //检查用户
        boolean isValidUser = checkUser(user);
        if (!isValidUser) {
            ProtoInstant.ResultCodeEnum resultCode = ProtoInstant.ResultCodeEnum.NO_TOKEN;
            //构造登录失败的报文
            ProtoMsg.Message response = loginResponseConverter.build(resultCode, seqNo, "-1");
            //发送登录失败的报文
            session.writeAndFlush(response);
            return false;
        }

        session.setUser(user);
        //服务端session和传输channel双向绑定
        session.reverseBind();

        //登录成功
        ProtoInstant.ResultCodeEnum resultCode = ProtoInstant.ResultCodeEnum.SUCCESS;
        //构造登录成功的报文
        ProtoMsg.Message response = loginResponseConverter.build(resultCode, seqNo, session.getSessionId());
        //发送登录成功的报文
        session.writeAndFlush(response);
        return true;
    }

    private boolean checkUser(User user) {
        if (SessionMap.inst().hasLogin(user)) {
            return false;
        }
        //校验用户：比较耗时的操作，需要100 ms以上的时间
        //比如1：调用远程用户RESTful校验服务
        //比如2：调用数据库接口校验
        return true;
    }
}
