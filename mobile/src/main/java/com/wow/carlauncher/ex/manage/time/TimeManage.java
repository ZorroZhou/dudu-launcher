package com.wow.carlauncher.ex.manage.time;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.time.event.MTime30MinuteEvent;
import com.wow.carlauncher.ex.manage.time.event.MTimeHalfSecondEvent;
import com.wow.carlauncher.ex.manage.time.event.MTimeMinuteEvent;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import static com.wow.carlauncher.common.CommonData.TAG;

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
    }

    private final static int ZHOUQI = 500;

    private final static int MSECOND = 1000;
    private final static int SECOND = MSECOND / ZHOUQI;

    private final static int MMINUTE = 60 * 1000;
    private final static int MINUTE = MMINUTE / ZHOUQI;

    private final static int MMINUTE30 = 30 * 60 * 1000;
    private final static int MINUTE30 = MMINUTE30 / ZHOUQI;

    private Timer timer;
    private long timeMark = 0L;

    private void startTimer() {
        stopTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (EventBus.getDefault().hasSubscriberForEvent(MTimeHalfSecondEvent.class)) {
                        postEvent(new MTimeHalfSecondEvent());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (timeMark % SECOND == 0) {
                        if (EventBus.getDefault().hasSubscriberForEvent(MTimeSecondEvent.class)) {
                            postEvent(new MTimeSecondEvent());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (timeMark % MINUTE30 == 0) {
                        if (EventBus.getDefault().hasSubscriberForEvent(MTime30MinuteEvent.class)) {
                            postEvent(new MTime30MinuteEvent());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (timeMark % MINUTE == 0) {
                        if (EventBus.getDefault().hasSubscriberForEvent(MTimeMinuteEvent.class)) {
                            postEvent(new MTimeMinuteEvent());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                timeMark++;
            }
        }, ZHOUQI, ZHOUQI);
    }

    private void stopTimer() {
        if (timer != null) {
            Log.e(TAG, "stopTimer: ");
            timer.cancel();
            timer = null;
        }
    }
}
