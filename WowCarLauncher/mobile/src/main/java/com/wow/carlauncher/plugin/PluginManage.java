package com.wow.carlauncher.plugin;

import android.app.Activity;
import android.content.Context;

import com.wow.carlauncher.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.plugin.console.ConsolePlugin;
import com.wow.carlauncher.plugin.music.MusicPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 10124 on 2017/10/26.
 */

public class PluginManage {
    private static final String TAG = "PluginManage";

    public static final String MUSIC = "MUSIC";
    public static final String CONSOLE = "CONSOLE";
    public static final String AMAPCAR = "AMAPCAR";
    public static final String NC_MUSIC = "NC_MUSIC";

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
        plugins.put(CONSOLE, new ConsolePlugin(context, this));
        plugins.put(AMAPCAR, new AMapCarPlugin(context, this));
    }

    public ConsolePlugin controller() {
        return (ConsolePlugin) plugins.get(CONSOLE);
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
