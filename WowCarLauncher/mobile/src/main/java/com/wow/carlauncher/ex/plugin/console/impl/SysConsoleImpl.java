package com.wow.carlauncher.ex.plugin.console.impl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.KeyEvent;

import com.wow.carlauncher.common.ex.ToastManage;
import com.wow.carlauncher.ex.plugin.console.ConsoleProtoclListener;
import com.wow.carlauncher.ex.plugin.console.ConsoleProtocl;
import com.wow.frame.util.AppUtil;

import java.util.List;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2017/11/9.
 */

public class SysConsoleImpl extends ConsoleProtocl {
    public static final int MARK = 0;

    public SysConsoleImpl(Context context, ConsoleProtoclListener listener) {
        super(context, listener);

        Log.d(TAG, "system console init");
    }

    @Override
    public void decVolume() {
        Log.e(TAG, "system console decVolume");
        AppUtil.sendKeyCode(KeyEvent.KEYCODE_VOLUME_DOWN);
    }

    @Override
    public void incVolume() {
        Log.e(TAG, "system console incVolume");
        AppUtil.sendKeyCode(KeyEvent.KEYCODE_VOLUME_UP);
    }

    @Override
    public void mute() {
        Log.e(TAG, "system console mute");
        AppUtil.sendKeyCode(KeyEvent.KEYCODE_VOLUME_MUTE);
    }

    @Override
    public void clearTask() {
        if (context.checkCallingPermission(android.Manifest.permission.KILL_BACKGROUND_PROCESSES) != PackageManager.PERMISSION_GRANTED &&
                context.checkCallingPermission(android.Manifest.permission.RESTART_PACKAGES) != PackageManager.PERMISSION_GRANTED) {
            //获取一个ActivityManager 对象
            ActivityManager activityManager = (ActivityManager)
                    context.getSystemService(Context.ACTIVITY_SERVICE);
            //获取系统中所有正在运行的进程
            List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager
                    .getRunningAppProcesses();
            int count = 0;//被杀进程计数
            String nameList = "";//记录被杀死进程的包名
            for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
                nameList = "";
                if (appProcessInfo.processName.contains("com.android.system")
                        || appProcessInfo.pid == android.os.Process.myPid())//跳过系统 及当前进程
                    continue;
                String[] pkNameList = appProcessInfo.pkgList;//进程下的所有包名
                for (int i = 0; i < pkNameList.length; i++) {
                    String pkName = pkNameList[i];
                    activityManager.killBackgroundProcesses(pkName);//杀死该进程
                    count++;//杀死进程的计数+1
                    nameList += "  " + pkName;
                }
                Log.i(TAG, nameList + "---------------------");
            }
            Log.i(TAG, "清理进程数量为 : " + count + 1);
        }
    }

    @Override
    public void callAnswer() {
        Log.e(TAG, "system console callAnswer");
        ToastManage.self().show("不支持的指令");
    }

    @Override
    public void callHangup() {
        Log.e(TAG, "system console callHangup");
        ToastManage.self().show("不支持的指令");
    }

    @Override
    public void destroy() {
        Log.d(TAG, "system console destroy");
    }
}