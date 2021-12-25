package org.cloudxue.imClient.starter;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.imClient.client.CommandController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName ClientApplication
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/24 上午11:19
 * @Version 1.0
 **/
@Slf4j
//使包路径下带有@Value的注解自动注入
//使包路径下带有@Autowired的类可以自动注入
@ComponentScan("org.cloudxue.imClient")
@SpringBootApplication
public class ClientApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ClientApplication.class, args);

        startChatClient(context);
    }

    private static void startChatClient(ApplicationContext context) {
        CommandController commandClient = context.getBean(CommandController.class);

        commandClient.initCommandMap();
        try {
            commandClient.commandThreadRunning();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
