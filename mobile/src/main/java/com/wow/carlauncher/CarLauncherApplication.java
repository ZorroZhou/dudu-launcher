package com.wow.carlauncher;

import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.wow.carlauncher.common.ActivityLifecycleListener;
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.service.MainService;

public class CarLauncherApplication extends MultiDexApplication {
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
}
