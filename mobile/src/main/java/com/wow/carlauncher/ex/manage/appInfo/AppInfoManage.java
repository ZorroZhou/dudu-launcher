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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private Map<String, AppInfo> appInfos;
    private Map<String, AppInfo> internalApps;
    private List<ResolveInfo> appResolveInfo;

    public List<AppInfo> getAllAppInfos() {
        List<AppInfo> list = new ArrayList<>(appInfos.values());
        Collections.sort(list, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo appInfo1, AppInfo appInfo2) {
                if (appInfo1.appMark < appInfo2.appMark) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return list;
    }

    public List<AppInfo> getOtherAppInfos() {
        List<AppInfo> list = new ArrayList<>();
        for (AppInfo appInfo : appInfos.values()) {
            if (appInfo.appMark == MARK_OTHER_APP) {
                list.add(appInfo);
            }
        }
        return list;
    }

    public List<AppInfo> getInternalAppInfos() {
        List<AppInfo> list = new ArrayList<>();
        for (AppInfo appInfo : appInfos.values()) {
            if (appInfo.appMark == MARK_INTERNAL_APP) {
                list.add(appInfo);
            }
        }
        return list;
    }

    public void init(Context c) {
        this.context = c;
        this.packageManager = this.context.getPackageManager();

        appResolveInfo = new ArrayList<>();

        appInfos = new ConcurrentHashMap<>();
        internalApps = new ConcurrentHashMap<>();
        internalApps.put(INTERNAL_APP_DRIVING, new InternalAppInfo("驾驶界面", INTERNAL_APP_DRIVING, MARK_INTERNAL_APP, DrivingActivity.class));
        refreshAppInfo(false);
    }

    public void refreshAppInfo(final boolean refresh) {
        x.task().run(new Runnable() {
            @Override
            public void run() {
                loadAllApp(refresh);
                EventBus.getDefault().post(new MAppInfoRefreshEvent());
            }
        });
    }

    private synchronized void loadAllApp(boolean refresh) {
        if (appInfos.isEmpty()) {
            refresh = true;
        }
        if (refresh) {
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            final List<ResolveInfo> apps = packageManager.queryIntentActivities(
                    mainIntent, 0);
            Collections.sort(apps, new ResolveInfo.DisplayNameComparator(packageManager));
            final int count = apps.size();
            appInfos.clear();
            appInfos.putAll(internalApps);
            for (int i = 0; i < count; i++) {
                ResolveInfo info = apps.get(i);
                String packageName = apps.get(i).activityInfo.applicationInfo.packageName;
                appInfos.put(packageName, new AppInfo(info.loadLabel(packageManager).toString(), packageName, MARK_OTHER_APP));
            }
        }
    }

    private AppInfo getAppInfo(String app) {
        if (CommonUtil.isNull(app)) {
            return null;
        }
        if (app.contains(CommonData.CONSTANT_APP_PACKAGE_SEPARATE)) {
            String[] xx = app.split(CommonData.CONSTANT_APP_PACKAGE_SEPARATE);
            if (xx.length == 2) {
                app = xx[1];
            }
        }
        //先刷新一遍,防止出现没加载的情况
        loadAllApp(false);

        if (appInfos.containsKey(app)) {
            return appInfos.get(app);
        }
        return null;
    }

    public Drawable getIcon(String app) {
        Resources resources = this.context.getResources();
        AppInfo info = getAppInfo(app);
        if (info != null) {
            //先根据主题获取图标
            int r = AppIconUtil.getIcon(app);
            if (r != 0) {
                Drawable drawable = resources.getDrawable(r);
                if (drawable != null) {
                    return drawable;
                }
            }

            if (MARK_OTHER_APP == info.appMark) {
                try {
                    PackageInfo packageInfo = packageManager.getPackageInfo(info.clazz, 0);
                    return packageInfo.applicationInfo.loadIcon(packageManager);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                switch (info.clazz) {
                    case INTERNAL_APP_DRIVING: {
                        return ContextCompat.getDrawable(context, R.mipmap.ic_driving);
                    }
                }
            }
        }
        return resources.getDrawable(R.mipmap.ic_launcher);
    }

    public CharSequence getName(String app) {
        AppInfo info = getAppInfo(app);
        if (info != null) {
            return info.name;
        }
        return "未知应用";
    }

    public boolean checkApp(String app) {
        //这里应该是没加载完
        AppInfo info = getAppInfo(app);
        if (info != null) {
            return true;
        }
        return false;
    }

    public boolean openApp(String app) {
        AppInfo info = getAppInfo(app);
        if (info != null) {
            if (info.appMark == MARK_OTHER_APP) {
                Intent appIntent = packageManager.getLaunchIntentForPackage(info.clazz);
                if (appIntent == null) {
                    ToastManage.self().show("APP不存在!!");
                    return false;
                }
                appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(appIntent);
                return true;
            } else {
                InternalAppInfo appInfo = (InternalAppInfo) info;
                Intent appIntent = new Intent(context, appInfo.loadClazz);
                appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(appIntent);
                return true;
            }
        }
        return false;
    }
}
