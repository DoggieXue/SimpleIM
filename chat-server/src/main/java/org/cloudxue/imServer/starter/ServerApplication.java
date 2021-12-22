package org.cloudxue.imServer.starter;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.imServer.server.ChatServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ServerApplication
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/22 下午3:50
 * @Version 1.0
 **/
//自动装载配置信息
@Configuration
//使包路径下带有@Value的注解自动注入
//使包路径下带有@Autowired的类可以自动注入
@ComponentScan("org.cloudxue.imServer")
@SpringBootApplication
@Slf4j
public class ServerApplication {
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ServerApplication.class, args);

        startChatServer(context);
    }

    private static void startChatServer(ApplicationContext context) {
        ChatServer nettyServer = context.getBean(ChatServer.class);
        nettyServer.run();
    }
}
