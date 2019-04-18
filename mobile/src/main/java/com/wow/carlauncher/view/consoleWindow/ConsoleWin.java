package com.wow.carlauncher.view.consoleWindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.frame.util.SharedPreUtil;

public class ConsoleWin {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static ConsoleWin instance = new ConsoleWin();
    }

    public static ConsoleWin self() {
        return ConsoleWin.SingletonHolder.instance;
    }

    private ConsoleWin() {

    }

    //窗口管理器
    private WindowManager wm;
    //窗口的布局参数
    private WindowManager.LayoutParams winparams;
    //是否展示了
    private Boolean isShow = false;
    private CarLauncherApplication context;
    //窗口视图
    private View consoleWin;
    //插件试图
    private LinearLayout pluginHome;
    private TextView tv_time;

    public void init(CarLauncherApplication context) {

        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //EventBus.getDefault().register(this);

        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);


        winparams = new WindowManager.LayoutParams();
        // 类型
        winparams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        if (SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, true)) {
            winparams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        } else {
            winparams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        winparams.format = PixelFormat.TRANSLUCENT;
        winparams.width = outMetrics.widthPixels / 2;
        winparams.height = outMetrics.heightPixels / 4;

        winparams.gravity = Gravity.TOP | Gravity.START;
        winparams.x = (outMetrics.widthPixels - winparams.width) / 2;
        winparams.y = outMetrics.heightPixels - winparams.height;

        consoleWin = View.inflate(context, R.layout.console_window, null);
    }

    public void show() {
        if (!isShow) {
            wm.addView(consoleWin, winparams);
            isShow = true;
        }
    }

    public void hide() {
        if (isShow) {
            wm.removeView(consoleWin);
            isShow = false;
        }
    }
}
