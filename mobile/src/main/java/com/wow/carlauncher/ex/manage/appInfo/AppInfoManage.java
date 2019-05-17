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
import android.widget.ImageView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppIconTemp;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.appInfo.event.MAppInfoRefreshShowEvent;
import com.wow.carlauncher.ex.manage.skin.SkinManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.view.activity.driving.DrivingActivity;
import com.wow.carlauncher.view.activity.set.SetActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.wow.carlauncher.common.CommonData.SDATA_DOCK1_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK2_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK3_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK4_CLASS;
import static com.wow.carlauncher.ex.manage.appInfo.AppInfo.INTERNAL_APP_DRIVING;
import static com.wow.carlauncher.ex.manage.appInfo.AppInfo.INTERNAL_APP_SETTING;
import static com.wow.carlauncher.ex.manage.appInfo.AppInfo.MARK_INTERNAL_APP;
import static com.wow.carlauncher.ex.manage.appInfo.AppInfo.MARK_OTHER_APP;

/**
 * Created by 10124 on 2017/11/13.
 */

public class AppInfoManage extends ContextEx {
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

    private PackageManager packageManager;
    private Map<String, AppInfo> allAppInfosMap;
    private Map<String, AppInfo> internalAppsInfoMap;


    private List<AppInfo> allAppInfosList;
    private List<AppInfo> showAppInfosList;
    private List<AppInfo> otherAppInfosList;

    //todo 需要优化效率
    public List<AppInfo> getAllAppInfos() {
        return new ArrayList<>(allAppInfosList);
    }

    //todo 需要优化效率
    public List<AppInfo> getShowAppInfos() {
        return new ArrayList<>(showAppInfosList);
    }

    //todo 需要优化效率
    public List<AppInfo> getOtherAppInfos() {
        return new ArrayList<>(otherAppInfosList);
    }

    //todo 需要优化效率
    public List<AppInfo> getInternalAppInfos() {
        return new ArrayList<>(allAppInfosMap.values());
    }

    public void init(Context c) {
        long t1 = System.currentTimeMillis();
        setContext(c);
        this.packageManager = getContext().getPackageManager();

        allAppInfosList = Collections.synchronizedList(new ArrayList<>());
        showAppInfosList = Collections.synchronizedList(new ArrayList<>());
        otherAppInfosList = Collections.synchronizedList(new ArrayList<>());

        allAppInfosMap = new ConcurrentHashMap<>();

        internalAppsInfoMap = new ConcurrentHashMap<>();
        internalAppsInfoMap.put(INTERNAL_APP_DRIVING, new InternalAppInfo("驾驶", INTERNAL_APP_DRIVING, MARK_INTERNAL_APP, DrivingActivity.class));
        internalAppsInfoMap.put(INTERNAL_APP_SETTING, new InternalAppInfo("桌面设置", INTERNAL_APP_SETTING, MARK_INTERNAL_APP, SetActivity.class));
        refreshAppInfo();

        LogEx.d(this, "init time:" + (System.currentTimeMillis() - t1));
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
        if (allAppInfosMap.containsKey(app)) {
            return allAppInfosMap.get(app);
        }
        return null;
    }

    public void setIconWithSkin(ImageView view, String app) {
        AppInfo info = getAppInfo(app);
        if (info != null) {
            if (MARK_OTHER_APP == info.appMark) {
                int r = AppIconTemp.getIcon(info.clazz);
                if (r > 0) {
                    view.setImageResource(r);
                } else {
                    try {
                        PackageInfo packageInfo = packageManager.getPackageInfo(info.clazz, 0);
                        view.setImageDrawable(packageInfo.applicationInfo.loadIcon(packageManager));
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                        view.setImageResource(R.mipmap.ic_launcher);
                    }
                }
            } else {
                switch (info.clazz) {
                    case INTERNAL_APP_DRIVING: {
                        view.setImageResource(R.drawable.app_icon_obd);
                        break;
                    }
                    case INTERNAL_APP_SETTING: {
                        view.setImageResource(R.drawable.app_icon_set);
                        break;
                    }
                }
            }
        } else {
            view.setImageResource(R.mipmap.ic_launcher);
        }
    }

    public Drawable getAppIcon(String app, boolean withTheme) {
        Resources resources = getContext().getResources();
        AppInfo info = getAppInfo(app);
        if (info != null) {
            if (MARK_OTHER_APP == info.appMark) {
                try {
                    PackageInfo packageInfo = packageManager.getPackageInfo(info.clazz, 0);
                    return packageInfo.applicationInfo.loadIcon(packageManager);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                return resources.getDrawable(R.mipmap.ic_launcher);
            } else {
                switch (info.clazz) {
                    case INTERNAL_APP_DRIVING: {
                        if (withTheme) {
                            return SkinManage.self().getDrawable(R.drawable.app_icon_obd);
                        } else {
                            return ContextCompat.getDrawable(getContext(), R.drawable.app_icon_obd);
                        }
                    }
                    case INTERNAL_APP_SETTING: {
                        if (withTheme) {
                            return SkinManage.self().getDrawable(R.drawable.app_icon_set);
                        } else {
                            return ContextCompat.getDrawable(getContext(), R.drawable.app_icon_set);
                        }
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
        return info != null;
    }

    public boolean openApp(String app) {
        LogEx.d(this, "openApp:" + app);
        AppInfo info = getAppInfo(app);
        if (info != null) {
            if (info.appMark == MARK_OTHER_APP) {
                Intent appIntent = packageManager.getLaunchIntentForPackage(info.clazz);
                if (appIntent == null) {
                    ToastManage.self().show("APP不存在!!");
                    return false;
                }
                appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(appIntent);
                return true;
            } else {
                InternalAppInfo appInfo = (InternalAppInfo) info;
                Intent appIntent = new Intent(getContext(), appInfo.loadClazz);
                appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(appIntent);
                return true;
            }
        }
        return false;
    }

    private static final byte[] LOCK1 = new byte[0];

    public void refreshAppInfo() {
        LogEx.d(this, "refreshAppInfo");
        TaskExecutor.self().run(() -> {
            synchronized (LOCK1) {
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                final List<ResolveInfo> apps = packageManager.queryIntentActivities(
                        mainIntent, 0);
                Collections.sort(apps, new ResolveInfo.DisplayNameComparator(packageManager));
                //清理所有的存储
                allAppInfosMap.clear();
                allAppInfosList.clear();

                allAppInfosMap.putAll(internalAppsInfoMap);
                for (ResolveInfo info : apps) {
                    String packageName = info.activityInfo.applicationInfo.packageName;
                    allAppInfosMap.put(packageName, new AppInfo(info.loadLabel(packageManager).toString(), packageName, MARK_OTHER_APP));
                }

                allAppInfosList.addAll(allAppInfosMap.values());
                Collections.sort(allAppInfosList, (appInfo1, appInfo2) -> appInfo2.appMark - appInfo1.appMark);

                otherAppInfosList.clear();
                for (AppInfo appInfo : allAppInfosMap.values()) {
                    if (appInfo.appMark == MARK_OTHER_APP) {
                        otherAppInfosList.add(appInfo);
                    }
                }
            }
            refreshShowApp();
        });
    }

    private static final byte[] LOCK2 = new byte[0];

    public void refreshShowApp() {
        LogEx.d(this, "refreshShowApp");
        TaskExecutor.self().run(() -> {
            synchronized (LOCK2) {
                showAppInfosList.clear();
                showAppInfosList.addAll(allAppInfosList);
                String packname1 = SharedPreUtil.getString(SDATA_DOCK1_CLASS);
                String packname2 = SharedPreUtil.getString(SDATA_DOCK2_CLASS);
                String packname3 = SharedPreUtil.getString(SDATA_DOCK3_CLASS);
                String packname4 = SharedPreUtil.getString(SDATA_DOCK4_CLASS);
                String selectapp = SharedPreUtil.getString(CommonData.SDATA_HIDE_APPS);
                List<AppInfo> hides = new ArrayList<>();
                for (AppInfo appInfo : showAppInfosList) {
                    if (selectapp.contains("[" + appInfo.clazz + "]")
                            || (CommonUtil.isNotNull(packname1) && packname1.contains(appInfo.clazz))
                            || (CommonUtil.isNotNull(packname2) && packname2.contains(appInfo.clazz))
                            || (CommonUtil.isNotNull(packname3) && packname3.contains(appInfo.clazz))
                            || (CommonUtil.isNotNull(packname4) && packname4.contains(appInfo.clazz))) {
                        hides.add(appInfo);
                    }
                }
                showAppInfosList.removeAll(hides);

                postEvent(new MAppInfoRefreshShowEvent());
            }
        });
    }

}
