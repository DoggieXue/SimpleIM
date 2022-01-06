package org.cloudxue.concurrent;

import org.cloudxue.util.ThreadUtil;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName FutureTaskScheduler
 * @Description 辅助类：提交执行没有回调的任务
 * @Author xuexiao
 * @Date 2021/12/24 下午4:55
 * @Version 1.0
 **/
public class FutureTaskScheduler {

    static ThreadPoolExecutor mixPool = null;

    static {
        mixPool = ThreadUtil.getMixedTargetThreadPool();
    }

    private static FutureTaskScheduler inst = new FutureTaskScheduler();

    private FutureTaskScheduler(){}

    public static void add(ExecuteTask executeTask) {
        mixPool.submit(()-> {executeTask.execute();});
    }

}
