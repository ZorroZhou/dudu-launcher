package com.wow.carlauncher;

import android.app.Application;
import android.content.Intent;

import com.wow.carlauncher.common.ActivityLifecycleListener;
import com.wow.carlauncher.common.LocationManage;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.popupWindow.PopupWin;
import com.wow.carlauncher.service.MainService;

import org.xutils.x;

/**
 * Created by 10124 on 2017/10/26.
 */

public class CarLauncherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
        SharedPreUtil.init(this);
        PluginManage.self().init(this);
        PopupWin.self().init(this);
        LocationManage.self().init(getApplicationContext());

        registerActivityLifecycleCallbacks(new ActivityLifecycleListener(this));

        Intent startIntent = new Intent(this, MainService.class);
        startService(startIntent);
    }

    private int startedActivityCount = 0;

    public synchronized int checkActivity(int count) {
        startedActivityCount = startedActivityCount + count;
        return startedActivityCount;
    }
}
