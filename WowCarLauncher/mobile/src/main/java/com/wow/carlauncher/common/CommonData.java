package com.wow.carlauncher.common;

/**
 * Created by 10124 on 2017/10/29.
 */

public class CommonData {

    public static final int REQUEST_SELECT_APP_TO_DOCK1 = 10000;
    public static final int REQUEST_SELECT_APP_TO_DOCK2 = 10001;
    public static final int REQUEST_SELECT_APP_TO_DOCK3 = 10002;
    public static final int REQUEST_SELECT_APP_TO_DOCK4 = 10003;
    public static final int REQUEST_SELECT_APP_TO_DOCK5 = 10004;

    public static final int REQUEST_SELECT_NCM_WIDGET1 = 10005;
    public static final int REQUEST_SELECT_NCM_WIDGET2 = 10006;

    public static final int REQUEST_SELECT_QQMUSIC_WIDGET1 = 10007;
    public static final int REQUEST_SELECT_QQMUSIC_WIDGET2 = 10008;

    public static final String IDATA_PACKAGE_NAME = "PACKAGE_NAME";

    //dock的包名start
    public static final String SDATA_DOCK1_CLASS = "DOCK1_BEAN";
    public static final String SDATA_DOCK2_CLASS = "DOCK2_BEAN";
    public static final String SDATA_DOCK3_CLASS = "DOCK3_BEAN";
    public static final String SDATA_DOCK4_CLASS = "DOCK4_BEAN";
    public static final String SDATA_DOCK5_CLASS = "DOCK5_BEAN";
    //dock的包名end
    //悬浮框的X坐标
    public static final String SDATA_POPUP_WIN_X = "SDATA_POPUP_WIN_X";
    //悬浮框的Y坐标
    public static final String SDATA_POPUP_WIN_Y = "SDATA_POPUP_WIN_Y";
    //悬浮框当前的插件
    public static final String SDATA_POPUP_CURRENT_PLUGIN = "SDATA_POPUP_CURRENT_PLUGIN";

    //是否允许悬浮框展示
    public static final String SDATA_POPUP_ALLOW_SHOW = "SDATA_POPUP_ALLOW_SHOW";
    //悬浮框展示方式
    public static final String SDATA_POPUP_SHOW_TYPE = "SDATA_POPUP_SHOW_TYPE";
    //悬浮框展示的APP
    public static final String SDATA_POPUP_SHOW_APPS = "SDATA_POPUP_SHOW_APPS";

    //当前的音乐控制器
    public static final String SDATA_CURRENT_MUSIC_CONTROLLER = "SDATA_CURRENT_MUSIC_CONTROLLER";

    //隐藏的APP的包名，格式：[包名],[包名],[包名],[包名]
    public static final String SDATA_HIDE_APPS = "SDATA_HIDE_APPS";

    //网易云音乐插件，弹出界面的展示的组件
    public static final String SDATA_MUSIC_PLUGIN_NCM_POPUP = "SDATA_MUSIC_PLUGIN_NCM_POPUP";
    //网易云音乐插件，主页界面的展示的组件
    public static final String SDATA_MUSIC_PLUGIN_NCM_LANNCHER = "SDATA_MUSIC_PLUGIN_NCM_LANNCHER";

    //QQ音乐插件，弹出界面的展示的组件
    public static final String SDATA_MUSIC_PLUGIN_QQMUSIC_POPUP = "SDATA_MUSIC_PLUGIN_QQMUSIC_POPUP";
    //QQ音乐插件，主页界面的展示的组件
    public static final String SDATA_MUSIC_PLUGIN_QQMUSIC_LANNCHER = "SDATA_MUSIC_PLUGIN_QQMUSIC_LANNCHER";

    //时间插件打开的APP
    public static final String SDATA_TIME_PLUGIN_OPEN_APP = "SDATA_TIME_PLUGIN_OPEN_APP";

    public static final int APP_WIDGET_HOST_ID = 0x200;
}
