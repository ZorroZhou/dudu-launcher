package com.wow.carlauncher.common;

import com.wow.carlauncher.plugin.PluginTypeEnum;

public class CommonData {
    public static final PluginTypeEnum[] ALL_PLUGINS = {PluginTypeEnum.SYSMUSIC, PluginTypeEnum.NCMUSIC, PluginTypeEnum.CONSOLE, PluginTypeEnum.AMAP, PluginTypeEnum.QQMUSIC, PluginTypeEnum.QQCARMUSIC};

    public static final String TAG = "WOW_CAR";

    public static final int REQUEST_SELECT_APP_TO_DOCK1 = 10000;
    public static final int REQUEST_SELECT_APP_TO_DOCK2 = 10001;
    public static final int REQUEST_SELECT_APP_TO_DOCK3 = 10002;
    public static final int REQUEST_SELECT_APP_TO_DOCK4 = 10003;
    public static final int REQUEST_SELECT_APP_TO_DOCK5 = 10004;
    public static final int REQUEST_SELECT_APP_TO_DOCK6 = 10005;

    public static final int REQUEST_SELECT_NCM_WIDGET1 = 10006;
    public static final int REQUEST_SELECT_NCM_WIDGET2 = 10007;

    public static final int REQUEST_SELECT_QQMUSIC_WIDGET1 = 10008;
    public static final int REQUEST_SELECT_QQMUSIC_WIDGET2 = 10009;

    public static final String IDATA_PACKAGE_NAME = "PACKAGE_NAME";

    //首页的插件
    public static final String SDATA_ITEM1_PLUGIN = "SDATA_ITEM1_PLUGIN";
    public static final String SDATA_ITEM2_PLUGIN = "SDATA_ITEM2_PLUGIN";
    public static final String SDATA_ITEM3_PLUGIN = "SDATA_ITEM3_PLUGIN";

    //dock的包名start
    public static final String SDATA_DOCK1_CLASS = "DOCK1_BEAN";
    public static final String SDATA_DOCK2_CLASS = "DOCK2_BEAN";
    public static final String SDATA_DOCK3_CLASS = "DOCK3_BEAN";
    public static final String SDATA_DOCK4_CLASS = "DOCK4_BEAN";
    public static final String SDATA_DOCK5_CLASS = "DOCK5_BEAN";
    public static final String SDATA_DOCK6_CLASS = "DOCK6_BEAN";
    //dock的包名end
    //悬浮框的X坐标
    public static final String SDATA_POPUP_WIN_X = "SDATA_POPUP_WIN_X";
    //悬浮框的Y坐标
    public static final String SDATA_POPUP_WIN_Y = "SDATA_POPUP_WIN_Y";
    //悬浮框展示的APP
    public static final String SDATA_POPUP_SHOW_APPS = "SDATA_POPUP_SHOW_APPS";
    public static final String SDATA_POPUP_SHOW_TYPE = "SDATA_POPUP_SHOW_TYPE";
    //悬浮框当前的插件
    public static final String SDATA_POPUP_CURRENT_PLUGIN = "SDATA_POPUP_CURRENT_PLUGIN_";
    //是否允许悬浮框展示
    public static final String SDATA_LAUNCHER_DOCK_LABEL_SHOW = "SDATA_LAUNCHER_DOCK_LABEL_SHOW";
    //是否允许悬浮框展示
    public static final String SDATA_POPUP_ALLOW_SHOW = "SDATA_POPUP_ALLOW_SHOW";
    //悬浮框展示的APP
    public static final String SDATA_POPUP_PLUGIN_SHOW_APPS = "SDATA_POPUP_PLUGIN_SHOW_APPS_";

    //当前的音乐控制器
    public static final String SDATA_CURRENT_MUSIC_CONTROLLER = "SDATA_CURRENT_MUSIC_CONTROLLER";

    //隐藏的APP的包名，格式：[包名],[包名],[包名],[包名]
    public static final String SDATA_HIDE_APPS = "SDATA_HIDE_APPS";

    //网易云音乐插件，弹出界面的展示的组件
    public static final String SDATA_MUSIC_PLUGIN_NCM_WIDGET1 = "SDATA_MUSIC_PLUGIN_NCM_WIDGET1";
    //网易云音乐插件，主页界面的展示的组件
    public static final String SDATA_MUSIC_PLUGIN_NCM_WIDGET2 = "SDATA_MUSIC_PLUGIN_NCM_WIDGET2";

    //QQ音乐插件，弹出界面的展示的组件
    public static final String SDATA_MUSIC_PLUGIN_QQMUSIC_WIDGET1 = "SDATA_MUSIC_PLUGIN_QQMUSIC_WIDGET1";
    //QQ音乐插件，主页界面的展示的组件
    public static final String SDATA_MUSIC_PLUGIN_QQMUSIC_WIDGET2 = "SDATA_MUSIC_PLUGIN_QQMUSIC_WIDGET2";

    //时间插件打开的APP
    public static final String SDATA_TIME_PLUGIN_OPEN_APP = "SDATA_TIME_PLUGIN_OPEN_APP";

    //时间插件打开的APP
    public static final String SDATA_CONSOLE_MARK = "SDATA_CONSOLE_MARK";

    public static final int APP_WIDGET_HOST_ID = 0x200;
}
