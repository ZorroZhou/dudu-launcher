package com.wow.carlauncher;

import android.app.Application;
import android.content.Intent;

import com.wow.frame.SFrame;
import com.wow.frame.declare.SAppDeclare;
import com.wow.frame.util.SharedPreUtil;
import com.wow.carlauncher.common.ActivityLifecycleListener;
import com.wow.carlauncher.common.AppInfoManage;
import com.wow.carlauncher.common.AppWidgetManage;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.console.ConsoleManage;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.popupWindow.ConsoleWin;
import com.wow.carlauncher.popupWindow.PopupWin;
import com.wow.carlauncher.service.MainService;

public class CarLauncherApplication extends Application implements SAppDeclare {
    @Override
    public void onCreate() {
        super.onCreate();

        SFrame.init(this);

        AppInfoManage.self().init(this);
        AppWidgetManage.self().init(this);
        ConsoleManage.self().init(this);
        PluginManage.self().init(this);
        PopupWin.self().init(this);
        ConsoleWin.self().init(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleListener(this));

        Intent startIntent = new Intent(this, MainService.class);
        startService(startIntent);

        int size = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_SIZE, 1);
        PopupWin.self().setRank(size + 1);
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
