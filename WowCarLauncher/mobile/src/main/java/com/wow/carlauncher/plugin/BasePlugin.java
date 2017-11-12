package com.wow.carlauncher.plugin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by 10124 on 2017/10/26.
 */

public abstract class BasePlugin {
    protected ViewGroup launcherView;
    protected ViewGroup popupView;
    protected PluginManage pluginManage;
    protected Context context;

    public PluginManage getPluginManage() {
        return pluginManage;
    }

    public Context getContext() {
        return context;
    }

    public BasePlugin(Context context, PluginManage pluginManage) {
        this.pluginManage = pluginManage;
        this.context = context;
    }

    public View getLauncherView() {
        if (launcherView == null) {
            launcherView = initLauncherView();
        }
        return launcherView;
    }

    public View getPopupView() {
        if (popupView == null) {
            popupView = initPopupView();
        }
        return popupView;
    }

    public void destroy() {
        if (launcherView != null && launcherView.getParent() != null) {
            ((ViewGroup) launcherView.getParent()).removeView(launcherView);
        }

        if (popupView != null && popupView.getParent() != null) {
            ((ViewGroup) popupView.getParent()).removeView(popupView);
        }
    }

    public abstract ViewGroup initLauncherView();

    public abstract ViewGroup initPopupView();
}
