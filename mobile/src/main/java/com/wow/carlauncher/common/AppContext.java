package com.wow.carlauncher.common;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.common.user.LocalUser;
import com.wow.carlauncher.common.user.event.UEventRefreshLoginState;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.AppWidgetManage;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.baiduVoice.BaiduVoiceAssistant;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.manage.location.LocationManage;
import com.wow.carlauncher.ex.manage.okHttp.OkHttpManage;
import com.wow.carlauncher.ex.manage.skin.SkinManage;
import com.wow.carlauncher.ex.manage.speed.SpeedManage;
import com.wow.carlauncher.ex.manage.time.TimeManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.ex.plugin.console.ConsolePlugin;
import com.wow.carlauncher.ex.plugin.fk.FangkongPlugin;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.xmlyfm.XmlyfmPlugin;
import com.wow.carlauncher.repertory.db.entiy.CoverTemp;
import com.wow.carlauncher.repertory.db.entiy.SkinInfo;
import com.wow.carlauncher.repertory.db.manage.DatabaseInfo;
import com.wow.carlauncher.repertory.db.manage.DatabaseManage;
import com.wow.carlauncher.repertory.server.CommonService;
import com.wow.carlauncher.view.popup.ConsoleWin;
import com.wow.carlauncher.view.popup.NaviWin;
import com.wow.carlauncher.view.popup.PopupWin;
import com.wow.carlauncher.view.popup.VoiceWin;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.wow.carlauncher.common.CommonData.LOGIN_USER_ID;
import static com.wow.carlauncher.common.CommonData.LOGIN_USER_INFO;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_SKIN_DAY;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_SKIN_NIGHT;
import static com.wow.carlauncher.repertory.server.ServerConstant.NET_ERROR;
import static com.wow.carlauncher.repertory.server.ServerConstant.RES_ERROR;


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

    private LocalUser localUser;

    public LocalUser getLocalUser() {
        return localUser;
    }

    public void loginSuccess(LocalUser localUser) {
        this.localUser = localUser;
        EventBus.getDefault().post(new UEventRefreshLoginState().setLogin(true));
        SharedPreUtil.saveLong(LOGIN_USER_ID, localUser.getUserId());
        SharedPreUtil.saveString(LOGIN_USER_INFO, GsonUtil.getGson().toJson(localUser));
    }

    public void logout() {
        this.localUser = null;
        EventBus.getDefault().post(new UEventRefreshLoginState().setLogin(false));
        SharedPreUtil.saveLong(LOGIN_USER_ID, -1L);
        SharedPreUtil.saveString(LOGIN_USER_INFO, "");
    }

    private AppContext() {

    }

    public void init(CarLauncherApplication app) {
        this.application = app;
        this.startTime = System.currentTimeMillis();

        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
        //共享信息管理器
        SharedPreUtil.init(app);
        //初始化某些参数
        if (CommonUtil.isNull(SharedPreUtil.getString(SDATA_APP_SKIN_DAY))) {
            SharedPreUtil.saveString(SDATA_APP_SKIN_DAY, "com.wow.carlauncher.theme");
        }
        if (CommonUtil.isNull(SharedPreUtil.getString(SDATA_APP_SKIN_NIGHT))) {
            SharedPreUtil.saveString(SDATA_APP_SKIN_NIGHT, "com.wow.carlauncher.theme.heise");
        }

        //日志管理器
        LogEx.init(app);
        //网络
        OkHttpManage.self().init(app);
        //任务管理器
        TaskExecutor.self().init();
        //数据库管理器
        DatabaseManage.init(app, getDatabaseInfo());
        //皮肤管理器
        SkinManage.self().init(app);
        //插件管理器
        AppWidgetManage.self().init(app);
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
        //单独的速度插件
        SpeedManage.self().init(app);

        XmlyfmPlugin.self().init(app);

        if (SharedPreUtil.getBoolean(CommonData.SDATA_USE_VA, false)) {
            BaiduVoiceAssistant.self().init(app);
            TaskExecutor.self().run(() -> BaiduVoiceAssistant.self().startWakeUp(), 1000);
        }

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
        LogEx.d(this, "APP初始化完毕 开始异步登陆流程 time:" + (System.currentTimeMillis() - startTime));

        TaskExecutor.self().run(() -> {
            long uid = SharedPreUtil.getLong(LOGIN_USER_ID, -1);
            if (uid > 0) {
                LocalUser user = GsonUtil.getGson().fromJson(SharedPreUtil.getString(LOGIN_USER_INFO), LocalUser.class);
                if (user != null && CommonUtil.isNotNull(user.getToken())) {
                    loginSuccess(user);
                    CommonService.loginByToken(user.getToken(), (code, msg, loginInfo) -> {
                        if (code != NET_ERROR && code != RES_ERROR && code != 0) {
                            logout();
                        } else {
                            if (code == 0) {
                                ToastManage.self().show("欢迎回来:" + localUser.getNickname());
                                AppContext.self().loginSuccess(new LocalUser().setUserId(loginInfo.getId()).setToken(loginInfo.getToken()).setUserPic(user.getUserPic()).setNickname(user.getNickname()).setEmail(loginInfo.getEmail()));
                            }
                        }
                    });
                }
            }
            try {
                String deviceId = Settings.System.getString(app.getContentResolver(), Settings.System.ANDROID_ID);
                CommonService.activate(deviceId);
            } catch (Exception ignored) {
            }
        });
    }

    public CarLauncherApplication getApplication() {
        return this.application;
    }

    private DatabaseInfo getDatabaseInfo() {
        return new DatabaseInfo() {
            @Override
            public String getDbPath() {
                return "wow_car";
            }

            @Override
            public int getDbVersion() {
                return 4;
            }

            @Override
            public Class<?>[] getBeanClass() {
                return new Class[]{CoverTemp.class, SkinInfo.class};
            }
        };
    }
}
