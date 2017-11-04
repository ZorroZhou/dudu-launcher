package com.wow.carlauncher.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.R;
import com.wow.carlauncher.activity.Launcher2Activity;
import com.wow.carlauncher.popupWindow.LoadWin;
import com.wow.carlauncher.popupWindow.PopupWin;

/**
 * Created by 10124 on 2017/10/29.
 */

public class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {
    private LoadWin loadWin;
    private CarLauncherApplication application;

    public ActivityLifecycleListener(CarLauncherApplication application) {
        this.application = application;
        loadWin = new LoadWin(application);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity instanceof Launcher2Activity) {
            loadWin.show();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        PopupWin.self().checkShow(1);

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (activity instanceof Launcher2Activity) {
            loadWin.hide();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        PopupWin.self().checkShow(-1);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}