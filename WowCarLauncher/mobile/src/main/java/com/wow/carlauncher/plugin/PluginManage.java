package com.wow.carlauncher.plugin;

import android.content.Context;
import android.util.Log;

import com.wow.carlauncher.plugin.music.MusicPlugin;
import com.wow.carlauncher.plugin.time.TimePlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 10124 on 2017/10/26.
 */

public class PluginManage {
    private static final String TAG = "PluginManage";

    public static final String MUSIC = "MUSIC";

    private static Map<String, IPlugin> plugins;

    public static void init(Context context) {
        plugins = new ConcurrentHashMap<>();
        plugins.put(MUSIC, new MusicPlugin(context));
    }

    public static MusicPlugin music() {
        Log.e(TAG, "MusicPlugin: " + plugins);
        return (MusicPlugin) plugins.get(MUSIC);
    }

    public static IPlugin getByName(String name) {
        return plugins.get(name);
    }
}
