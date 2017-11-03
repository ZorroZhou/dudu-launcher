package com.wow.carlauncher.common.util;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.wow.carlauncher.activity.adapter.SelectAppAdapter;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.ACTIVITY_SERVICE;

public class CommonUtil {
    public static boolean isNull(Object object) {
        if (null == object) {
            return true;
        }
        if (object instanceof String) {
            return object.toString().trim().length() == 0 || object.toString().trim().equals("") || object.toString().trim() == "null";
        }
        if (object instanceof CharSequence) {
            return object.toString().trim().length() == 0 || object.toString().trim().equals("") || object.toString().trim() == "null";
        }
        if (object instanceof Iterable) {
            return !((Iterable<?>) object).iterator().hasNext();
        }
        if (object.getClass().isArray()) {
            if (Array.getLength(object) == 0) {
                return true;
            } else {
                for (int i = 0; i < Array.getLength(object); i++) {
                    if (!isNull(Array.get(object, 1))) {
                        return false;
                    }
                }
                return true;
            }
        }
        if (object instanceof Number) {
            System.out.println("数字");
            return ((Number) object).doubleValue() == 0;
        }
        if (object instanceof Boolean) {
            System.out.println("布尔");
            return !((Boolean) object).booleanValue();
        }
        if (object instanceof Character) {
            System.out.println("字符");
            return object == "\0";
        }
        System.out.println("其他,暂未处理");
        return false;
    }

    public static boolean isNotNull(Object object) {
        return !isNull(object);
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

                //if ((!info.activityInfo.applicationInfo.packageName.equals(context.getPackageName()))) {
                    appInfos.add(new AppInfo(info.activityInfo.loadIcon(manager), info.loadLabel(manager).toString(), info.activityInfo.applicationInfo.packageName));
               // }
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
}
