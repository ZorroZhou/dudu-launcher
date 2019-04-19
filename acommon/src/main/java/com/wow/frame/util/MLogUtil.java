package com.wow.carlauncher.common.util;

import android.util.Log;

public class MLogUtil {
    public static void e(Object msg) {
        String log = "";
        StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        for (StackTraceElement ste : stack) {
            if ((ste.getClassName().indexOf("com.satsoftec.ipspcp")) != -1) {
                log = "Class: " + ste.getClassName();
            }
        }
        Log.e(log, msg + "");
    }

    public static void d(Object msg) {
        Log.d("========debug========", msg + "");
    }


    public static void e(String tag,Object msg) {
        Log.e(tag, msg + "");
    }
}
