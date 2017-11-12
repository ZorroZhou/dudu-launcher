package com.wow.carlauncher.plugin.music;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.plugin.music.controllers.NeteaseCloudMusicPlugin;
import com.wow.carlauncher.plugin.music.controllers.QQMusicCarPluginOld;
import com.wow.carlauncher.plugin.music.controllers.QQMusicPluginOld;
import com.wow.carlauncher.plugin.music.controllers.SystemMusicPlugin;

/**
 * Created by 10124 on 2017/10/26.
 */

public class MusicPlugin extends BasePlugin {

    private MusicController controller;

    public MusicPlugin(Context context, PluginManage pluginManage) {
        super(context, pluginManage);

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
                this.controller = new QQMusicCarPluginOld(context);
                break;
            }
            case QQMUSIC: {
                this.controller = new QQMusicPluginOld(context);
                break;
            }
            default:
                return;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
//        if (controller != null) {
//            controller.destroy();
//        }
    }

    @Override
    public ViewGroup initLauncherView() {
        LinearLayout launcherView = new LinearLayout(context);
        if (controller.getLauncherView() != null) {
            launcherView.addView(controller.getLauncherView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
        return launcherView;
    }

    @Override
    public ViewGroup initPopupView() {
        LinearLayout popupView = new LinearLayout(context);
        if (controller.getPopupView() != null) {
            popupView.addView(controller.getPopupView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
        return popupView;
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
                this.controller = new QQMusicCarPluginOld(context);
                break;
            }
            case QQMUSIC: {
                this.controller = new QQMusicPluginOld(context);
                break;
            }
            default:
                return;
        }
        if (popupView != null) {
            popupView.removeAllViews();
            if (controller.getPopupView() != null) {
                popupView.addView(controller.getPopupView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            }
        }

        if (launcherView != null) {
            launcherView.removeAllViews();
            if (controller.getLauncherView() != null) {
                launcherView.addView(controller.getLauncherView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            }
        }

    }
}
