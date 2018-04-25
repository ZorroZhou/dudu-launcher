package com.wow.carlauncher.ex.manage.appInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppIconUtil;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.frame.util.AppUtil;
import com.wow.carlauncher.ex.manage.appInfo.event.MAppInfoRefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.TAG;

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
    private PackageManager packageManager;
    private List<AppUtil.AppInfo> appInfos;

    public List<AppUtil.AppInfo> getAppInfos() {
        return appInfos;
    }

    public void init(Context c) {
        this.context = c;
        this.packageManager = this.context.getPackageManager();
        refreshAppInfo();
    }

    public void refreshAppInfo() {
        x.task().run(new Runnable() {
            @Override
            public void run() {
                appInfos = getAllApp(context);
                EventBus.getDefault().post(new MAppInfoRefreshEvent());
            }
        });
    }

    private List<AppUtil.AppInfo> getAllApp(Context context) {
        List<AppUtil.AppInfo> appInfos = new ArrayList<>();

        PackageManager manager = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> apps = manager.queryIntentActivities(
                mainIntent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
        final int count = apps.size();
        appInfos.clear();
        for (int i = 0; i < count; i++) {
            ResolveInfo info = apps.get(i);
            Log.d(TAG, "getAllApp: " + info.activityInfo.applicationInfo.packageName);
            appInfos.add(new AppUtil.AppInfo(getIcon(info.activityInfo.applicationInfo.packageName), info.loadLabel(manager).toString(), info.activityInfo.applicationInfo.packageName));
        }
        return appInfos;
    }

    public Drawable getIcon(String app) {
        int r = AppIconUtil.getIcon(app);
        Resources resources = this.context.getResources();
        if (r != 0) {
            Drawable drawable = resources.getDrawable(r);
            if (drawable != null) {
                return drawable;
            }
        }
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(app, 0);
            return packageInfo.applicationInfo.loadIcon(packageManager);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resources.getDrawable(R.mipmap.ic_launcher);
    }

}
