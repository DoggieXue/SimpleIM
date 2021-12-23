package org.cloudxue.imClient.command;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * @ClassName ChatConsoleCommand
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/23 下午9:44
 * @Version 1.0
 **/
@Data
@Service("chatConsoleCommand")
public class ChatConsoleCommand implements BaseCommand{

    public static final String KEY = "2";
    private String toUserId;
    private String message;

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTips() {
        return "聊天";
    }

    @Override
    public void exec(Scanner scanner) {
        System.out.println("请输入聊天的消息{id:message}: ");
        String[] info = null;
        while (true) {
            String input = scanner.next();
            info = input.split(":");
            if (info.length != 2) {
                System.out.println("请输入聊天的消息{id:message}: ");
            } else {
                break;
            }
        }
        toUserId = info[0];
        message = info[1];
    }
}
