package com.wow.carlauncher.common.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Date;

/**
 * Created by liuyixian on 16/1/5.
 */
public class AlarmManagerUtil {
    private static AlarmManager getAlarmManager(Context ctx) {
        return (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
    }

    public static void set(Context ctx, Class<? extends BroadcastReceiver> clazz, long time, String cmd) {
        AlarmManager am = getAlarmManager(ctx);
        Intent i = new Intent(ctx, clazz);
        i.setAction(cmd);
        PendingIntent pi = PendingIntent.getBroadcast(ctx, 0, i, 0);
        am.cancel(pi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pi);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pi);
        }

    }

    public static void set(Context ctx, Class<? extends BroadcastReceiver> clazz, String dateTime, String cmd) {
        AlarmManager am = getAlarmManager(ctx);
        Intent i = new Intent(ctx, clazz);
        i.setAction(cmd);
        PendingIntent pi = PendingIntent.getBroadcast(ctx, 0, i, 0);
        am.cancel(pi);
        Date d = DateUtil.stringToDate(dateTime, "yyyy-MM-dd HH:mm:ss");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, d.getTime(), pi);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, d.getTime(), pi);
        }
    }

    public static void cancel(Context ctx, Class<? extends BroadcastReceiver> clazz, String cmd) {
        AlarmManager am = getAlarmManager(ctx);
        Intent i = new Intent(ctx, clazz);
        i.setAction(cmd);
        PendingIntent pi = PendingIntent.getBroadcast(ctx, 0, i, 0);
        am.cancel(pi);
    }
}
