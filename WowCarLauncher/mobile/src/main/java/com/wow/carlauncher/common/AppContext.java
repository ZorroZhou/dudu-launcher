package com.wow.carlauncher.common;

import android.app.Application;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.manage.location.LocationManage;
import com.wow.carlauncher.ex.manage.time.TimeManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.manage.trip.TripManage;
import com.wow.carlauncher.ex.plugin.console.ConsolePlugin;
import com.wow.carlauncher.ex.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.ex.plugin.fk.FangkongPlugin;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.repertory.db.AppDbInfo;
import com.wow.carlauncher.view.popupWindow.PopupWin;
import com.wow.carlauncher.repertory.webservice.AppWsInfo;
import com.wow.carlauncher.repertory.webservice.service.CommonService;
import com.wow.frame.SFrame;
import com.wow.frame.declare.SAppDeclare;
import com.wow.frame.declare.SDatabaseDeclare;
import com.wow.frame.declare.SWebServiceDeclare;
import com.wow.frame.repertory.dbTool.DatabaseInfo;
import com.wow.frame.repertory.remote.WebServiceInfo;
import com.wow.frame.repertory.remote.WebServiceManage;
import com.wow.frame.util.AndroidUtil;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * Created by liuyixian on 2017/9/20.
 */

public class AppContext implements SWebServiceDeclare, SAppDeclare, SDatabaseDeclare {
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

    }

    @Override
    public WebServiceInfo getWebServiceInfo() {
        return new AppWsInfo();
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
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                WebServiceManage.getService(CommonService.class).appError(AndroidUtil.getPhoneInfo(application), sw.toString());
                pw.close();

                System.exit(0);
            }
        });
    }
}
