package com.wow.carlauncher.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.wow.carlauncher.view.popupWindow.PopupWin;

/**
 * Created by 10124 on 2017/10/29.
 */

public class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        PopupWin.self().checkShow(1);
    }

    @Override
    public void onActivityResumed(Activity activity) {
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