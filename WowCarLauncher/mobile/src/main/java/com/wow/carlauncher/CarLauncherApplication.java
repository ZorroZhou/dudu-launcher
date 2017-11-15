package com.wow.carlauncher;

import android.app.Application;
import android.content.Intent;

import com.wow.carlauncher.common.ActivityLifecycleListener;
import com.wow.carlauncher.common.AppInfoManage;
import com.wow.carlauncher.common.AppWidgetManage;
import com.wow.carlauncher.common.console.ConsoleManage;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.popupWindow.ConsoleWin;
import com.wow.carlauncher.popupWindow.PopupWin;
import com.wow.carlauncher.service.MainService;


import org.xutils.x;

public class CarLauncherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
        SharedPreUtil.init(this);
        AppInfoManage.self().init(this);
        AppWidgetManage.self().init(this);
        ConsoleManage.self().init(this);
        PluginManage.self().init(this);
        PopupWin.self().init(this);
        ConsoleWin.self().init(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleListener(this));

        Intent startIntent = new Intent(this, MainService.class);
        startService(startIntent);


//        public static final String UPDATE_INTERFACE = "com.ijidou.music.UPDATE_INTERFACE";
//        public static final String UPDATE_MUSIC_INFO = "com.ijidou.action.UPDATE_MUSIC_INFO";
//        public static final String UPDATE_NOTIFICATION = "com.ijidou.music.UPDATE_NOTIFICATION";
//        public static final String UPDATE_PLAY_RECORD = "com.ijidou.music.UPDATE_PLAY_RECORD";
//        public static final String UPDATE_PROGRESS = "com.ijidou.action.UPDATE_PROGRESS";
//        public static final String UPDATE_USER_PLAY_STATUS = "com.ijidou.action.UPDATE_USER_PLAY_STATUS";

//        IntentFilter intentFilter = new IntentFilter("com.ijidou.action.UPDATE_MUSIC_INFO");
//        intentFilter.addAction("com.ijidou.action.UPDATE_INTERFACE");
//        intentFilter.addAction("com.ijidou.action.UPDATE_USER_PLAY_STATUS");
//        intentFilter.addAction("com.ijidou.action.UPDATE_NOTIFICATION");
//        intentFilter.addAction("com.ijidou.action.UPDATE_PROGRESS");
//        intentFilter.addAction("com.ijidou.action.UPDATE_PLAY_RECORD");
//        intentFilter.addAction("com.ijidou.card.music");
//
//        this.registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Bundle b = intent.getExtras();
//                for (String key : b.keySet()) {
//                    Log.e(TAG, key + ":" + b.get(key));
//                }
//            }
//        }, intentFilter);
    }

    private int startedActivityCount = 0;

    public synchronized int checkActivity(int count) {
        startedActivityCount = startedActivityCount + count;
        return startedActivityCount;
    }
}
