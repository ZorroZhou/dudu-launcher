package com.wow.carlauncher;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.common.CrashUtil;
import com.wow.carlauncher.plugin.amapcar.AMapCarPlugin;
import com.wow.frame.declare.SAppDeclare;
import com.wow.carlauncher.common.ActivityLifecycleListener;
import com.wow.carlauncher.service.MainService;

public class CarLauncherApplication extends Application implements SAppDeclare {
    @Override
    public void onCreate() {
        super.onCreate();
//        CrashUtil crashUtil = CrashUtil.getInstance();
//        crashUtil.init(this);
        AppContext.self().init(this);

        registerActivityLifecycleCallbacks(new ActivityLifecycleListener());

        Intent startIntent = new Intent(this, MainService.class);
        startService(startIntent);
    }

    private int startedActivityCount = 0;

    public synchronized int checkActivity(int count) {
        startedActivityCount = startedActivityCount + count;
        return startedActivityCount;
    }

    @Override
    public Application getApplication() {
        return this;
    }
}
