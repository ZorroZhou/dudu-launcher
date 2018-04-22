package com.wow.carlauncher.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.popupWindow.PopupWin;

/**
 * Created by 10124 on 2017/11/2.
 */

public class MainService extends Service {
    private final String TAG = "FlowWindowService";

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PopupWin.self().checkShow(0);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private CarLauncherApplication getApplicationEx() {
        return (CarLauncherApplication) getApplication();
    }
}
