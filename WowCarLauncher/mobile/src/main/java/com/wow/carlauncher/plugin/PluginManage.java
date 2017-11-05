package com.wow.carlauncher.plugin;

import android.content.Context;
import android.util.Log;

import com.wow.carlauncher.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.plugin.controller.ControllerPlugin;
import com.wow.carlauncher.plugin.music.MusicPlugin;

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

    private static Map<String, IPlugin> plugins;

    public static void init(Context context) {
        plugins = new ConcurrentHashMap<>();
        plugins.put(MUSIC, new MusicPlugin(context));
        plugins.put(CONTROLLER, new ControllerPlugin(context));
        plugins.put(AMAPCAR, new AMapCarPlugin(context));
    }

    public static ControllerPlugin controller() {
        return (ControllerPlugin) plugins.get(CONTROLLER);
    }

    public static MusicPlugin music() {
        return (MusicPlugin) plugins.get(MUSIC);
    }

    public static AMapCarPlugin amapCar() {
        return (AMapCarPlugin) plugins.get(AMAPCAR);
    }

    public static IPlugin getByName(String name) {
        return plugins.get(name);
    }
}
