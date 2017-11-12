package com.wow.carlauncher.popupWindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
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
import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.PluginManage;

import org.xutils.x;

import static com.wow.carlauncher.common.CommonData.SDATA_POPUP_ALLOW_SHOW;

/**
 * Created by 10124 on 2017/10/29.
 */

public class PopupWin {
    private static final String TAG = "PopupWin";

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
    //打开按钮
    private ImageView iv_open;
    private LinearLayout ll_menu;
    //插件试图
    private LinearLayout plugin;
    //插件布局
    private LinearLayout.LayoutParams pluginlp;
    private int screenWidth = -1;
    private int screenHeight = -1;
    //
    private int currentPluginIndex = -1;
    private final String[] pluginNames = {PluginManage.MUSIC, PluginManage.AMAPCAR};

    private boolean moveing = false;

    public void init(CarLauncherApplication context) {
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
        winparams.width = (int) (screenWidth * 0.15);
        winparams.height = (int) (screenWidth * 0.15);
        winparams.gravity = Gravity.TOP | Gravity.START;
        winparams.x = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_WIN_X, 0);
        winparams.y = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_WIN_Y, 0);

        pluginlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        currentPluginIndex = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_CURRENT_PLUGIN, -1);
        if (currentPluginIndex >= pluginNames.length || currentPluginIndex < -1) {
            currentPluginIndex = -1;
        }

        popupWindow = View.inflate(context, R.layout.popup_window, null);
        plugin = (LinearLayout) popupWindow.findViewById(R.id.ll_plugin);

        iv_open = (ImageView) popupWindow.findViewById(R.id.iv_open);
        iv_open.setOnClickListener(onClickListener);
        iv_open.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                moveing = true;
                return false;
            }
        });
        iv_open.setOnTouchListener(moveTouchListener);

        ll_menu = (LinearLayout) popupWindow.findViewById(R.id.ll_menu);
        ll_menu.setOnClickListener(onClickListener);
        ll_menu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                moveing = true;
                return false;
            }
        });
        ll_menu.setOnTouchListener(moveTouchListener);

        popupWindow.findViewById(R.id.iv_controller).setOnClickListener(onClickListener);

        showPlugin(false);
    }

    private void showPlugin(boolean goNext) {
        BasePlugin iplugin = null;
        while (true) {
            if (goNext) {
                currentPluginIndex = currentPluginIndex + 1;
            }
            if (currentPluginIndex >= pluginNames.length) {
                currentPluginIndex = -1;
            }
            if (currentPluginIndex == -1) {
                break;
            }
            iplugin = PluginManage.self().getByName(pluginNames[currentPluginIndex]);
            if (iplugin != null) {
                break;
            }
        }

        plugin.removeAllViews();

        if (currentPluginIndex == -1) {
            plugin.setVisibility(View.GONE);
            ll_menu.setVisibility(View.GONE);
            iv_open.setVisibility(View.VISIBLE);

            winparams.width = (int) (screenWidth * 0.15);
            winparams.height = (int) (screenWidth * 0.15);
            popupWindow.setBackgroundResource(R.color.popup_hide_plugin);
            if (isShow) {
                wm.updateViewLayout(popupWindow, winparams);
            }
        } else {
            plugin.setVisibility(View.VISIBLE);
            ll_menu.setVisibility(View.VISIBLE);
            iv_open.setVisibility(View.GONE);

            winparams.height = (int) (screenWidth * 0.15);
            winparams.width = (int) (screenWidth * 0.15 / 2 + screenWidth * 0.3);
            plugin.addView(iplugin.getPopupView(), pluginlp);
            popupWindow.setBackgroundResource(R.color.popup_show_plugin);
            if (isShow) {
                wm.updateViewLayout(popupWindow, winparams);
            }
        }

        SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_POPUP_CURRENT_PLUGIN, currentPluginIndex);
    }


    public void checkShowApp(final String app) {
        if (!SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_SHOW_TYPE, true)) {
            final String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_POPUP_SHOW_APPS);
            x.task().autoPost(new Runnable() {
                @Override
                public void run() {
                    if (selectapp.indexOf("[" + app + "]") >= 0) {
                        popupWindow.setVisibility(View.VISIBLE);
                    } else {
                        popupWindow.setVisibility(View.GONE);
                    }
                }
            });
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
        }
    }

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
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_open: {
                    showPlugin(true);
                    break;
                }
                case R.id.ll_menu: {
                    showPlugin(true);
                    break;
                }
                case R.id.iv_controller: {
                    ConsoleWin.self().show();
                    break;
                }

            }
        }
    };

    private View.OnTouchListener moveTouchListener = new View.OnTouchListener() {
        private int tx, ty;
        private int rx, ry;

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                tx = (int) e.getX();
                ty = (int) e.getY();

                rx = (int) e.getRawX();
                ry = (int) e.getRawY();
            } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                if (!moveing) {
                    return false;
                }
                winparams.x = (int) (e.getRawX() - tx);
                winparams.y = (int) (e.getRawY() - ty);
                wm.updateViewLayout(popupWindow, winparams);
                return true;
            } else if (e.getAction() == MotionEvent.ACTION_UP) {
                if (!moveing) {
                    return false;
                }
                moveing = false;
                SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_POPUP_WIN_X, winparams.x);
                SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_POPUP_WIN_Y, winparams.y);
                return true;
            }
            return false;
        }
    };
}
