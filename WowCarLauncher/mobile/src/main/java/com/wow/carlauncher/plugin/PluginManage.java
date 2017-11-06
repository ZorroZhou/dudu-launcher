package com.wow.carlauncher.plugin;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.wow.carlauncher.activity.LauncherActivity;
import com.wow.carlauncher.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.plugin.controller.ControllerPlugin;
import com.wow.carlauncher.plugin.music.MusicPlugin;
import com.wow.carlauncher.popupWindow.PopupWin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 10124 on 2017/10/26.
 */

public class PluginManage {
    private static final String TAG = "PluginManage";

    public static final String MUSIC = "MUSIC";
    public static final String CONTROLLER = "CONTROLLER";
    public static final String AMAPCAR = "AMAPCAR";

    private static PluginManage self;

    public static PluginManage self() {
        if (self == null) {
            self = new PluginManage();
        }
        return self;
    }

    private Map<String, IPlugin> plugins;
    private Activity currentActivity;

    private PluginManage() {

    }

    public void init(Context context) {
        plugins = new ConcurrentHashMap<>();
        plugins.put(MUSIC, new MusicPlugin(context, this));
        plugins.put(CONTROLLER, new ControllerPlugin(context, this));
        plugins.put(AMAPCAR, new AMapCarPlugin(context, this));
    }

    public ControllerPlugin controller() {
        return (ControllerPlugin) plugins.get(CONTROLLER);
    }

    public MusicPlugin music() {
        return (MusicPlugin) plugins.get(MUSIC);
    }

    public AMapCarPlugin amapCar() {
        return (AMapCarPlugin) plugins.get(AMAPCAR);
    }

    public IPlugin getByName(String name) {
        return plugins.get(name);
    }

    public void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }
}
