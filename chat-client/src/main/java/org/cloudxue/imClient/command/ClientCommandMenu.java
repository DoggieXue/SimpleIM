package org.cloudxue.imClient.command;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * @ClassName ClientCommandMenu
 * @Description 命令的类型收集类
 * @Author xuexiao
 * @Date 2021/12/23 下午9:27
 * @Version 1.0
 **/
@Data
@Service("clientCommandMenu")
public class ClientCommandMenu implements BaseCommand{

    public static final String KEY = "0";

    private String allCommandsShow;
    private String commandInput;

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTips() {
        return "show 所有命令";
    }

    @Override
    public void exec(Scanner scanner) {
        System.err.println("请输入某个操作指令： " + allCommandsShow);
        commandInput = scanner.next();
    }

    public void setAllCommand(Map<String, BaseCommand> commandMap) {
        Set<Map.Entry<String, BaseCommand>> entries = commandMap.entrySet();
        Iterator<Map.Entry<String, BaseCommand>> iterator = entries.iterator();

        StringBuilder menus = new StringBuilder();
        menus.append("[menu] ");
        while (iterator.hasNext()) {
            BaseCommand next = iterator.next().getValue();
            menus.append("->").append(next.getTips()).append(" | ");
        }
        allCommandsShow = menus.toString();
    }
}
