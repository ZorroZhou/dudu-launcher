package com.wow.carlauncher.common;

import android.app.Application;
import android.content.Intent;
import android.os.Environment;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.AppWidgetManage;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.ex.manage.skin.SkinManage;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.baiduVoice.BaiduVoiceAssistant;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.manage.location.LocationManage;
import com.wow.carlauncher.ex.manage.okHttp.OkHttpManage;
import com.wow.carlauncher.ex.manage.speed.SpeedManage;
import com.wow.carlauncher.ex.manage.time.TimeManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.ex.plugin.console.ConsolePlugin;
import com.wow.carlauncher.ex.plugin.fk.FangkongPlugin;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.repertory.db.entiy.CoverTemp;
import com.wow.carlauncher.repertory.db.manage.DatabaseInfo;
import com.wow.carlauncher.repertory.db.manage.DatabaseManage;
import com.wow.carlauncher.view.popup.ConsoleWin;
import com.wow.carlauncher.view.popup.NaviWin;
import com.wow.carlauncher.view.popup.PopupWin;
import com.wow.carlauncher.view.popup.VoiceWin;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by liuyixian on 2017/9/20.
 */

public class AppContext {
    private static AppContext self;

    public synchronized static AppContext self() {
        if (self == null) {
            self = new AppContext();
        }
        return self;
    }

    private CarLauncherApplication application;

    private long startTime;

    public long getStartTime() {
        return startTime;
    }

    private AppContext() {

    }

    public void init(CarLauncherApplication app) {
        this.application = app;
        this.startTime = System.currentTimeMillis();

        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();

        SkinManage.self().init(app);

        SharedPreUtil.init(app);

        LogEx.init(app);
        LogEx.setSaveFile(SharedPreUtil.getBoolean(CommonData.SDATA_LOG_OPEN, false));

        OkHttpManage.self().init(app);

        TaskExecutor.self().init();

        AppWidgetManage.self().init(app);

        DatabaseManage.init(app, getDatabaseInfo());
        //图片加载工具
        ImageManage.self().init(app);
        //通知工具
        ToastManage.self().init(app);
        //时间管理器
        TimeManage.self().init(app);
        //蓝牙客户端
        BleManage.self().init(app);
        //定位管理器
        LocationManage.self().init(app);
        //主题管理器
        ThemeManage.self().init(app);
        //app信息管理器
        AppInfoManage.self().init(app);
        //插件相关
        //高德地图插件
        AMapCarPlugin.self().init(app);
        //音乐插件
        MusicPlugin.self().init(app);
        //方控插件
        FangkongPlugin.self().init(app);
        //OBD插件
        ObdPlugin.self().init(app);
        //车机控制插件
        ConsolePlugin.self().init(app);
        //悬浮窗
        //弹出敞口
        PopupWin.self().init(app);
        //控制窗口
        ConsoleWin.self().init(app);
        //导航窗口
        NaviWin.self().init(app);
        //语音窗口
        VoiceWin.self().init(app);
        //单独的速度插件
        SpeedManage.self().init(app);

        if (SharedPreUtil.getBoolean(CommonData.SDATA_USE_VA, false)) {
            BaiduVoiceAssistant.self().init(app);
            TaskExecutor.self().run(() -> BaiduVoiceAssistant.self().startWakeUp(), 1000);
        }

        int size = SharedPreUtil.getInteger(CommonData.SDATA_POPUP_SIZE, 1);
        PopupWin.self().setRank(size + 1);
        handerException();

        TaskExecutor.self().run(() -> {
            if (SharedPreUtil.getBoolean(CommonData.SDATA_APP_AUTO_OPEN_USE, false)) {
                LogEx.d(this, "开始唤醒其他APP");
                if (CommonUtil.isNotNull(SharedPreUtil.getString(CommonData.SDATA_APP_AUTO_OPEN1))) {
                    LogEx.d(this, "SDATA_APP_AUTO_OPEN1 " + SharedPreUtil.getString(CommonData.SDATA_APP_AUTO_OPEN1));
                    AppInfoManage.self().openApp(SharedPreUtil.getString(CommonData.SDATA_APP_AUTO_OPEN1));
                }
                if (CommonUtil.isNotNull(SharedPreUtil.getString(CommonData.SDATA_APP_AUTO_OPEN2))) {
                    LogEx.d(this, "SDATA_APP_AUTO_OPEN2 " + SharedPreUtil.getString(CommonData.SDATA_APP_AUTO_OPEN2));
                    AppInfoManage.self().openApp(SharedPreUtil.getString(CommonData.SDATA_APP_AUTO_OPEN2));
                }
                if (CommonUtil.isNotNull(SharedPreUtil.getString(CommonData.SDATA_APP_AUTO_OPEN3))) {
                    LogEx.d(this, "SDATA_APP_AUTO_OPEN3 " + SharedPreUtil.getString(CommonData.SDATA_APP_AUTO_OPEN3));
                    AppInfoManage.self().openApp(SharedPreUtil.getString(CommonData.SDATA_APP_AUTO_OPEN3));
                }
                if (CommonUtil.isNotNull(SharedPreUtil.getString(CommonData.SDATA_APP_AUTO_OPEN4))) {
                    LogEx.d(this, "SDATA_APP_AUTO_OPEN4 " + SharedPreUtil.getString(CommonData.SDATA_APP_AUTO_OPEN4));
                    AppInfoManage.self().openApp(SharedPreUtil.getString(CommonData.SDATA_APP_AUTO_OPEN4));
                }
                LogEx.d(this, "延迟返回:" + SharedPreUtil.getInteger(CommonData.SDATA_APP_AUTO_OPEN_BACK, 5) + "秒");
                TaskExecutor.self().run(() -> {
                    LogEx.d(this, "back to desktop");
                    Intent home = new Intent(Intent.ACTION_MAIN);
                    home.addCategory(Intent.CATEGORY_HOME);
                    home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    application.startActivity(home);
                }, SharedPreUtil.getInteger(CommonData.SDATA_APP_AUTO_OPEN_BACK, CommonData.SDATA_APP_AUTO_OPEN_BACK_DF) * 1000);
            } else {
                LogEx.d(this, "不唤醒其他APP");
            }
        });

        LogEx.d(this, "APP初始化完毕 ");
//        BroadcastReceiver br = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                System.out.println(intent.getAction());
//                if (intent.getExtras() != null) {
//                    for (String key : intent.getExtras().keySet()) {
//                        System.out.println(key + " : " + intent.getExtras().get(key));
//                    }
//                }
//            }
//        };
//
//        IntentFilter intentFilter = new IntentFilter();
//
//        intentFilter.addAction("com.nwd.action.SL_WIDGET_COMMAND");
//        intentFilter.addAction("com.nwd.action.ACTION_SEND_RADIO_FREQUENCE");
//        intentFilter.addAction("com.nwd.radio.service.ACTION_RADIO_SERVICE");
//        app.registerReceiver(br, intentFilter);
    }

    public Application getApplication() {
        return this.application;
    }

    private void handerException() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            try {
                String path;

                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
                    path = Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + File.separator + "carLauncherError";
                } else {// 如果SD卡不存在，就保存到本应用的目录下
                    path = getApplication().getFilesDir().getAbsolutePath()
                            + File.separator + "carLauncherError";
                }

                File pathFile = new File(path);
                if (!pathFile.exists()) {
                    pathFile.mkdirs();
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                String date = format.format(new Date(System.currentTimeMillis()));

                File file = new File(path, "log_"
                        + date + ".log");
                if (!file.exists() && file.createNewFile()) {
                    LogEx.d(this, "创建文件");
                } else {
                    return;
                }

                PrintWriter pw = new PrintWriter(new FileWriter(file));
                e.printStackTrace(pw);
                pw.close();
                System.exit(0);
            } catch (Exception ee) {
                e.printStackTrace();
            }
        });
    }


    private DatabaseInfo getDatabaseInfo() {
        return new DatabaseInfo() {
            @Override
            public String getDbPath() {
                return "wow_car";
            }

            @Override
            public int getDbVersion() {
                return 1;
            }

            @Override
            public Class<?>[] getBeanClass() {
                return new Class[]{CoverTemp.class};
            }
        };
    }
}
