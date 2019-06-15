package com.wow.carlauncher.ex.manage.speed;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.time.event.TMEvent3Second;
import com.wow.carlauncher.ex.manage.time.event.TMEventSecond;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarInfo;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 速度管理器,这里单独管理吧,开发还简单
 */
public class SpeedManage extends ContextEx {
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

    public void init(Context context) {
        long t1 = System.currentTimeMillis();
        setContext(context);
        EventBus.getDefault().register(this);
        LogEx.d(this, "init time:" + (System.currentTimeMillis() - t1));
    }

    private long amaptime = 0;
    private long obdtime = 0;

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(SMEventReceiveSpeed event) {
        time = 0;
        if (event.getFrom().equals(SMEventReceiveSpeed.SMReceiveSpeedFrom.AMAP)) {
            amaptime = System.currentTimeMillis();
            cameraSpeed = event.getCameraSpeed();
        }
        if (event.getFrom().equals(SMEventReceiveSpeed.SMReceiveSpeedFrom.OBD)) {
            obdtime = System.currentTimeMillis();
            speed = event.getSpeed();
        }
        if (event.getFrom().equals(SMEventReceiveSpeed.SMReceiveSpeedFrom.AMAP) &&
                System.currentTimeMillis() - obdtime > 5000) {
            speed = event.getSpeed();
        } else if (event.getFrom().equals(SMEventReceiveSpeed.SMReceiveSpeedFrom.GPS) &&
                System.currentTimeMillis() - obdtime > 5000 &&
                System.currentTimeMillis() - amaptime > 5000) {
            speed = event.getSpeed();
        }

//        else if (System.currentTimeMillis() - amaptime > 5000) {
//            EventBus.getDefault().post(new SMEventSendSpeed().setUse(true).setSpeed(event.getSpeed()));
//        } else {
//            if (event.getFrom().equals(SMEventReceiveSpeed.SMReceiveSpeedFrom.AMAP)) {
//                EventBus.getDefault().post(new SMEventSendSpeed().setUse(true).setSpeed(event.getSpeed()));
//            }
//        }
    }

    private int speed = 0;
    private int cameraSpeed = 0;
    private int time = 0;

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(TMEventSecond event) {
        time++;
        if (time >= 20) {
            EventBus.getDefault().post(new SMEventSendSpeed().setUse(false));
        } else {
            EventBus.getDefault().post(new SMEventSendSpeed().setUse(true).setSpeed(speed).setCameraSpeed(cameraSpeed));
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final PObdEventCarInfo event) {
        if (ObdPlugin.self().isConnect()) {
            onEvent(new SMEventReceiveSpeed().setSpeed(event.getSpeed()).setFrom(SMEventReceiveSpeed.SMReceiveSpeedFrom.OBD));
        }
    }
}
