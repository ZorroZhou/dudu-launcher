package com.wow.carlauncher.common;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.manage.location.LocationManage;
import com.wow.carlauncher.ex.manage.time.TimeManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.manage.trip.TripManage;
import com.wow.carlauncher.ex.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.ex.plugin.console.ConsolePlugin;
import com.wow.carlauncher.ex.plugin.fk.FangkongPlugin;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.repertory.db.AppDbInfo;
import com.wow.carlauncher.view.popupWindow.PopupWin;
import com.wow.frame.SFrame;
import com.wow.frame.declare.SAppDeclare;
import com.wow.frame.declare.SDatabaseDeclare;
import com.wow.frame.repertory.dbTool.DatabaseInfo;
import com.wow.frame.util.SharedPreUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.wow.carlauncher.common.CommonData.TAG;


/**
 * Created by liuyixian on 2017/9/20.
 */

public class AppContext implements SAppDeclare, SDatabaseDeclare {
    private static AppContext self;

    public synchronized static AppContext self() {
        if (self == null) {
            self = new AppContext();
        }
        return self;
    }

    private CarLauncherApplication application;

//    private BluetoothClient bluetoothClient;
//
//    public BluetoothClient getBluetoothClient() {
//        return bluetoothClient;
//    }

    private AppContext() {

    }

    public void init(CarLauncherApplication app) {
        this.application = app;
        SFrame.init(this);
        //通知工具
        ToastManage.self().init(app);
        TimeManage.self().init(app);
        //蓝牙客户端
        BleManage.self().init(app);
        //定位管理器
        LocationManage.self().init(app);

        //插件相关
        //高德地图插件
        AMapCarPlugin.self().init(app);
        //音乐插件
        MusicPlugin.self().init(app);
        //方控插件
        FangkongPlugin.self().init(app);
        //OBD插件
        ObdPlugin.self().init(app);

        //app信息管理器
        AppInfoManage.self().init(app);
        //车机控制插件
        ConsolePlugin.self().init(app);
        //弹出敞口
        PopupWin.self().init(app);

        //行程管理器
        TripManage.self().init(app);

        int size = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_SIZE, 1);
        PopupWin.self().setRank(size + 1);
        handerException();
    }

    @Override
    public Application getApplication() {
        return this.application;
    }

    @Override
    public DatabaseInfo getDatabaseInfo() {
        return new AppDbInfo();
    }

    private void handerException() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                try {
                    String path;

                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
                        path = Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + File.separator + "carLauncherError";
                    } else {// 如果SD卡不存在，就保存到本应用的目录下
                        path = getApplication().getFilesDir().getAbsolutePath()
                                + File.separator + "error";
                    }

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                    String date = format.format(new Date(System.currentTimeMillis()));

                    File file = new File(path, "log_"
                            + date + ".log");
                    if (!file.exists() && file.mkdirs() && file.createNewFile()) {
                        Log.e(TAG, "创建文件");
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
            }
        });
    }
}
