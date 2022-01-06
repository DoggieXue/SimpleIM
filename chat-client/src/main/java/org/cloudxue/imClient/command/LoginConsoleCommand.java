package org.cloudxue.imClient.command;


import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * @ClassName LoginConsoleCommand
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/23 下午9:37
 * @Version 1.0
 **/
@Data
@Service("loginConsoleCommand")
public class LoginConsoleCommand implements BaseCommand {

    public static final String KEY = "1";

    private String username;
    private String password;

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTips() {
        return "登录";
    }

    @Override
    public void exec(Scanner scanner) {
        System.out.println("请输入用户信息：{id@password}");
        String[] info = null;
        while (true) {
            String input = scanner.next();
            info = input.split("@");
            if (info.length != 2) {
                System.out.println("请按照格式输入：{id@password}: ");
            } else {
                break;
            }
        }
        username = info[0];
        password = info[1];
    }
}
