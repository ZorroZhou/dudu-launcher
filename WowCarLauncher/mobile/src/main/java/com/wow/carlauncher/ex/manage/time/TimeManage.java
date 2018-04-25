package com.wow.carlauncher.ex.manage.time;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.time.event.MTimeHSecondEvent;

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

    private final static int HELF_SECOND = 500;
    private Timer timer;

    private void startTimer() {
        stopTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                postEvent(new MTimeHSecondEvent());
            }
        }, HELF_SECOND, HELF_SECOND);
    }

    private void stopTimer() {
        if (timer != null) {
            Log.e(TAG, "stopTimer: ");
            timer.cancel();
            timer = null;
        }
    }
}
