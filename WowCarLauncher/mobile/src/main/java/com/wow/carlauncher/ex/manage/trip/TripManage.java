package com.wow.carlauncher.ex.manage.trip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarInfo;
import com.wow.carlauncher.view.activity.driving.DrivingActivity;
import com.wow.carlauncher.view.activity.launcher.LauncherActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.x;

/**
 * 行程管理器
 * Created by 10124 on 2018/5/11.
 */

public class TripManage extends ContextEx {
    private final static String TAG = "WOW_CAR 行程管理器:";

    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static TripManage instance = new TripManage();
    }

    public static TripManage self() {
        return TripManage.SingletonHolder.instance;
    }

    private boolean drivingShow = false;

    public boolean isDrivingShow() {
        return drivingShow;
    }

    public TripManage setDrivingShow(boolean drivingShow) {
        this.drivingShow = drivingShow;
        return this;
    }

    private TripManage() {
        super();
    }

    public void init(Context context) {
        setContext(context);
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PObdEventCarInfo event) {
        Log.d(TAG, "onEventMainThread: " + event);
        if (event.getRev() != null && event.getRev() > 400) {
            if (!drivingShow) {
                Intent intent2 = new Intent(getContext(), DrivingActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent2);
            }
        }
    }
}
