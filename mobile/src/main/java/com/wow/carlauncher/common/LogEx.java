package com.wow.carlauncher.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.time.event.TMEventMinute;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogEx {
    private final static String TAG = "DUDU-LAUNCHER-";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault());

    private static boolean saveFile = false;

    private static List<String> logs = Collections.synchronizedList(new ArrayList<>());

    private static LogEventBus logEventBus;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static void init(Context context) {
        LogEx.context = context;
        LogEx.setSaveFile(SharedPreUtil.getBoolean(CommonData.SDATA_LOG_OPEN, false));
    }

    public static void setSaveFile(boolean saveFile) {
        LogEx.saveFile = saveFile;
        if (saveFile) {
            runSave();
        } else {
            stopSave();
        }
    }

    public static void d(Object target, Object log) {
        if (target instanceof Class) {
            Log.d(TAG + ((Class) target).getSimpleName(), String.valueOf(log));
        } else {
            Log.d(TAG + target.getClass().getSimpleName(), String.valueOf(log));
        }
        if (saveFile) {
            logs.add(createLog(target, log, "DEBUG"));
        }
    }

    public static void e(Object target, Object log) {
        if (target instanceof Class) {
            Log.e(TAG + ((Class) target).getSimpleName(), String.valueOf(log));
        } else {
            Log.e(TAG + target.getClass().getSimpleName(), String.valueOf(log));
        }
        if (saveFile) {
            logs.add(createLog(target, log, "ERROR"));
        }
    }

    private static void runSave() {
        stopSave();

        logEventBus = new LogEventBus(() -> {
            ArrayList<String> log = new ArrayList<>(logs);
            logs.clear();
            return log;
        });
        logEventBus.init();
    }

    private static void stopSave() {
        logs.clear();
        if (logEventBus != null) {
            logEventBus.destroy();
            logEventBus = null;
        }
    }

    private static String createLog(Object target, Object log, String level) {
        if (target instanceof Class) {
            return sdf.format(new Date()) + ":" + level + ":" + TAG + ((Class) target).getSimpleName() + ":" + String.valueOf(log);
        } else {
            return sdf.format(new Date()) + ":" + level + ":" + TAG + target.getClass().getSimpleName() + ":" + String.valueOf(log);
        }
    }

    static class LogEventBus {
        private GetLogList getLogList;

        private File logFile;

        public LogEventBus(GetLogList getLogList) {
            this.getLogList = getLogList;
        }

        void init() {
            EventBus.getDefault().register(this);
        }


        @Subscribe(threadMode = ThreadMode.BACKGROUND)
        public void onEvent(TMEventMinute event) {
            List<String> logs = getLogList.getLogs();
            if (logFile == null || logFile.length() > 10 * 1024 * 1024) {
                logFile = getNewLogFile();
            }
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(logFile));
                for (String log : logs) {
                    pw.append(log).append("\n");
                }
                pw.flush();
                pw.close();
                Log.e(TAG, "写入成功");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        void destroy() {
            EventBus.getDefault().unregister(this);
        }


        private File getNewLogFile() {
            Log.e(TAG, "创建新的日志文件");
            String path;
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
                path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + File.separator + "duduLauncher" + File.separator + "log";
            } else {// 如果SD卡不存在，就保存到本应用的目录下
                path = LogEx.context.getFilesDir().getAbsolutePath()
                        + File.separator + "duduLauncher" + File.separator + "log";
            }

            File pathFile = new File(path);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            String date = format.format(new Date(System.currentTimeMillis()));
            return new File(path, "log_"
                    + date + ".log");
        }
    }

    interface GetLogList {
        ArrayList<String> getLogs();
    }
}
