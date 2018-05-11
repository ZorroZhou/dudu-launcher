package com.wow.carlauncher.ex.manage.appInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppIconUtil;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.view.activity.driving.DrivingActivity;
import com.wow.carlauncher.ex.manage.appInfo.event.MAppInfoRefreshEvent;
import com.wow.frame.util.CommonUtil;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wow.carlauncher.ex.manage.appInfo.AppInfo.*;

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
    private List<AppInfo> appInfos;

    private Map<String, AppInfo> internalApps;

    public List<AppInfo> getAllAppInfos() {
        return appInfos;
    }

    public List<AppInfo> getOtherAppInfos() {
        List<AppInfo> list = new ArrayList<>();
        for (AppInfo appInfo : appInfos) {
            if (appInfo.appMark == MARK_OTHER_APP) {
                list.add(appInfo);
            }
        }
        return list;
    }

    public List<AppInfo> getInternalAppInfos() {
        List<AppInfo> list = new ArrayList<>();
        for (AppInfo appInfo : appInfos) {
            if (appInfo.appMark == MARK_INTERNAL_APP) {
                list.add(appInfo);
            }
        }
        return list;
    }

    public void init(Context c) {
        this.context = c;
        this.packageManager = this.context.getPackageManager();
        internalApps = new HashMap<>();
        internalApps.put(INTERNAL_APP_DRIVING, new InternalAppInfo(ContextCompat.getDrawable(context, R.mipmap.ic_driving), "仪表盘", INTERNAL_APP_DRIVING, MARK_INTERNAL_APP, DrivingActivity.class));
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

    private List<AppInfo> getAllApp(Context context) {
        List<AppInfo> appInfos = new ArrayList<>();

        PackageManager manager = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> apps = manager.queryIntentActivities(
                mainIntent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
        final int count = apps.size();
        appInfos.clear();
        appInfos.addAll(internalApps.values());
        for (int i = 0; i < count; i++) {
            String packageName = apps.get(i).activityInfo.applicationInfo.packageName;
            appInfos.add(new AppInfo(getIcon(packageName), getName(packageName), packageName, MARK_OTHER_APP));
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
        if (CommonUtil.isNotNull(app)) {
            if (app.contains(CommonData.APP_SEPARATE)) {
                String[] xx = app.split(CommonData.APP_SEPARATE);
                if (xx.length == 2) {
                    if (xx[0].equals(AppInfo.MARK_OTHER_APP + "")) {
                        try {
                            PackageInfo packageInfo = packageManager.getPackageInfo(xx[1], 0);
                            return packageInfo.applicationInfo.loadIcon(packageManager);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else if (xx[0].equals(AppInfo.MARK_INTERNAL_APP + "")) {
                        AppInfo appInfo = internalApps.get(xx[1]);
                        if (appInfo != null) {
                            return appInfo.icon;
                        }
                    }
                }
            } else {
                try {
                    PackageInfo packageInfo = packageManager.getPackageInfo(app, 0);
                    return packageInfo.applicationInfo.loadIcon(packageManager);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return resources.getDrawable(R.mipmap.ic_launcher);
    }

    public CharSequence getName(String app) {
        if (CommonUtil.isNotNull(app)) {
            if (app.contains(CommonData.APP_SEPARATE)) {
                String[] xx = app.split(CommonData.APP_SEPARATE);
                if (xx.length == 2) {
                    if (xx[0].equals(AppInfo.MARK_OTHER_APP + "")) {
                        try {
                            PackageInfo packageInfo = packageManager.getPackageInfo(xx[1], 0);
                            return packageInfo.applicationInfo.loadLabel(packageManager);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else if (xx[0].equals(AppInfo.MARK_INTERNAL_APP + "")) {
                        AppInfo appInfo = internalApps.get(xx[1]);
                        if (appInfo != null) {
                            return appInfo.name;
                        }
                    }
                }
            } else {
                try {
                    PackageInfo packageInfo = packageManager.getPackageInfo(app, 0);
                    return packageInfo.applicationInfo.loadLabel(packageManager);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return "未知应用";
    }

    public boolean checkApp(String app) {
        if (CommonUtil.isNotNull(app)) {
            if (app.contains(CommonData.APP_SEPARATE)) {
                String[] xx = app.split(CommonData.APP_SEPARATE);
                if (xx.length == 2) {
                    if (xx[0].equals(AppInfo.MARK_OTHER_APP + "")) {
                        try {
                            packageManager.getPackageInfo(xx[1], 0);
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (xx[0].equals(AppInfo.MARK_INTERNAL_APP + "")) {
                        InternalAppInfo appInfo = (InternalAppInfo) internalApps.get(xx[1]);
                        if (appInfo != null) {
                            return true;
                        }
                    }
                }
            } else {
                try {
                    packageManager.getPackageInfo(app, 0);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean openApp(String app) {
        if (CommonUtil.isNotNull(app)) {
            if (app.contains(CommonData.APP_SEPARATE)) {
                String[] xx = app.split(CommonData.APP_SEPARATE);
                if (xx.length == 2) {
                    if (xx[0].equals(AppInfo.MARK_OTHER_APP + "")) {
                        Intent appIntent = packageManager.getLaunchIntentForPackage(xx[1]);
                        if (appIntent == null) {
                            ToastManage.self().show("APP不存在!!");
                            return false;
                        }
                        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(appIntent);
                        return true;
                    } else if (xx[0].equals(AppInfo.MARK_INTERNAL_APP + "")) {
                        InternalAppInfo appInfo = (InternalAppInfo) internalApps.get(xx[1]);
                        if (appInfo != null) {
                            Intent appIntent = new Intent(context, appInfo.loadClazz);
                            appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(appIntent);
                            return true;
                        }
                    }
                }
            } else {
                Intent appIntent = packageManager.getLaunchIntentForPackage(app);
                if (appIntent == null) {
                    ToastManage.self().show("APP不存在!!");
                    return false;
                }
                appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(appIntent);
                return true;
            }
        }
        return false;
    }
}
