package org.cloudxue.concurrent;

/**
 * 异步任务执行
 * @param <R>
 */
public interface CallBackTask<R> {

    R extcute() throws Exception;

    void onBack(R r);

    void onException(Throwable t);
}
