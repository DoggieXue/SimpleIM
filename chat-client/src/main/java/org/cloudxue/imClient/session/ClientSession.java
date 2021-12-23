package org.cloudxue.imClient.session;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.bean.User;

import java.nio.channels.Channel;

/**
 * @ClassName ClientSession
 * @Description 客户端Session会话
 * @Author xuexiao
 * @Date 2021/12/23 下午9:56
 * @Version 1.0
 **/
@Data
@Slf4j
public class ClientSession {

    /**
     * 用户实现客户端会话管理的核心
     */
    private Channel channel;
    private User user;

    /**
     * 保存登录后的服务端sessionId
     */
    private String sessionId;

    private boolean isConnected = false;
    private boolean isLogin = false;
}
