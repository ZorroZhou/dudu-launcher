package com.wow.carlauncher.plugin;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.dialog.InputDialog;
import com.wow.carlauncher.event.LauncherItemRefreshEvent;
import com.wow.carlauncher.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.plugin.console.ConsolePlugin;
import com.wow.carlauncher.plugin.ncmusic.NcMusicPlugin;
import com.wow.carlauncher.plugin.qqmusic.QQMusicPlugin;
import com.wow.carlauncher.plugin.qqcarmusic.QQMusicCarPlugin;
import com.wow.carlauncher.plugin.sysmusic.SystemMusicPlugin;

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

        Integer launcherPlugins1 = SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginTypeEnum.SYSMUSIC.getId());
        allUsePlugins.put(launcherPlugins1, createPlugin(PluginTypeEnum.getById(launcherPlugins1)));

        Integer launcherPlugins2 = SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginTypeEnum.AMAP.getId());
        allUsePlugins.put(launcherPlugins2, createPlugin(PluginTypeEnum.getById(launcherPlugins2)));

        Integer launcherPlugins3 = SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginTypeEnum.CONSOLE.getId());
        allUsePlugins.put(launcherPlugins3, createPlugin(PluginTypeEnum.getById(launcherPlugins3)));

        for (PluginTypeEnum pluginTypeEnum : ALL_PLUGINS) {
            List<String> apps = new ArrayList<>();
            String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_POPUP_PLUGIN_SHOW_APPS + pluginTypeEnum.getId());
            if (CommonUtil.isNotNull(selectapp)) {
                String[] apptemps = selectapp.split(";");
                for (String apptemp : apptemps) {
                    apps.add(apptemp.replace("[", "").replace("]", ""));
                }
            }
            pluginShowApp.put(pluginTypeEnum.getId(), apps);
            if (apps.size() > 0) {
                allUsePlugins.put(pluginTypeEnum.getId(), createPlugin(pluginTypeEnum));
            }
        }
        Log.e(TAG, "init: " + allUsePlugins);
    }

    public PluginTypeEnum getPopupPlugin(String app, int currentPlugin, boolean next) {
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
        return PluginTypeEnum.getById(pluginId);
    }

    public BasePlugin getPlugin(PluginTypeEnum p) {
        return allUsePlugins.get(p.getId());
    }

    public void setPopupPluginShowApps(PluginTypeEnum p, List<String> apps) {
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
    public void setLauncherPlugin(LauncherPluginEnum pluginEnum, PluginTypeEnum pluginTypeEnum) {
        PluginTypeEnum oldPluginTypeEnum = null;
        switch (pluginEnum) {
            case LAUNCHER_ITEM1: {
                oldPluginTypeEnum = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginTypeEnum.SYSMUSIC.getId()));
                SharedPreUtil.saveSharedPreInteger(SDATA_ITEM1_PLUGIN, pluginTypeEnum.getId());
                break;
            }
            case LAUNCHER_ITEM2: {
                oldPluginTypeEnum = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginTypeEnum.AMAP.getId()));
                SharedPreUtil.saveSharedPreInteger(SDATA_ITEM2_PLUGIN, pluginTypeEnum.getId());
                break;
            }
            case LAUNCHER_ITEM3: {
                oldPluginTypeEnum = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginTypeEnum.CONSOLE.getId()));
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

//        switch (pluginEnum) {
//            case LAUNCHER_ITEM1: {
//                PluginTypeEnum p = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginTypeEnum.SYSMUSIC.getId()));
//                if (!p.equals(pluginTypeEnum)) {
//                    //先检测弹出框是不是使用了这个插件，如果没有使用，则直接销毁
//                    allUsePlugins.get(launcherPlugins1).destroy();
//                    //从使用的库中移除这个插件
//                    allUsePlugins.remove(p.getId());
//                    BasePlugin plugin = allUsePlugins.get(pluginTypeEnum.getId());
//                    if (plugin == null) {
//                        plugin = createPlugin(pluginTypeEnum);
//                    }
//                    launcherPlugins1 = pluginTypeEnum.getId();
//                    allUsePlugins.put(pluginTypeEnum.getId(), plugin);
//
//                    SharedPreUtil.saveSharedPreInteger(SDATA_ITEM1_PLUGIN, pluginTypeEnum.getId());
//
//                    EventBus.getDefault().post(new LauncherItemRefreshEvent());
//                }
//                break;
//            }
//            case LAUNCHER_ITEM2: {
//                PluginTypeEnum p = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginTypeEnum.AMAP.getId()));
//                if (!p.equals(pluginTypeEnum)) {
//                    //先检测弹出框是不是使用了这个插件，如果没有使用，则直接销毁
//                    allUsePlugins.get(launcherPlugins2).destroy();
//                    //从使用的库中移除这个插件
//                    allUsePlugins.remove(p.getId());
//                    BasePlugin plugin = allUsePlugins.get(pluginTypeEnum.getId());
//                    if (plugin == null) {
//                        plugin = createPlugin(pluginTypeEnum);
//                    }
//                    launcherPlugins2 = pluginTypeEnum.getId();
//                    allUsePlugins.put(pluginTypeEnum.getId(), plugin);
//
//                    SharedPreUtil.saveSharedPreInteger(SDATA_ITEM2_PLUGIN, pluginTypeEnum.getId());
//
//                    EventBus.getDefault().post(new LauncherItemRefreshEvent());
//                }
//
//                break;
//            }
//            case LAUNCHER_ITEM3: {
//                PluginTypeEnum p = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginTypeEnum.CONSOLE.getId()));
//                if (!p.equals(pluginTypeEnum)) {
//                    //先检测弹出框是不是使用了这个插件，如果没有使用，则直接销毁
//                    allUsePlugins.get(launcherPlugins2).destroy();
//                    //从使用的库中移除这个插件
//                    allUsePlugins.remove(p.getId());
//                    BasePlugin plugin = allUsePlugins.get(pluginTypeEnum.getId());
//                    if (plugin == null) {
//                        plugin = createPlugin(pluginTypeEnum);
//                    }
//                    launcherPlugins3 = pluginTypeEnum.getId();;
//                    allUsePlugins.put(pluginTypeEnum.getId(), plugin);
//
//                    SharedPreUtil.saveSharedPreInteger(SDATA_ITEM3_PLUGIN, pluginTypeEnum.getId());
//
//                    EventBus.getDefault().post(new LauncherItemRefreshEvent());
//                }
//                break;
//            }
//        }
    }

    //获取主页的插件
    public BasePlugin getLauncherPlugin(LauncherPluginEnum pluginEnum) {
        switch (pluginEnum) {
            case LAUNCHER_ITEM1: {
                return allUsePlugins.get(SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginTypeEnum.SYSMUSIC.getId()));
            }
            case LAUNCHER_ITEM2: {
                return allUsePlugins.get(SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginTypeEnum.AMAP.getId()));
            }
            case LAUNCHER_ITEM3: {
                return allUsePlugins.get(SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginTypeEnum.CONSOLE.getId()));
            }
        }
        return null;
    }

    //创建一个插件
    private BasePlugin createPlugin(PluginTypeEnum pluginTypeEnum) {
        switch (pluginTypeEnum) {
            case SYSMUSIC: {
                return new SystemMusicPlugin(context, this);
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
            case QQMUSIC: {
                return new QQMusicPlugin(context, this);
            }
            case QQCARMUSIC: {
                return new QQMusicCarPlugin(context, this);
            }
        }
        return null;
    }

    private boolean isLauncherItemUse(PluginTypeEnum p) {
        boolean use = false;
        if (SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginTypeEnum.SYSMUSIC.getId()) == p.getId()) {
            use = true;
        }
        if (SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginTypeEnum.AMAP.getId()) == p.getId()) {
            use = true;
        }
        if (SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginTypeEnum.CONSOLE.getId()) == p.getId()) {
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
