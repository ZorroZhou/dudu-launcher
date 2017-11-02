package com.wow.carlauncher;

import android.app.Application;
import android.content.Intent;

import com.wow.carlauncher.common.ActivityLifecycleListener;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.popupWindow.PopupWindow;
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
        PluginManage.init(this);
        PopupWindow.self().init(this);

        registerActivityLifecycleCallbacks(new ActivityLifecycleListener());

        Intent startIntent = new Intent(this, MainService.class);
        startService(startIntent);
    }
}
