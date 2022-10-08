package org.cloudxue.imClient.command;

import org.cloudxue.util.Logger;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * @ClassName LogoutConsoleCommand
 * @Description 退出命令的信息收集类
 * @Author xuexiao
 * @Date 2021/12/23 下午9:41
 * @Version 1.0
 **/
@Service("logoutConsoleCommand")
public class LogoutConsoleCommand implements BaseCommand{

    public static final String KEY = "10";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTips() {
        return "退出";
    }

    @Override
    public void exec(Scanner scanner) {
        Logger.cfo("退出命令执行成功");
    }
}
