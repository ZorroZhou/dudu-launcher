package com.wow.carlauncher.popupWindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.IPlugin;
import com.wow.carlauncher.plugin.PluginManage;

import org.xutils.x;

import static com.wow.carlauncher.common.CommonData.SDATA_POPUP_ALLOW_SHOW;

/**
 * Created by 10124 on 2017/10/29.
 */

public class LoadWin {
    private static final String TAG = "PopupWin";
    private CarLauncherApplication context;
    private WindowManager wm = null;
    //窗口的布局参数
    private WindowManager.LayoutParams winparams;
    private int screenWidth = -1;
    private int screenHeight = -1;
    private Boolean isShow = false;
    //窗口视图
    private ImageView popupWindow;

    public LoadWin(CarLauncherApplication context) {
        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;

        winparams = new WindowManager.LayoutParams();
        // 类型
        winparams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        winparams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        winparams.format = PixelFormat.TRANSLUCENT;
        winparams.width = screenWidth;
        winparams.height = screenHeight;
        winparams.gravity = Gravity.TOP | Gravity.START;
        winparams.x = 0;
        winparams.y = 0;


        popupWindow = new ImageView(context);
        popupWindow.setImageResource(R.mipmap.background);
    }

    //隐藏方法
    public void hide() {
        if (isShow) {
            wm.removeView(popupWindow);
            isShow = false;
        }
    }

    //显示方法
    public void show() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(context)) {
            return;
        }
        if (!SharedPreUtil.getSharedPreBoolean(SDATA_POPUP_ALLOW_SHOW, true)) {
            return;
        }
        if (!isShow) {
            wm.addView(popupWindow, winparams);
            isShow = true;
        }
    }
}
