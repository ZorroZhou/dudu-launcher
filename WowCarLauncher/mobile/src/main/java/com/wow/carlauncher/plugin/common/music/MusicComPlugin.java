package com.wow.carlauncher.plugin.common.music;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;

import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.PluginManage;

public abstract class MusicComPlugin extends BasePlugin implements MusicController, MusicView {
    private MusicView launcherView;
    private MusicView popupView;

    public MusicComPlugin(Context context, PluginManage pluginManage) {
        super(context, pluginManage);
    }

    public abstract String getName();

    @Override
    public ViewGroup initLauncherView() {
        MusicLauncherView launcherView = new MusicLauncherView(context, this);
        launcherView.setName(getName());
        this.launcherView = launcherView;
        return launcherView;
    }

    @Override
    public ViewGroup initPopupView() {
        MusicPopupView view = new MusicPopupView(context, this);
        this.popupView = view;
        return view;
    }

    public void refreshInfo(String title, String artist) {
        if (launcherView != null) {
            launcherView.refreshInfo(title, artist);
        }
        if (popupView != null) {
            popupView.refreshInfo(title, artist);
        }
    }

    public void refreshCover(Bitmap cover) {
        if (launcherView != null) {
            launcherView.refreshCover(cover);
        }
        if (popupView != null) {
            popupView.refreshCover(cover);
        }
    }

    public void refreshProgress(final int curr_time, final int total_tim) {
        if (launcherView != null) {
            launcherView.refreshProgress(curr_time, total_tim);
        }
        if (popupView != null) {
            popupView.refreshProgress(curr_time, total_tim);
        }
    }

    public void refreshState(boolean run) {
        if (launcherView != null) {
            launcherView.refreshState(run);
        }
        if (popupView != null) {
            popupView.refreshState(run);
        }
    }
}
