package com.wow.carlauncher;

import android.app.Application;
import android.content.Intent;

import com.wow.carlauncher.common.ActivityLifecycleListener;
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.service.MainService;
import com.wow.frame.declare.SAppDeclare;

public class CarLauncherApplication extends Application implements SAppDeclare {
    @Override
    public void onCreate() {
        super.onCreate();
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
