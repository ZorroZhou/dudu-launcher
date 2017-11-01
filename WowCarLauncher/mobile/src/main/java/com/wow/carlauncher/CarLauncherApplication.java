package com.wow.carlauncher;

import android.app.Application;

import com.wow.carlauncher.common.ActivityLifecycleListener;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.popupWindow.PopupWindow;

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
    }
}
