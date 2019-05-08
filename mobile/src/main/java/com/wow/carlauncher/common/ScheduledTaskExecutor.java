package com.wow.carlauncher.common;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 统一任务调度
 */
public class ScheduledTaskExecutor {
    /**
     * 单例模式的控制中心
     */
    private static class SingletonHolder {
        private final static ScheduledTaskExecutor instance = new ScheduledTaskExecutor();
    }

    public static ScheduledTaskExecutor self() {
        return ScheduledTaskExecutor.SingletonHolder.instance;
    }

    private ScheduledTaskExecutor() {

    }

    public void init() {
        scheduled = new ScheduledThreadPoolExecutor(2);
        LogEx.d(this, "init ");
    }


    private ScheduledExecutorService scheduled;

    /**
     * 计划调度
     *
     * @param runnable     执行的信息
     * @param initialDelay 延迟多长时间
     * @param interval     间隔时间
     */
    public ScheduledFuture<?> repeatRun(Runnable runnable, long initialDelay, long interval) {
        return scheduled.scheduleWithFixedDelay(runnable, initialDelay, interval, TimeUnit.MILLISECONDS);
    }

    /**
     * 计划调度
     *
     * @param runnable 执行的信息
     * @param interval 间隔时间
     */
    public ScheduledFuture<?> repeatRun(Runnable runnable, long interval) {
        return scheduled.scheduleWithFixedDelay(runnable, 0, interval, TimeUnit.MILLISECONDS);
    }

    /**
     * 延迟执行,这个是一次性的
     *
     * @param runnable 执行的信息
     * @param delay    延迟时间
     * @return
     */
    public ScheduledFuture<?> run(Runnable runnable, long delay) {
        return scheduled.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

}
