package org.cloudxue.concurrent;

/**
 * 有回调处理的异步任务执行
 * @param <R>
 */
public interface CallBackTask<R> {

    R execute() throws Exception;

    void onBack(R r);

    void onException(Throwable t);
}
