package com.wow.carlauncher.common;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * 统一任务调度
 */
public class TaskExecutor {
    /**
     * 单例模式的控制中心
     */
    private static class SingletonHolder {
        private final static TaskExecutor instance = new TaskExecutor();
    }

    public static TaskExecutor self() {
        return TaskExecutor.SingletonHolder.instance;
    }

    private TaskExecutor() {

    }

    public void init() {
        handler = new Handler();
        scheduled = new ScheduledThreadPoolExecutor(2);
        task = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        Log.e(TAG + getClass().getSimpleName(), "init ");
    }


    private ScheduledExecutorService scheduled;
    private ExecutorService task;
    private Handler handler;

    public void autoPost(Runnable runnable) {
        if (runnable == null) return;
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            runnable.run();
        } else {
            handler.post(runnable);
        }
    }

    /**
     * 子线程执行
     *
     * @param runnable
     */
    public void run(Runnable runnable) {
        task.execute(runnable);
    }

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
