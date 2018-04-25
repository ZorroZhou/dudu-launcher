package com.wow.carlauncher.ex.manage.appInfo;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.frame.util.AppUtil;
import com.wow.carlauncher.ex.manage.appInfo.event.MAppInfoRefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import java.util.List;

/**
 * Created by 10124 on 2017/11/13.
 */

public class AppInfoManage {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static AppInfoManage instance = new AppInfoManage();
    }

    public static AppInfoManage self() {
        return AppInfoManage.SingletonHolder.instance;
    }

    private AppInfoManage() {
        super();
    }

    private Context context;
    private List<AppUtil.AppInfo> appInfos;

    public List<AppUtil.AppInfo> getAppInfos() {
        return appInfos;
    }

    public void init(Context c) {
        this.context = c;

        refreshAppInfo();
    }

    public void refreshAppInfo() {
        x.task().run(new Runnable() {
            @Override
            public void run() {
                appInfos = AppUtil.getAllApp(context);
                EventBus.getDefault().post(new MAppInfoRefreshEvent());
            }
        });
    }
}
