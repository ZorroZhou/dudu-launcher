package com.wow.frame.util;

import android.app.ActivityManager;
import android.app.Instrumentation;
import android.app.WallpaperManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by 10124 on 2017/11/5.
 */

public class AppUtil {
    public enum NetWorkState {
        NETWORKSTATE_NONE, NETWORKSTATE_MOBILE, NETWORKSTATE_WIFI
    }

    public static NetWorkState getNetWorkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NetWorkState.NETWORKSTATE_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NetWorkState.NETWORKSTATE_MOBILE;
            }
        } else {
            return NetWorkState.NETWORKSTATE_NONE;
        }
        return NetWorkState.NETWORKSTATE_NONE;
    }

    public static String getConnectWifiSsid(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID().replace("\"", "");
    }

    public static boolean isInstall(Context context, String name) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(name, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    public static String getForegroundApp(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long ts = System.currentTimeMillis();
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);
            if (queryUsageStats == null || queryUsageStats.isEmpty()) {
                return null;
            }
            UsageStats recentStats = null;
            for (UsageStats usageStats : queryUsageStats) {
                if (recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()) {
                    recentStats = usageStats;
                }
            }
            return recentStats.getPackageName();
        } else {
            ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName runningTopActivity = activityManager.getRunningTasks(1).get(0).topActivity;
            return runningTopActivity.getPackageName();
        }
    }

    public static List<AppInfo> getAllApp(Context context) {
        List<AppInfo> appInfos = new ArrayList<>();

        PackageManager manager = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> apps = manager.queryIntentActivities(
                mainIntent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
        if (apps != null) {
            final int count = apps.size();
            if (appInfos == null) {
                appInfos = new ArrayList(count);
            }
            appInfos.clear();
            for (int i = 0; i < count; i++) {
                ResolveInfo info = apps.get(i);
                appInfos.add(new AppInfo(info.activityInfo.loadIcon(manager), info.loadLabel(manager).toString(), info.activityInfo.applicationInfo.packageName));
            }
        }
        return appInfos;
    }

    public static class AppInfo {
        public AppInfo(Drawable icon, String name, String packageName) {
            this.icon = icon;
            this.name = name;
            this.packageName = packageName;
        }

        public String packageName;
        public Drawable icon;
        public String name;
    }


    public static void sendKeyCode(final int keyCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建一个Instrumentation对象
                    Instrumentation inst = new Instrumentation();
                    // 调用inst对象的按键模拟方法
                    inst.sendKeyDownUpSync(keyCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static boolean isLivingWallpaper(Context paramContext) {
        return (WallpaperManager.getInstance(paramContext).getWallpaperInfo() != null);
    }
}
