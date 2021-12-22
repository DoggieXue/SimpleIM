package org.cloudxue.concurrent;

import com.google.common.util.concurrent.*;
import org.cloudxue.util.ThreadUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * @ClassName CallbackTaskScheduler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/22 下午3:05
 * @Version 1.0
 **/
public class CallbackTaskScheduler {
    static ListeningExecutorService guavaPool = null;

    static {
        ExecutorService jPool = ThreadUtil.getMixedTargetThreadPool();
        guavaPool = MoreExecutors.listeningDecorator(jPool);
    }

    private CallbackTaskScheduler(){

    }

    /**
     * 添加执行任务
     * @param executeTask
     * @param <R>
     */
    public static <R> void add (CallBackTask<R> executeTask) {
        ListenableFuture<R> future = guavaPool.submit(new Callable<R>() {
            @Override
            public R call() throws Exception {
                R r = executeTask.extcute();
                return r;
            }
        });

        Futures.addCallback(future, new FutureCallback<R>() {
            @Override
            public void onSuccess(R r) {
                executeTask.onBack(r);
            }

            @Override
            public void onFailure(Throwable throwable) {
                executeTask.onException(throwable);
            }
        });
    }
}
