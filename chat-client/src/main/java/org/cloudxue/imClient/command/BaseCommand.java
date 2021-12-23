package org.cloudxue.imClient.command;

import java.util.Scanner;

public interface BaseCommand {
    /**
     * 获取命令Key
     * @return
     */
    String getKey();

    /**
     * 获取命令的提示信息
     * @return
     */
    String getTips();

    /**
     * 从控制台提取业务数据
     * @param scanner
     */
    void exec(Scanner scanner);
}
