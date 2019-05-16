package com.wow.carlauncher.ex.manage.time;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.time.event.TMEvent30Minute;
import com.wow.carlauncher.ex.manage.time.event.TMEvent3Second;
import com.wow.carlauncher.ex.manage.time.event.TMEvent5Minute;
import com.wow.carlauncher.ex.manage.time.event.TMEventHalfSecond;
import com.wow.carlauncher.ex.manage.time.event.TMEventMinute;
import com.wow.carlauncher.ex.manage.time.event.TMEventSecond;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ScheduledFuture;

/**
 * Created by 10124 on 2018/4/25.
 */

public class TimeManage extends ContextEx {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static TimeManage instance = new TimeManage();
    }

    public static TimeManage self() {
        return TimeManage.SingletonHolder.instance;
    }

    private TimeManage() {
        super();
    }

    public void init(Context context) {
        setContext(context);
        startTimer();
        LogEx.d(this, "init");
    }

    private final static int ZHOUQI = 500;

    private final static int MSECOND = 1000;
    private final static int SECOND = MSECOND / ZHOUQI;

    private final static int MSECOND3 = 3000;
    private final static int SECOND3 = MSECOND3 / ZHOUQI;

    private final static int MMINUTE = 60 * 1000;
    private final static int MINUTE = MMINUTE / ZHOUQI;

    private final static int MMINUTE30 = 30 * 60 * 1000;
    private final static int MINUTE30 = MMINUTE30 / ZHOUQI;

    private final static int MMINUTE5 = 5 * 60 * 1000;
    private final static int MINUTE5 = MMINUTE5 / ZHOUQI;

    private ScheduledFuture<?> timer;
    private long timeMark = 0L;

    private void startTimer() {
        stopTimer();
        LogEx.d(this, "startTimer");
        timer = TaskExecutor.self().repeatRun(() -> {
            try {
                if (EventBus.getDefault().hasSubscriberForEvent(TMEventHalfSecond.class)) {
                    postEvent(new TMEventHalfSecond());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (timeMark % SECOND == 0) {
                    if (EventBus.getDefault().hasSubscriberForEvent(TMEventSecond.class)) {
                        postEvent(new TMEventSecond());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (timeMark % SECOND3 == 0) {
                    if (EventBus.getDefault().hasSubscriberForEvent(TMEvent3Second.class)) {
                        postEvent(new TMEvent3Second());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (timeMark % MINUTE30 == 0) {
                    if (EventBus.getDefault().hasSubscriberForEvent(TMEvent30Minute.class)) {
                        postEvent(new TMEvent30Minute());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (timeMark % MINUTE5 == 0) {
                    if (EventBus.getDefault().hasSubscriberForEvent(TMEvent5Minute.class)) {
                        postEvent(new TMEvent5Minute());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (timeMark % MINUTE == 0) {
                    if (EventBus.getDefault().hasSubscriberForEvent(TMEventMinute.class)) {
                        postEvent(new TMEventMinute());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            timeMark++;
        }, ZHOUQI, ZHOUQI);
    }

    private void stopTimer() {
        LogEx.d(this, "stopTimer");
        if (timer != null) {
            timer.cancel(true);
            timer = null;
        }
    }
}
