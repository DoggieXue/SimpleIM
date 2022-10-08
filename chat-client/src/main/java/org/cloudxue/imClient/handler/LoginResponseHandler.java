package org.cloudxue.imClient.handler;

import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.ProtoInstant;
import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imClient.session.ClientSession;
import org.springframework.stereotype.Service;

/**
 * @ClassName LoginResponseHandler
 * @Description 负责处理登录响应
 * @Author xuexiao
 * @Date 2021/12/23 下午9:55
 * @Version 1.0
 **/
@Slf4j
@ChannelHandler.Sharable
@Service("loginResponseHandler")
public class LoginResponseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        //判断类型
        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType headType = pkg.getType();
        if (!headType.equals(ProtoMsg.HeadType.LOGIN_RESPONSE)) {
            super.channelRead(ctx, msg);
            return;
        }

        //判断返回是否成功
        ProtoMsg.LoginResponse info = pkg.getLoginResponse();

        ProtoInstant.ResultCodeEnum result = ProtoInstant.ResultCodeEnum.values()[info.getCode()];

        if (!result.equals(ProtoInstant.ResultCodeEnum.SUCCESS)) {
            //登录失败
            log.info(result.getDesc());
        } else {
            //登录成功：为ClientSession设置服务端返回的sessionID
            ClientSession.loginSuccess(ctx, pkg);
            ChannelPipeline p = ctx.pipeline();
            p.remove(this);
            //在编码器后面，动态插入心跳处理器
            p.addAfter("encoder","heartbeat", new HeartBeatClientHandler());
        }
    }
}
