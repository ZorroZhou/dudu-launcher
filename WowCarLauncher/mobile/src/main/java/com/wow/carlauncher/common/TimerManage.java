package com.wow.carlauncher.common;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wow.carlauncher.common.ex.ContextEx;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 10124 on 2018/4/25.
 */

public class TimerManage extends ContextEx {
    private final static int HELF_SECOND = 500;

    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static TimerManage instance = new TimerManage();
    }

    public static TimerManage self() {
        return TimerManage.SingletonHolder.instance;
    }

    private TimerManage() {
        super();
    }


    public void init(Context context) {
        setContext(context);
        startTimer();
    }

    private Timer timer;

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


            }
        }, HELF_SECOND, HELF_SECOND);
    }
}
