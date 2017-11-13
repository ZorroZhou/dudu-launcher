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
    private BasePlugin launcherPlugins1;
    private BasePlugin launcherPlugins2;
    private BasePlugin launcherPlugins3;


    private Map<Integer, List<String>> pluginShowApp;

    private PluginManage() {

    }

    public void init(Context context) {
        this.context = context;
        allUsePlugins = new ConcurrentHashMap<>();
        pluginShowApp = new ConcurrentHashMap<>();

        Integer id1 = SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginTypeEnum.SYSMUSIC.getId());
        launcherPlugins1 = createPlugin(PluginTypeEnum.getById(id1));
        if (launcherPlugins1 == null) {
            launcherPlugins1 = createPlugin(PluginTypeEnum.SYSMUSIC);
        }
        allUsePlugins.put(id1, launcherPlugins1);

        Integer id2 = SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginTypeEnum.AMAP.getId());
        launcherPlugins2 = createPlugin(PluginTypeEnum.getById(id2));
        if (launcherPlugins2 == null) {
            launcherPlugins2 = createPlugin(PluginTypeEnum.AMAP);
        }
        allUsePlugins.put(id2, launcherPlugins2);

        Integer id3 = SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginTypeEnum.CONSOLE.getId());
        launcherPlugins3 = createPlugin(PluginTypeEnum.getById(id3));
        if (launcherPlugins3 == null) {
            launcherPlugins3 = createPlugin(PluginTypeEnum.CONSOLE);
        }
        allUsePlugins.put(id3, launcherPlugins3);

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
        long t1 = System.currentTimeMillis();
        Log.e(TAG, "getPopupPlugin-0: " + app);
        List<Integer> pluginIds = new ArrayList<>();
        for (Integer key : pluginShowApp.keySet()) {
            Log.e(TAG, "getPopupPlugin: " + app);
            Log.e(TAG, "getPopupPlugin: " + pluginShowApp.get(key));
            if (pluginShowApp.get(key).contains(app)) {
                pluginIds.add(key);
            }
        }
        Log.e(TAG, "getPopupPlugin0: " + pluginIds);
        if (pluginIds.size() == 0) {
            return null;
        }
        int index = pluginIds.indexOf(currentPlugin);
        Log.e(TAG, "getPopupPlugin1: " + index);
        if (next) {
            index++;
        }
        Log.e(TAG, "getPopupPlugin2: " + index);
        if (index >= pluginIds.size() || index < 0) {
            return null;
        }
        Log.e(TAG, "getPopupPlugin3: " + index);
        Integer pluginId = pluginIds.get(index);
        Log.e(TAG, "getPopupPlugin4: " + (System.currentTimeMillis() - t1));
        return PluginTypeEnum.getById(pluginId);
    }

    public BasePlugin getPlugin(PluginTypeEnum p) {
        Log.e(TAG, "getPlugin: " + p);
        Log.e(TAG, "getPlugin: " + allUsePlugins);
        return allUsePlugins.get(p.getId());
    }


    //设置主页的插件
    public void setLauncherPlugin(LauncherPluginEnum pluginEnum, PluginTypeEnum pluginTypeEnum) {
        switch (pluginEnum) {
            case LAUNCHER_ITEM1: {
                PluginTypeEnum p = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginTypeEnum.SYSMUSIC.getId()));
                if (!p.equals(pluginTypeEnum)) {
                    //先检测弹出框是不是使用了这个插件，如果没有使用，则直接销毁
                    launcherPlugins1.destroy();
                    //从使用的库中移除这个插件
                    allUsePlugins.remove(p.getId());
                    BasePlugin plugin = allUsePlugins.get(pluginTypeEnum.getId());
                    if (plugin == null) {
                        plugin = createPlugin(pluginTypeEnum);
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
                        plugin = createPlugin(pluginTypeEnum);
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
                        plugin = createPlugin(pluginTypeEnum);
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

    //获取主页的插件
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

    public void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }
}
