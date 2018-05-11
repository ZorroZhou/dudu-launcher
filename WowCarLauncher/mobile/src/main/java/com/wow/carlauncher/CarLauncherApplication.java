package com.wow.carlauncher;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.google.common.base.Strings;
import com.wow.carlauncher.common.AppContext;
import com.wow.frame.declare.SAppDeclare;
import com.wow.carlauncher.common.ActivityLifecycleListener;
import com.wow.carlauncher.service.MainService;
import com.wow.frame.util.AndroidUtil;

public class CarLauncherApplication extends Application implements SAppDeclare {
    @Override
    public void onCreate() {
        super.onCreate();
        String pname = AndroidUtil.getProcessName(getApplicationContext());
        if (Strings.isNullOrEmpty(pname) && !pname.contains("main")) {
            return;
        }
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
