package com.wow.carlauncher.ex.manage.speed;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wow.carlauncher.common.LogEx;

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
        this.context = context;
        EventBus.getDefault().register(this);
        LogEx.d(this, "init ");
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(SMEventReceiveSpeed event) {

    }
}
