package com.wow.carlauncher.common;

import android.content.Context;

import com.wow.frame.util.AppUtil;
import com.wow.carlauncher.activity.launcher.event.LauncherAppRefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import java.util.List;

/**
 * Created by 10124 on 2017/11/13.
 */

public class AppInfoManage {
    private static AppInfoManage self;

    public static AppInfoManage self() {
        if (self == null) {
            self = new AppInfoManage();
        }
        return self;
    }

    private AppInfoManage() {
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
                EventBus.getDefault().post(new LauncherAppRefreshEvent());
            }
        });
    }
}
