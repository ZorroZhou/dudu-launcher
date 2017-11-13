package com.wow.carlauncher.popupWindow;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.AppUtil;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.plugin.PluginTypeEnum;

import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

import static com.wow.carlauncher.common.CommonData.*;

public class PopupWin {
    private static PopupWin self;

    public static PopupWin self() {
        if (self == null) {
            self = new PopupWin();
        }
        return self;
    }

    private PopupWin() {

    }

    //窗口管理器
    private WindowManager wm = null;
    //窗口的布局参数
    private WindowManager.LayoutParams winparams;
    //是否展示了
    private Boolean isShow = false;
    private CarLauncherApplication context;
    //窗口视图
    private View popupWindow;
    //插件试图
    private LinearLayout pluginHome;
    //插件布局
    private LinearLayout.LayoutParams pluginlp;
    private int screenWidth = -1;

    public void init(CarLauncherApplication context) {
        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;

        winparams = new WindowManager.LayoutParams();
        // 类型
        winparams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        winparams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        winparams.format = PixelFormat.TRANSLUCENT;
        winparams.width = (int) (screenWidth * 0.15);
        winparams.height = (int) (screenWidth * 0.15);
        winparams.gravity = Gravity.TOP | Gravity.START;
        winparams.x = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_WIN_X, 0);
        winparams.y = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_WIN_Y, 0);

        pluginlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        popupWindow = View.inflate(context, R.layout.popup_window, null);
        pluginHome = (LinearLayout) popupWindow.findViewById(R.id.ll_plugin);

        popupWindow.findViewById(R.id.ll_yidong).setOnTouchListener(moveTouchListener);
        popupWindow.findViewById(R.id.ll_xunhuan).setOnClickListener(onClickListener);
        popupWindow.findViewById(R.id.ll_controller).setOnClickListener(onClickListener);
        popupWindow.findViewById(R.id.ll_home).setOnClickListener(onClickListener);
    }

    private String nowApp = "";

    private void checkShowApp(final String app) {
        //如果APP是空的,则说明用户没有打开权限,则直接不显示了
        if (CommonUtil.isNull(app)) {
            x.task().autoPost(new Runnable() {
                @Override
                public void run() {
                    popupWindow.setVisibility(View.GONE);
                }
            });
        }
        //如果不显示了,或者传进来的app参数是空的
        if (!isShow) {
            return;
        }
        //如果APP没切换,也不用处理了
        if (nowApp.equals(app)) {
            return;
        }
        this.nowApp = app;
        Log.e(TAG, "checkShowApp1: " + nowApp);
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                if (!SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_SHOW_TYPE, true)) {
                    final String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_POPUP_SHOW_APPS);
                    if (selectapp.contains("[" + app + "]")) {
                        popupWindow.setVisibility(View.VISIBLE);
                    } else {
                        popupWindow.setVisibility(View.GONE);
                    }
                }
                if (popupWindow.getVisibility() == View.GONE) {
                    return;
                }
                Integer pluginId = SharedPreUtil.getSharedPreInteger(SDATA_POPUP_CURRENT_PLUGIN + nowApp, -1);
                if (pluginId == -1) {
                    pluginHome.setVisibility(View.GONE);

                    winparams.width = (int) (screenWidth * 0.15);
                    winparams.height = (int) (screenWidth * 0.15);
                    if (isShow) {
                        wm.updateViewLayout(popupWindow, winparams);
                    }
                } else {
                    showPlugin(false);
                }
            }
        });
    }

    private void showPlugin(boolean goNext) {
        Integer pluginId = SharedPreUtil.getSharedPreInteger(SDATA_POPUP_CURRENT_PLUGIN + nowApp, -1);
        PluginTypeEnum pluginType = PluginManage.self().getPopupPlugin(nowApp, pluginId, goNext);
        if (pluginType == null) {
            pluginHome.setVisibility(View.GONE);

            winparams.width = (int) (screenWidth * 0.15);
            winparams.height = (int) (screenWidth * 0.15);
            if (isShow) {
                wm.updateViewLayout(popupWindow, winparams);
            }
            SharedPreUtil.saveSharedPreInteger(SDATA_POPUP_CURRENT_PLUGIN + nowApp, -1);
        } else {
            pluginHome.setVisibility(View.VISIBLE);

            winparams.height = (int) (screenWidth * 0.15);
            winparams.width = (int) (screenWidth * 0.15 + screenWidth * 0.3);
            if (isShow) {
                wm.updateViewLayout(popupWindow, winparams);
            }

            BasePlugin plugin = PluginManage.self().getPlugin(pluginType);
            pluginHome.removeAllViews();
            pluginHome.addView(plugin.getPopupView(), pluginlp);
            SharedPreUtil.saveSharedPreInteger(SDATA_POPUP_CURRENT_PLUGIN + nowApp, pluginType.getId());
        }
    }

    public synchronized void checkShow(int count) {
        if (context.checkActivity(count) > 0) {
            hide();
        } else {
            show();
        }
    }

    //隐藏方法
    private void hide() {
        if (isShow) {
            wm.removeView(popupWindow);
            isShow = false;

            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }

    private Timer timer;

    //显示方法
    private void show() {
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

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkShowApp(AppUtil.getForegroundApp(context));
            }
        }, 500 - System.currentTimeMillis() % 500, 500);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_xunhuan: {
                    showPlugin(true);
                    break;
                }
                case R.id.ll_controller: {
                    ConsoleWin.self().show();
                    break;
                }
                case R.id.ll_home: {
                    Intent home = new Intent(Intent.ACTION_MAIN);
                    home.addCategory(Intent.CATEGORY_HOME);
                    home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(home);
                    break;
                }
            }
        }
    };

    private View.OnTouchListener moveTouchListener = new View.OnTouchListener() {
        private int tx, ty;

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                tx = (int) e.getX();
                ty = (int) e.getY();
                return true;
            } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                winparams.x = (int) (e.getRawX() - tx);
                winparams.y = (int) (e.getRawY() - ty);
                wm.updateViewLayout(popupWindow, winparams);
                return true;
            } else if (e.getAction() == MotionEvent.ACTION_UP) {
                SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_POPUP_WIN_X, winparams.x);
                SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_POPUP_WIN_Y, winparams.y);
                return true;
            }
            return false;
        }
    };
}
