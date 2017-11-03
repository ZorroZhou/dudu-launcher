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

public class PopupWindow {
    private static final String TAG = "PopupWindow";

    private static PopupWindow self;

    public static PopupWindow self() {
        if (self == null) {
            self = new PopupWindow();
        }
        return self;
    }

    private PopupWindow() {

    }


    //窗口管理器
    private WindowManager wm = null;
    //窗口的布局参数
    private WindowManager.LayoutParams winparams;
    //是否展示了
    private Boolean isShow = Boolean.valueOf(false);
    private CarLauncherApplication context;
    //窗口视图
    private View popupWindow;
    //打开按钮
    private ImageView iv_open;
    //插件试图
    private LinearLayout plugin;
    //插件布局
    private LinearLayout.LayoutParams pluginlp;
    private int screenWidth = -1;
    private int screenHeight = -1;
    //
    private int currentPluginIndex = -1;
    private final String[] pluginNames = {PluginManage.MUSIC, PluginManage.TIME};

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

        currentPluginIndex = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_CURRENT_PLUGIN, -1);
        if (currentPluginIndex >= pluginNames.length || currentPluginIndex < -1) {
            currentPluginIndex = -1;
        }

        popupWindow = View.inflate(context, R.layout.popup_window, null);
        plugin = popupWindow.findViewById(R.id.ll_plugin);
        iv_open = popupWindow.findViewById(R.id.iv_open);

        pluginlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        iv_open.setOnClickListener(openOnClickListener);
        iv_open.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                moveing = true;
                return false;
            }
        });
        iv_open.setOnTouchListener(new View.OnTouchListener() {
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
//                    if (Math.abs(e.getRawX() - rx) < 10 && Math.abs(e.getRawY() - ry) < 10) {
//                        return false;
//                    }
                    winparams.x = (int) (e.getRawX() - tx);
                    winparams.y = (int) (e.getRawY() - ty);
                    wm.updateViewLayout(popupWindow, winparams);
                    return true;
                } else if (e.getAction() == MotionEvent.ACTION_UP) {
                    if (!moveing) {
                        return false;
                    }
//                    if (Math.abs(e.getRawX() - rx) < 10 && Math.abs(e.getRawY() - ry) < 10) {
//                        return false;
//                    }
                    moveing = false;
//                    int cx = winparams.x + popupWindow.getWidth() / 2;
//                    int cy = winparams.y + popupWindow.getHeight() / 2;
//                    Log.e(TAG, "onTouch: " + cx + "--" + cy);
//
//                    if (cx < screenWidth / 2 && cy < screenHeight / 2) {
//                        if (cx < cy) {
//                            stopMoveByX();
//                        } else {
//                            stopMoveByY();
//                        }
//                    } else if (cx > screenWidth / 2 && cy < screenHeight / 2) {
//                        if ((screenWidth - cx) < cy) {
//                            stopMoveByX();
//                        } else {
//                            stopMoveByY();
//                        }
//                    } else if (cx < screenWidth / 2 && cy > screenHeight / 2) {
//                        if (cx < (screenHeight - cy)) {
//                            stopMoveByX();
//                        } else {
//                            stopMoveByY();
//                        }
//                    } else {
//                        if ((screenWidth - cx) < (screenHeight - cy)) {
//                            stopMoveByX();
//                        } else {
//                            stopMoveByY();
//                        }
//                    }
                    SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_POPUP_WIN_X, winparams.x);
                    SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_POPUP_WIN_Y, winparams.y);
                    return true;
                }
                return false;
            }
        });
        Log.e(TAG, "init: " + currentPluginIndex);
        showPlugin(false);
    }

    //根据X轴停靠
    private void stopMoveByX() {
        if (winparams.x + popupWindow.getWidth() / 2 < screenWidth / 2) {
            winparams.x = 0;
            wm.updateViewLayout(popupWindow, winparams);
        } else {
            winparams.x = screenWidth - popupWindow.getWidth();
            wm.updateViewLayout(popupWindow, winparams);
        }
    }

    //根据Y轴停靠
    private void stopMoveByY() {
        if (winparams.y + popupWindow.getHeight() / 2 < screenHeight / 2) {
            winparams.y = 0;
            wm.updateViewLayout(popupWindow, winparams);
        } else {
            winparams.y = screenHeight - popupWindow.getHeight();
            wm.updateViewLayout(popupWindow, winparams);
        }
    }

    private void showPlugin(boolean goNext) {
        IPlugin iplugin = null;
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
            iplugin = PluginManage.getByName(pluginNames[currentPluginIndex]);
            if (iplugin != null && iplugin.getPopupViewProportion() != null) {
                break;
            }
        }

        plugin.removeAllViews();

        if (currentPluginIndex == -1) {
            plugin.setVisibility(View.GONE);
            winparams.width = (int) (screenWidth * 0.15);
            winparams.height = (int) (screenWidth * 0.15);
            popupWindow.setBackgroundResource(R.color.popup_hide_plugin);
            if (isShow) {
                wm.updateViewLayout(popupWindow, winparams);
            }
        } else {
            winparams.height = (int) (screenWidth * 0.15);
            winparams.width = (int) (screenWidth * 0.15 * iplugin.getPopupViewProportion().w / iplugin.getPopupViewProportion().h + screenWidth * 0.15);
            plugin.setVisibility(View.VISIBLE);
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

    private View.OnClickListener openOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPlugin(true);
        }
    };
}
