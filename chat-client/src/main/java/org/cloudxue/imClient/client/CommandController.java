package org.cloudxue.imClient.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.concurrent.FutureTaskScheduler;
import org.cloudxue.im.common.bean.User;
import org.cloudxue.imClient.command.*;
import org.cloudxue.imClient.sender.ChatSender;
import org.cloudxue.imClient.sender.LoginSender;
import org.cloudxue.imClient.session.ClientSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CommandController
 * @Description 代表了整个客户端，收集各种类型的命令及内容，组装成POJO对象，
 *              然后调用对应的消息发送器向服务器
 * @Author xuexiao
 * @Date 2021/12/24 上午11:23
 * @Version 1.0
 **/
@Slf4j
@Data
@Service("commandController")
public class CommandController {
    /**
     * 菜单命令收集类
     */
    @Autowired
    ClientCommandMenu clientCommandMenu;
    /**
     * 登录命令收集类
     */
    @Autowired
    LoginConsoleCommand loginConsoleCommand;
    /**
     * 登出命令收集类
     */
    @Autowired
    LogoutConsoleCommand logoutConsoleCommand;

    @Autowired
    ChatConsoleCommand chatConsoleCommand;

    private Map<String, BaseCommand> commandMap;

    private String menuString;

    @Autowired
    private ChatNettyClient chatNettyClient;

    private ClientSession session;
    private Channel channel;

    @Autowired
    private LoginSender loginSender;
    @Autowired
    private ChatSender chatSender;

    private boolean connectFlag = false;
    private User user;

    /**
     * 客户端与服务端断开【异步】监听器
     */
    GenericFutureListener<ChannelFuture> closeListener = (ChannelFuture f) -> {
        log.info(new Date() + ":  连接已经断开...");
        channel = f.channel();

        ClientSession session = channel.attr(ClientSession.SESSION_LEY).get();
        session.close();

        //唤醒用户线程
        notifyCommandThread();
    };

    /**
     * 客户端连接服务端【异步】监听器
     */
    GenericFutureListener<ChannelFuture> connectedListener = (ChannelFuture f) -> {
        final EventLoop eventLoop = f.channel().eventLoop();

        if (!f.isSuccess()) {
            log.info("连接失败！在10S之后准备尝试重连！");
            eventLoop.schedule(() -> chatNettyClient.doConnect(), 10, TimeUnit.SECONDS);

            connectFlag = false;
        } else {
            connectFlag = true;
            log.info("SimpleIM 服务器 连接成功");

            channel = f.channel();
            //创建客户端会话
            session = new ClientSession(channel);
            session.setConnected(true);

            channel.closeFuture().addListener(closeListener);

            //唤醒用户线程
            notifyCommandThread();
        }
    };

    public void initCommandMap() {
        commandMap = new HashMap<>();
        commandMap.put(clientCommandMenu.getKey(), clientCommandMenu);
        commandMap.put(loginConsoleCommand.getKey(), loginConsoleCommand);
        commandMap.put(chatConsoleCommand.getKey(), chatConsoleCommand);
        commandMap.put(logoutConsoleCommand.getKey(), logoutConsoleCommand);

        clientCommandMenu.setAllCommand(commandMap);
    }

    public void startConnectServer() {
        FutureTaskScheduler.add(() -> {
            chatNettyClient.setConnectedListener(connectedListener);
            chatNettyClient.doConnect();
        });
    }

    /**
     * 唤醒，命令收集器
     */
    public synchronized void notifyCommandThread () {
        this.notify();
    }

    /**
     * 休眠，命令收集器
     */
    public synchronized void waitCommandThread () {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void commandThreadRunning () throws InterruptedException {
        Thread.currentThread().setName("命令线程");

        while (true) {
            //建立连接
            while (connectFlag == false) {
                //开始连接: 1）设置连接监听器、2）执行连接操作、3）连接成功创建客户端session、4）设置连接关闭监听器
                startConnectServer();

                waitCommandThread();
            }
            //处理命令
            while (null != session) {
                Scanner scanner = new Scanner(System.in);
                clientCommandMenu.exec(scanner);
                String key = clientCommandMenu.getCommandInput();
                BaseCommand command = commandMap.get(key);
                if (null == command) {
                    System.out.println("无法识别【" + key + "】指令，请重新输入！");
                    continue;
                }

                switch (key) {
                    case LoginConsoleCommand.KEY:
                        command.exec(scanner);
                        startLogin((LoginConsoleCommand) command);
                        break;
                    case LogoutConsoleCommand.KEY:
                        command.exec(scanner);
                        startLogout((LogoutConsoleCommand) command);
                        break;
                    case ChatConsoleCommand.KEY:
                        command.exec(scanner);
                        startOneChat((ChatConsoleCommand) command);
                        break;
                }
            }
        }
    }

    /**
     * 登录
     * @param command
     */
    private void startLogin(LoginConsoleCommand command) {
        if (!isConnectFlag()) {
            log.info("连接异常，请重新建立连接");
            return;
        }
        User user = new User();
        user.setUid(command.getUsername());
        user.setToken(command.getPassword());
        user.setDeviceId("0987654321");
        this.user = user;

        session.setUser(user);

        loginSender.setUser(user);
        loginSender.setSession(session);
        //发送登录信息
        loginSender.sendLoginMsg();
    }

    /**
     * 退出
     * @param command
     */
    private void startLogout(LogoutConsoleCommand command) {
        if (!isLogin()) {
            log.info("还没有登录，请先登录");
            return;
        }
        //TODO:登出逻辑
    }

    /**
     * 发送单体消息
     * @param command
     */
    private void startOneChat (ChatConsoleCommand command) {
        if (!isLogin()) {
            log.info("还没有登录，请先登录");
            return;
        }

        chatSender.setSession(session);
        chatSender.setUser(user);

        chatSender.sendChatMsg(command.getToUserId(), command.getMessage());
    }

    public boolean isLogin() {
        if (null == session) {
            log.info("session is null");
            return false;
        }
        return session.isLogin();
    }
}
