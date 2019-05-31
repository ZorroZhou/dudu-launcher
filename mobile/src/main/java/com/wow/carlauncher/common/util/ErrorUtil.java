package com.wow.carlauncher.common.util;

import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.repertory.server.CommonService;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ErrorUtil {
    public static void handerException(CarLauncherApplication application) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw2 = new PrintWriter(sw);
                e.printStackTrace(pw2);
                pw2.close();

                String deviceId = Settings.System.getString(application.getContentResolver(), Settings.System.ANDROID_ID);
                CommonService.reportError(deviceId, "SDK:" + Build.VERSION.SDK_INT, sw.toString(), null);

                String path;

                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
                    path = Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + File.separator + "duduLauncher" + File.separator + "error";
                } else {// 如果SD卡不存在，就保存到本应用的目录下
                    path = application.getFilesDir().getAbsolutePath()
                            + File.separator + "duduLauncher" + File.separator + "error";
                }

                File pathFile = new File(path);
                if (!pathFile.exists()) {
                    pathFile.mkdirs();
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                String date = format.format(new Date(System.currentTimeMillis()));

                File file = new File(path, "log_"
                        + date + ".log");
                if (!file.exists() && file.createNewFile()) {
                    LogEx.d("", "创建文件");
                } else {
                    return;
                }

                PrintWriter pw = new PrintWriter(new FileWriter(file));
                e.printStackTrace(pw);
                pw.close();

                System.exit(0);
            } catch (Exception ee) {
                e.printStackTrace();
            }
        });
    }
}
