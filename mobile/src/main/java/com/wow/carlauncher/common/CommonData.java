package com.wow.carlauncher.common;

import com.wow.carlauncher.ex.manage.skin.SkinModel;
import com.wow.carlauncher.ex.plugin.console.ConsoleProtoclEnum;
import com.wow.carlauncher.ex.plugin.fk.FangkongProtocolEnum;
import com.wow.carlauncher.ex.plugin.music.MusicControllerEnum;
import com.wow.carlauncher.ex.plugin.obd.ObdProtocolEnum;
import com.wow.carlauncher.view.activity.driving.AutoDrivingEnum;
import com.wow.carlauncher.view.activity.driving.DrivingViewEnum;
import com.wow.carlauncher.view.activity.launcher.ItemEnum;
import com.wow.carlauncher.view.activity.launcher.ItemTransformer;
import com.wow.carlauncher.view.activity.launcher.LayoutEnum;

public class CommonData {
    public static final String TAG = "WOW_CAR";

    public static final SkinModel[] SKIN_MODEL = {
            SkinModel.BAISE,
            SkinModel.DENGGUANG,
            SkinModel.SHIJIAN
    };

    //所有的桌面插件
    public static final AutoDrivingEnum[] AUTO_DRIVING_TYPES = {
            AutoDrivingEnum.TIME,
            AutoDrivingEnum.REV
    };
    //所有的桌面插件
    public static final LayoutEnum[] LAUNCHER_LAYOUTS = {
            LayoutEnum.AUTO,
            LayoutEnum.LAYOUT1,
            LayoutEnum.LAYOUT2
    };
    //所有的桌面插件
    public static final ItemTransformer[] LAUNCHER_ITEMS_TRANS = {
            ItemTransformer.None,
            ItemTransformer.BackgroundToForeground,
            ItemTransformer.Accordion,
            ItemTransformer.CubeIn,
            ItemTransformer.CubeOut,
            ItemTransformer.DepthPage,
            ItemTransformer.Drawer,
            ItemTransformer.FlipHorizontal,
            ItemTransformer.FlipVertical,
            ItemTransformer.ForegroundToBackground,
            ItemTransformer.RotateDown,
            ItemTransformer.RotateUp,
            ItemTransformer.ScaleInOut,
            ItemTransformer.Stack,
            ItemTransformer.Tablet,
            ItemTransformer.ZoomIn,
            ItemTransformer.ZoomOutSlide,
            ItemTransformer.ZoomOut,
    };

    //所有的桌面插件
    public static final ItemEnum[] LAUNCHER_ITEMS = {
            ItemEnum.AMAP,
            ItemEnum.MUSIC,
            ItemEnum.OBD,
            ItemEnum.TAIYA,
            ItemEnum.TIME,
            ItemEnum.WEATHER
    };
    //所有OBD控制器
    public static final ObdProtocolEnum[] OBD_CONTROLLER = {
            ObdProtocolEnum.YJ_TYB,
            ObdProtocolEnum.YJ_PTB};
    //所有的音乐控制器
    public static final MusicControllerEnum[] MUSIC_CONTROLLER = {
            MusicControllerEnum.SYSMUSIC,
            MusicControllerEnum.QQCARMUSIC,
            MusicControllerEnum.JIDOUMUSIC,
            MusicControllerEnum.NWDMUSIC,
            MusicControllerEnum.ZXMUSIC,
            MusicControllerEnum.KUWOMUSIC};

    public static final DrivingViewEnum[] DRIVING_VIEW = {
            DrivingViewEnum.BLACK,
            DrivingViewEnum.BLUE};

    public static final FangkongProtocolEnum[] FANGKONG_CONTROLLER = {FangkongProtocolEnum.YLFK};

    public static final ConsoleProtoclEnum[] CONSOLES_PROTOCL = {ConsoleProtoclEnum.SYSTEM, ConsoleProtoclEnum.NWD};

    public final static long MINUTE_MILL = 60 * 1000;
    public final static long HOUR_MILL = 60 * MINUTE_MILL;
    public final static long DAY_MILL = 24 * HOUR_MILL;

    public static final int APP_WIDGET_HOST_ID = 0x200;

    public static final String APP_WIDGET_AMAP_PLUGIN = "APP_WIDGET_AMAP_PLUGIN";

    public static final String[] POPUP_SIZE = {"小", "中", "大"};

    public static final String PACKAGE_NAME = "com.wow.carlauncher";

    public static final String LOGIN_USER_ID = "LOGIN_USER_ID";
    public static final String LOGIN_USER_INFO = "LOGIN_USER_INFO";

    public static final String SDATA_APP_SKIN = "SDATA_APP_SKIN";

    public static final String SDATA_APP_SKIN_DAY = "SDATA_APP_SKIN_DAY";
    public static final String SDATA_APP_SKIN_NIGHT = "SDATA_APP_SKIN_NIGHT";

    //
    public static final int REQUEST_SELECT_AMAP_PLUGIN = 10004;

    //-------------------------------Intent传输数据时使用的mark-startWakeUp-----------------------------------
    //-------------------------------Intent传输数据时使用的mark-end-----------------------------------

    //-------------------------------本地缓存使用的mark-startWakeUp-----------------------------------
    //音乐播放器的控制器
    public static final String SDATA_MUSIC_CONTROLLER = "SDATA_MUSIC_CONTROLLER";

    //-------------------------------方控设置相关-startWakeUp-----------------------------------
    //方控的控制器
    public static final String SDATA_FANGKONG_CONTROLLER = "SDATA_FANGKONG_CONTROLLER";
    //方控的名称
    public static final String SDATA_FANGKONG_NAME = "SDATA_FANGKONG_NAME";
    //方控的地址
    public static final String SDATA_FANGKONG_ADDRESS = "SDATA_FANGKONG_ADDRESS";
    //-------------------------------方控设置相关-end-----------------------------------

    //-------------------------------OBD设置相关-startWakeUp-----------------------------------
    //OBD的控制器
    public static final String SDATA_OBD_CONTROLLER = "SDATA_OBD_CONTROLLER";
    //OBD的名称
    public static final String SDATA_OBD_NAME = "SDATA_OBD_NAME";
    //OBD的地址
    public static final String SDATA_OBD_ADDRESS = "SDATA_OBD_ADDRESS";
    //-------------------------------OBD设置相关-end-----------------------------------
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
    //悬浮框展示的APP
    public static final String SDATA_POPUP_SHOW_APPS = "SDATA_POPUP_SHOW_APPS";
    //悬浮框是否全局展示
    public static final String SDATA_POPUP_SHOW_TYPE = "SDATA_POPUP_SHOW_TYPE";
    //悬浮框当前的插件
    public static final String SDATA_POPUP_CURRENT_PLUGIN = "SDATA_POPUP_CURRENT_PLUGIN_";
    //是否允许悬浮框展示
    public static final String SDATA_LAUNCHER_DOCK_LABEL_SHOW = "SDATA_LAUNCHER_DOCK_LABEL_SHOW";
    //是否允许悬浮框展示
    public static final String SDATA_POPUP_ALLOW_SHOW = "SDATA_POPUP_ALLOW_SHOW";
    //悬浮框是否覆盖状态栏
    public static final String SDATA_POPUP_FULL_SCREEN = "SDATA_POPUP_FULL_SCREEN";
    //悬浮框尺寸
    public static final String SDATA_POPUP_SIZE = "SDATA_POPUP_SIZE";
    //隐藏的APP的包名，格式：[包名],[包名],[包名],[包名]
    public static final String SDATA_HIDE_APPS = "SDATA_HIDE_APPS";
    //时间插件打开的APP
    public static final String SDATA_TIME_PLUGIN_OPEN_APP = "SDATA_TIME_PLUGIN_OPEN_APP";
    //控制接口实现类型,系统还是NWD
    public static final String SDATA_CONSOLE_MARK = "SDATA_CONSOLE_MARK";
    //天气所属的城市
    public static final String SDATA_WEATHER_DISTRICT = "SDATA_WEATHER_DISTRICT";

    public static final String SDATA_WEATHER_SHI = "SDATA_WEATHER_SHI";
    //是否接受开发中的APP包
    public static final String SDATA_ALLOW_DEBUG_APP = "SDATA_ALLOW_DEBUG_APP";
    //应用包名标记使用的间隔
    public static final String CONSTANT_APP_PACKAGE_SEPARATE = ":";
    //是否自动启动驾驶界面
    public static final String SDATA_TRIP_AUTO_OPEN_DRIVING = "SDATA_TRIP_AUTO_OPEN_DRIVING";
    //以上默认值
    public static final boolean SDATA_TRIP_AUTO_OPEN_DRIVING_DF = true;

    //APP启动后自动任务相关
    public static final String SDATA_APP_AUTO_OPEN_USE = "SDATA_APP_AUTO_OPEN_USE";
    public static final String SDATA_APP_AUTO_OPEN_BACK = "SDATA_APP_AUTO_OPEN_BACK";
    public static final int SDATA_APP_AUTO_OPEN_BACK_DF = 5;
    //第一个打开的APP
    public static final String SDATA_APP_AUTO_OPEN1 = "SDATA_APP_AUTO_OPEN1";
    public static final String SDATA_APP_AUTO_OPEN2 = "SDATA_APP_AUTO_OPEN2";
    public static final String SDATA_APP_AUTO_OPEN3 = "SDATA_APP_AUTO_OPEN3";
    public static final String SDATA_APP_AUTO_OPEN4 = "SDATA_APP_AUTO_OPEN4";

    //首页设置
    public static final String SDATA_LAUNCHER_ITEM_SORT_ = "SDATA_LAUNCHER_ITEM_SORT_";
    public static final String SDATA_LAUNCHER_ITEM_OPEN_ = "SDATA_LAUNCHER_ITEM_OPEN_";
    public static final String SDATA_LAUNCHER_ITEM_NUM = "SDATA_LAUNCHER_ITEM_NUM";
    public static final String SDATA_LAUNCHER_ITEM_TRAN = "SDATA_LAUNCHER_ITEM_TRAN";
    public static final String SDATA_LAUNCHER_LAYOUT = "SDATA_LAUNCHER_LAYOUT";
    public static final String SDATA_LAUNCHER_PROMPT_SHOW = "SDATA_LAUNCHER_PROMPT_SHOW";

    public static final String SDATA_MUSIC_INSIDE_COVER = "SDATA_MUSIC_INSIDE_COVER";//是否使用控件自己的封面
    public static final String SDATA_MUSIC_USE_LRC = "SDATA_MUSIC_USE_LRC";//是否使用歌词
    public static final String SDATA_USE_NAVI_POP = "SDATA_USE_NAVI_POP"; //是否使用导航弹窗
    public static final String SDATA_USE_NAVI_XUNHYANG = "SDATA_USE_NAVI_XUNHYANG";//是否使用巡航
    public static final String SDATA_HOME_FULL = "SDATA_HOME_FULL";//首页是否全屏
    public static final String SDATA_USE_VA = "SDATA_USE_VA";//是否使用语音助手
    public static final String SDATA_LOG_OPEN = "SDATA_LOG_OPEN";//是否打开日志
    public static final String SDATA_DRIVING_VIEW = "SDATA_DRIVING_VIEW";//驾驶界面风格
    public static final String SDATA_SHOW_USB_MOUNT = "SDATA_SHOW_USB_MOUNT";//是否显示挂载
    public static final String SDATA_AUTO_TO_DRIVING = "SDATA_AUTO_TO_DRIVING";//是否自动跳入驾驶界面
    public static final String SDATA_AUTO_TO_DRIVING_TYPE = "SDATA_AUTO_TO_DRIVING_TYPE";//是否自动跳入驾驶界面
    public static final String SDATA_AUTO_TO_DRIVING_TIME = "SDATA_AUTO_TO_DRIVING_TIME";//自动跳入驾驶界面时间
}
