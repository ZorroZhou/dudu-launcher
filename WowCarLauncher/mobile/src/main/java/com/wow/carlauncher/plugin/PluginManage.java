package com.wow.carlauncher.plugin;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.event.LauncherDockLabelShowChangeEvent;
import com.wow.carlauncher.event.LauncherItemRefreshEvent;
import com.wow.carlauncher.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.plugin.console.ConsolePlugin;
import com.wow.carlauncher.plugin.music.MusicPlugin;
import com.wow.carlauncher.plugin.ncmusic.NcMusicPlugin;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.wow.carlauncher.common.CommonData.*;

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

    private Map<String, BasePlugin> plugins;
    private Activity currentActivity;
    private Context context;

    private Map<Integer, BasePlugin> allUsePlugins;
    private BasePlugin launcherPlugins1;
    private BasePlugin launcherPlugins2;
    private BasePlugin launcherPlugins3;

    private PluginManage() {

    }

    public void init(Context context) {
        this.context = context;
        allUsePlugins = new ConcurrentHashMap<>();

        plugins = new ConcurrentHashMap<>();

        plugins.put(MUSIC, new MusicPlugin(context, this));
        plugins.put(CONSOLE, new ConsolePlugin(context, this));
        plugins.put(AMAPCAR, new AMapCarPlugin(context, this));

        Integer name1 = SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginTypeEnum.MUSIC.getId());
        launcherPlugins1 = createPluginById(name1);
        if (launcherPlugins1 == null) {
            launcherPlugins1 = createPluginById(PluginTypeEnum.MUSIC.getId());
        }
        allUsePlugins.put(name1, launcherPlugins1);

        Integer name2 = SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginTypeEnum.AMAP.getId());
        launcherPlugins2 = createPluginById(name2);
        if (launcherPlugins2 == null) {
            launcherPlugins2 = createPluginById(PluginTypeEnum.AMAP.getId());
        }
        allUsePlugins.put(name2, launcherPlugins2);

        Integer name3 = SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginTypeEnum.CONSOLE.getId());
        launcherPlugins3 = createPluginById(name3);
        if (launcherPlugins3 == null) {
            launcherPlugins3 = createPluginById(PluginTypeEnum.CONSOLE.getId());
        }
        allUsePlugins.put(name3, launcherPlugins3);

    }

    public void setLauncherPlugin(LauncherPluginEnum pluginEnum, PluginTypeEnum pluginTypeEnum) {
        switch (pluginEnum) {
            case LAUNCHER_ITEM1: {
                PluginTypeEnum p = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginTypeEnum.MUSIC.getId()));
                if (!p.equals(pluginTypeEnum)) {
                    //先检测弹出框是不是使用了这个插件，如果没有使用，则直接销毁
                    launcherPlugins1.destroy();
                    //从使用的库中移除这个插件
                    allUsePlugins.remove(p.getId());
                    BasePlugin plugin = allUsePlugins.get(pluginTypeEnum.getId());
                    if (plugin == null) {
                        plugin = createPluginById(pluginTypeEnum.getId());
                    }
                    launcherPlugins1 = plugin;
                    allUsePlugins.put(pluginTypeEnum.getId(), launcherPlugins1);

                    SharedPreUtil.saveSharedPreInteger(SDATA_ITEM1_PLUGIN, pluginTypeEnum.getId());

                    EventBus.getDefault().post(new LauncherItemRefreshEvent());
                }
                break;
            }
            case LAUNCHER_ITEM2: {
                PluginTypeEnum p = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginTypeEnum.AMAP.getId()));
                if (!p.equals(pluginTypeEnum)) {
                    //先检测弹出框是不是使用了这个插件，如果没有使用，则直接销毁
                    launcherPlugins2.destroy();
                    //从使用的库中移除这个插件
                    allUsePlugins.remove(p.getId());
                    BasePlugin plugin = allUsePlugins.get(pluginTypeEnum.getId());
                    if (plugin == null) {
                        plugin = createPluginById(pluginTypeEnum.getId());
                    }
                    launcherPlugins2 = plugin;
                    allUsePlugins.put(pluginTypeEnum.getId(), launcherPlugins2);

                    SharedPreUtil.saveSharedPreInteger(SDATA_ITEM2_PLUGIN, pluginTypeEnum.getId());

                    EventBus.getDefault().post(new LauncherItemRefreshEvent());
                }

                break;
            }
            case LAUNCHER_ITEM3: {
                PluginTypeEnum p = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginTypeEnum.CONSOLE.getId()));
                if (!p.equals(pluginTypeEnum)) {
                    //先检测弹出框是不是使用了这个插件，如果没有使用，则直接销毁
                    launcherPlugins3.destroy();
                    //从使用的库中移除这个插件
                    allUsePlugins.remove(p.getId());
                    BasePlugin plugin = allUsePlugins.get(pluginTypeEnum.getId());
                    if (plugin == null) {
                        plugin = createPluginById(pluginTypeEnum.getId());
                    }
                    launcherPlugins3 = plugin;
                    allUsePlugins.put(pluginTypeEnum.getId(), launcherPlugins3);

                    SharedPreUtil.saveSharedPreInteger(SDATA_ITEM3_PLUGIN, pluginTypeEnum.getId());

                    EventBus.getDefault().post(new LauncherItemRefreshEvent());
                }
                break;
            }
        }
    }

    public BasePlugin getLauncherPlugin(LauncherPluginEnum pluginEnum) {
        switch (pluginEnum) {
            case LAUNCHER_ITEM1: {
                return launcherPlugins1;
            }
            case LAUNCHER_ITEM2: {
                return launcherPlugins2;
            }
            case LAUNCHER_ITEM3: {
                return launcherPlugins3;
            }
        }
        return null;
    }


    private BasePlugin createPluginById(Integer id) {
        switch (PluginTypeEnum.getById(id)) {
            case MUSIC: {
                return new MusicPlugin(context, this);
            }
            case CONSOLE: {
                return new ConsolePlugin(context, this);
            }
            case AMAP: {
                return new AMapCarPlugin(context, this);
            }
            case NCMUSIC: {
                return new NcMusicPlugin(context, this);
            }
        }
        return null;
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

    public BasePlugin getByName(String name) {
        return plugins.get(name);
    }

    public void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }
}
