package com.wow.carlauncher.popupWindow;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.R;
import com.wow.carlauncher.activity.LockActivity;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.plugin11.console.ConsolePlugin;
import com.wow.frame.util.SharedPreUtil;

/**
 * Created by 10124 on 2017/11/7.
 */

public class ConsoleWin implements View.OnClickListener {
    private static ConsoleWin self;

    public static ConsoleWin self() {
        if (self == null) {
            self = new ConsoleWin();
        }
        return self;
    }

    private ConsoleWin() {

    }

    //窗口管理器
    private WindowManager wm = null;
    private WindowManager.LayoutParams winparams;
    //窗口的布局参数
    private CarLauncherApplication context;
    //窗口视图
    private View popupWindow;
    private boolean isShow = false;

    public void init(CarLauncherApplication context) {
        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        winparams = new WindowManager.LayoutParams();
        // 类型
        winparams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        winparams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        winparams.format = PixelFormat.TRANSLUCENT;
        winparams.width = outMetrics.widthPixels;
        winparams.height = outMetrics.heightPixels;
        winparams.gravity = Gravity.TOP | Gravity.START;
        winparams.x = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_WIN_X, 0);
        winparams.y = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_WIN_Y, 0);

        popupWindow = View.inflate(context, R.layout.popup_controller, null);
        popupWindow.findViewById(R.id.popup_bg).setOnClickListener(this);
        popupWindow.findViewById(R.id.btn_close_screen).setOnClickListener(this);
        popupWindow.findViewById(R.id.btn_jy).setOnClickListener(this);
        popupWindow.findViewById(R.id.btn_vu).setOnClickListener(this);
        popupWindow.findViewById(R.id.btn_vd).setOnClickListener(this);
    }

    //隐藏方法
    private void hide() {
        if (isShow) {
            wm.removeView(popupWindow);
            isShow = false;
        }
    }

    //显示方法
    public void show() {
        if (!isShow) {
            wm.addView(popupWindow, winparams);
            isShow = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_bg: {
                hide();
                break;
            }
            case R.id.btn_close_screen: {
                hide();
                Intent intent = new Intent(context, LockActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            }
            case R.id.btn_vu: {
                ConsolePlugin.self().incVolume();
                break;
            }
            case R.id.btn_vd: {
                ConsolePlugin.self().decVolume();
                break;
            }
            case R.id.btn_jy: {
                ConsolePlugin.self().mute();
                break;
            }
        }
    }
}
