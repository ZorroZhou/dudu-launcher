package com.wow.carlauncher.plugin;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.wow.carlauncher.plugin.gps.GpsPlugin;
import com.wow.carlauncher.plugin.music.JidouMusicPlugin;
import com.wow.carlauncher.plugin.music.NcMusicPlugin;
import com.wow.carlauncher.plugin.music.NwdMusicPlugin;
import com.wow.carlauncher.plugin.music.PowerAmpCarPlugin;
import com.wow.carlauncher.plugin.music.QQMusicCarPlugin;
import com.wow.carlauncher.plugin.music.QQMusicPlugin;
import com.wow.carlauncher.plugin.music.SystemMusicPlugin;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.event.LauncherItemRefreshEvent;
import com.wow.carlauncher.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.plugin.console.ConsolePlugin;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.wow.carlauncher.common.CommonData.*;

public class PluginManage {
    private static PluginManage self;

    public static PluginManage self() {
        if (self == null) {
            self = new PluginManage();
        }
        return self;
    }

    private Activity currentActivity;
    private Context context;

    private Map<Integer, BasePlugin> allUsePlugins;

    private Map<Integer, List<String>> pluginShowApp;

    private PluginManage() {

    }

    public void init(Context context) {
        this.context = context;
        allUsePlugins = new ConcurrentHashMap<>();
        pluginShowApp = new ConcurrentHashMap<>();

        Integer launcherPlugins1 = SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginEnum.SYSMUSIC.getId());
        allUsePlugins.put(launcherPlugins1, createPlugin(PluginEnum.getById(launcherPlugins1)));

        Integer launcherPlugins2 = SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginEnum.AMAP.getId());
        allUsePlugins.put(launcherPlugins2, createPlugin(PluginEnum.getById(launcherPlugins2)));

        Integer launcherPlugins3 = SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginEnum.CONSOLE.getId());
        allUsePlugins.put(launcherPlugins3, createPlugin(PluginEnum.getById(launcherPlugins3)));

        for (PluginEnum pluginTypeEnum : ALL_PLUGINS) {
            List<String> apps = new ArrayList<>();
            String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_POPUP_PLUGIN_SHOW_APPS + pluginTypeEnum.getId());
            if (CommonUtil.isNotNull(selectapp)) {
                String[] apptemps = selectapp.split(";");
                for (String apptemp : apptemps) {
                    apps.add(apptemp.replace("[", "").replace("]", ""));
                }
            }
            pluginShowApp.put(pluginTypeEnum.getId(), apps);
            if (apps.size() > 0 && !allUsePlugins.containsKey(pluginTypeEnum.getId())) {
                allUsePlugins.put(pluginTypeEnum.getId(), createPlugin(pluginTypeEnum));
            }
        }
        Log.e(TAG, "init: " + allUsePlugins);
    }

    public PluginEnum getPopupPlugin(String app, int currentPlugin, boolean next) {
        List<Integer> pluginIds = new ArrayList<>();
        for (Integer key : pluginShowApp.keySet()) {
            if (pluginShowApp.get(key).contains(app)) {
                pluginIds.add(key);
            }
        }
        if (pluginIds.size() == 0) {
            return null;
        }
        int index = pluginIds.indexOf(currentPlugin);
        if (next) {
            index++;
        }
        if (index >= pluginIds.size() || index < 0) {
            return null;
        }
        Integer pluginId = pluginIds.get(index);
        return PluginEnum.getById(pluginId);
    }

    public BasePlugin getPlugin(PluginEnum p) {
        return allUsePlugins.get(p.getId());
    }

    public void setPopupPluginShowApps(PluginEnum p, List<String> apps) {
        List<String> yapps = pluginShowApp.get(p.getId());
        yapps.clear();
        yapps.addAll(apps);

        boolean use = false;
        if (isLauncherItemUse(p)) {
            use = true;
        }
        if (yapps.size() > 0) {
            use = true;
        }
        if (use) {
            BasePlugin plugin = allUsePlugins.get(p.getId());
            if (plugin == null) {
                plugin = createPlugin(p);
                allUsePlugins.put(p.getId(), plugin);
            }
        } else {
            BasePlugin plugin = allUsePlugins.get(p.getId());
            if (plugin != null) {
                allUsePlugins.remove(p.getId());
                plugin.destroy();
            }
        }
        Log.e(TAG, "setPopupPluginShowApps: " + allUsePlugins);
    }


    //设置主页的插件
    public void setLauncherPlugin(LauncherPluginEnum pluginEnum, PluginEnum pluginTypeEnum) {
        PluginEnum oldPluginTypeEnum = null;
        switch (pluginEnum) {
            case LAUNCHER_ITEM1: {
                oldPluginTypeEnum = PluginEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginEnum.SYSMUSIC.getId()));
                SharedPreUtil.saveSharedPreInteger(SDATA_ITEM1_PLUGIN, pluginTypeEnum.getId());
                break;
            }
            case LAUNCHER_ITEM2: {
                oldPluginTypeEnum = PluginEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginEnum.AMAP.getId()));
                SharedPreUtil.saveSharedPreInteger(SDATA_ITEM2_PLUGIN, pluginTypeEnum.getId());
                break;
            }
            case LAUNCHER_ITEM3: {
                oldPluginTypeEnum = PluginEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginEnum.CONSOLE.getId()));
                SharedPreUtil.saveSharedPreInteger(SDATA_ITEM3_PLUGIN, pluginTypeEnum.getId());
                break;
            }
        }
        if (oldPluginTypeEnum == null || pluginTypeEnum.equals(oldPluginTypeEnum)) {
            return;
        }
        if (pluginShowApp.get(oldPluginTypeEnum.getId()).size() == 0) {
            allUsePlugins.get(oldPluginTypeEnum.getId()).destroy();
            allUsePlugins.remove(oldPluginTypeEnum.getId());
        }
        BasePlugin plugin = allUsePlugins.get(pluginTypeEnum.getId());
        if (plugin == null) {
            plugin = createPlugin(pluginTypeEnum);
            allUsePlugins.put(pluginTypeEnum.getId(), plugin);
        }
        EventBus.getDefault().post(new LauncherItemRefreshEvent());
    }

    //获取主页的插件
    public BasePlugin getLauncherPlugin(LauncherPluginEnum pluginEnum) {
        switch (pluginEnum) {
            case LAUNCHER_ITEM1: {
                return allUsePlugins.get(SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginEnum.SYSMUSIC.getId()));
            }
            case LAUNCHER_ITEM2: {
                return allUsePlugins.get(SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginEnum.AMAP.getId()));
            }
            case LAUNCHER_ITEM3: {
                return allUsePlugins.get(SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginEnum.CONSOLE.getId()));
            }
        }
        return null;
    }

    //创建一个插件
    private BasePlugin createPlugin(PluginEnum pluginEnum) {
        switch (pluginEnum) {
            case SYSMUSIC: {
                return new SystemMusicPlugin(context, this);
            }
            case CONSOLE: {
                return new GpsPlugin(context, this);
            }
            case AMAP: {
                return new AMapCarPlugin(context, this);
            }
            case NCMUSIC: {
                return new NcMusicPlugin(context, this);
            }
            case QQMUSIC: {
                return new QQMusicPlugin(context, this);
            }
            case QQCARMUSIC: {
                return new QQMusicCarPlugin(context, this);
            }
            case JIDOUMUSIC: {
                return new JidouMusicPlugin(context, this);
            }
            case POWERAMPMUSIC: {
                return new PowerAmpCarPlugin(context, this);
            }
            case NWDMUSIC: {
                return new NwdMusicPlugin(context, this);
            }
        }
        return null;
    }

    private boolean isLauncherItemUse(PluginEnum p) {
        boolean use = false;
        if (SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginEnum.SYSMUSIC.getId()) == p.getId()) {
            use = true;
        }
        if (SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginEnum.AMAP.getId()) == p.getId()) {
            use = true;
        }
        if (SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginEnum.CONSOLE.getId()) == p.getId()) {
            use = true;
        }
        return use;
    }

    public void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }
}
