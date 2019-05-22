package com.wow.carlauncher;

import android.app.Application;
import android.content.Intent;

import com.wow.carlauncher.common.ActivityLifecycleListener;
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.service.MainService;

import static com.wow.carlauncher.common.util.AppUtil.getCurProcessName;

public class CarLauncherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if ("com.wow.carlauncher".equals(getCurProcessName(this))) {
            AppContext.self().init(this);

            registerActivityLifecycleCallbacks(new ActivityLifecycleListener());

            Intent startIntent = new Intent(this, MainService.class);
            startService(startIntent);
        }
    }

    private int startedActivityCount = 0;

    public synchronized int checkActivity(int count) {
        startedActivityCount = startedActivityCount + count;
        return startedActivityCount;
    }
}
