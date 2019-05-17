package com.wow.carlauncher.ex.manage.speed;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.ex.manage.time.event.TMEvent3Second;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 速度管理器,这里单独管理吧,开发还简单
 */
public class SpeedManage {
    @SuppressLint("StaticFieldLeak")
    private static SpeedManage self;

    public static SpeedManage self() {
        if (self == null) {
            self = new SpeedManage();
        }
        return self;
    }

    private SpeedManage() {
    }

    private Context context;

    public void init(Context context) {
        long t1 = System.currentTimeMillis();
        this.context = context;
        EventBus.getDefault().register(this);
        LogEx.d(this, "init time:" + (System.currentTimeMillis() - t1));
    }

    private long amaptime = 0;

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(SMEventReceiveSpeed event) {
        time = 0;
        if (event.getFrom().equals(SMEventReceiveSpeed.SMReceiveSpeedFrom.AMAP)) {
            amaptime = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - amaptime > 5000) {
            EventBus.getDefault().post(new SMEventSendSpeed().setUse(true).setSpeed(event.getSpeed()));
        } else {
            if (event.getFrom().equals(SMEventReceiveSpeed.SMReceiveSpeedFrom.AMAP)) {
                EventBus.getDefault().post(new SMEventSendSpeed().setUse(true).setSpeed(event.getSpeed()));
            }
        }
    }

    private int time = 0;

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(TMEvent3Second event) {
        time++;
        if (time == 2) {
            EventBus.getDefault().post(new SMEventSendSpeed().setUse(false));
        }
    }
}
