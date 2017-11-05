package com.wow.carlauncher.plugin.music;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.IPlugin;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.plugin.PopupViewProportion;
import com.wow.carlauncher.plugin.music.controllers.NeteaseCloudMusicPlugin;
import com.wow.carlauncher.plugin.music.controllers.QQMusicCarPlugin;
import com.wow.carlauncher.plugin.music.controllers.QQMusicPlugin;
import com.wow.carlauncher.plugin.music.controllers.SystemMusicPlugin;

/**
 * Created by 10124 on 2017/10/26.
 */

public class MusicPlugin implements IPlugin {
    private PluginManage pluginManage;

    private Context context;

    private MusicController controller;

    private LinearLayout launcherView;
    private LinearLayout popupView;

    public MusicPlugin(Context context) {
        this.context = context;

        MusicControllerEnum type = MusicControllerEnum.valueOfId(SharedPreUtil.getSharedPreInteger(CommonData.SDATA_CURRENT_MUSIC_CONTROLLER, MusicControllerEnum.SYSTEM.getId()));
        switch (type) {
            case SYSTEM: {
                this.controller = new SystemMusicPlugin(context);
                break;
            }
            case NETEASECLOUD: {
                this.controller = new NeteaseCloudMusicPlugin(context);
                break;
            }
            case QQMUSICCAR: {
                this.controller = new QQMusicCarPlugin(context);
                break;
            }
            case QQMUSIC: {
                this.controller = new QQMusicPlugin(context);
                break;
            }
            default:
                return;
        }
    }

    @Override
    public void destroy() {
        this.context = null;
        if (controller != null) {
            controller.destroy();
        }
    }

    @Override
    public View getLauncherView() {
        if (launcherView == null) {
            launcherView = new LinearLayout(context);
        }
        launcherView.removeAllViews();
        if (controller.getPopupView() != null) {
            launcherView.addView(controller.getLauncherView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
        return launcherView;
    }

    @Override
    public View getPopupView() {
        if (popupView == null) {
            popupView = new LinearLayout(context);
        }
        popupView.removeAllViews();
        if (controller.getPopupView() != null) {
            popupView.addView(controller.getPopupView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
        return popupView;
    }

    public void setPluginManage(PluginManage pluginManage) {
        this.pluginManage = pluginManage;
    }

    public void selectMusicController(MusicControllerEnum type) {
        if (controller != null) {
            controller.destroy();
        }
        switch (type) {
            case SYSTEM: {
                this.controller = new SystemMusicPlugin(context);
                break;
            }
            case NETEASECLOUD: {
                this.controller = new NeteaseCloudMusicPlugin(context);
                break;
            }
            case QQMUSICCAR: {
                this.controller = new QQMusicCarPlugin(context);
                break;
            }
            case QQMUSIC: {
                this.controller = new QQMusicPlugin(context);
                break;
            }
            default:
                return;
        }
        if (popupView != null) {
            popupView.removeAllViews();
            popupView.addView(controller.getPopupView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }

        if (launcherView != null) {
            launcherView.removeAllViews();
            launcherView.addView(controller.getLauncherView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }

    }
}
