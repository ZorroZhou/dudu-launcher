package com.wow.carlauncher.common;

import android.app.Application;
import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.activity.LockActivity;
import com.wow.carlauncher.common.console.ConsoleManage;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.popupWindow.ConsoleWin;
import com.wow.carlauncher.popupWindow.PopupWin;
import com.wow.carlauncher.service.MainService;
import com.wow.carlauncher.webservice.AppWsInfo;
import com.wow.carlauncher.webservice.service.CommonService;
import com.wow.frame.SFrame;
import com.wow.frame.declare.SAppDeclare;
import com.wow.frame.declare.SWebServiceDeclare;
import com.wow.frame.repertory.remote.WebServiceInfo;
import com.wow.frame.repertory.remote.WebServiceManage;
import com.wow.frame.util.AndroidUtil;
import com.wow.frame.util.SharedPreUtil;

import org.xutils.x;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * Created by liuyixian on 2017/9/20.
 */

public class AppContext implements SWebServiceDeclare, SAppDeclare {
    private static AppContext self;

    public synchronized static AppContext self() {
        if (self == null) {
            self = new AppContext();
        }
        return self;
    }

    private CarLauncherApplication application;

    private AppContext() {

    }

    public void init(CarLauncherApplication app) {
        this.application = app;
        SFrame.init(this);

        AppInfoManage.self().init(app);
        AppWidgetManage.self().init(app);
        ConsoleManage.self().init(app);
        PluginManage.self().init(app);
        PopupWin.self().init(app);
        ConsoleWin.self().init(app);

        int size = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_SIZE, 1);
        PopupWin.self().setRank(size + 1);

        handerException();
    }

    @Override
    public WebServiceInfo getWebServiceInfo() {
        return new AppWsInfo();
    }

    @Override
    public Application getApplication() {
        return this.application;
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
