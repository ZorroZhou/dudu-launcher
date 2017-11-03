package com.wow.carlauncher.plugin.music.controllers;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.PopupViewProportion;
import com.wow.carlauncher.plugin.music.MusicController;

import org.xutils.x;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_HOST_ID;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_NCM_POPUP;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_NCM_LANNCHER;

/**
 * Created by 10124 on 2017/10/26.
 */

public class NeteaseCloudMusicPlugin extends MusicController {
    private final static String TAG = "NeteaseCloudMusicPlugin";

    private AppWidgetHost appWidgetHost;
    private AppWidgetManager appWidgetManager;
    private LinearLayout launcherView;
    private LinearLayout popupView;

    public NeteaseCloudMusicPlugin(Context context) {
        super(context);

        appWidgetHost = new AppWidgetHost(context, APP_WIDGET_HOST_ID);
        appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetHost.startListening();

        int popup = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_POPUP, -1);
        int launcher = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_LANNCHER, -1);
        if (launcher == -1 || popup == -1) {
            return;
        }


        // 获取所选的Widget的AppWidgetProviderInfo信息
        AppWidgetProviderInfo launcherWidgetInfo = appWidgetManager.getAppWidgetInfo(launcher);
        final View launcherWidgetView = appWidgetHost.createView(context, launcher, launcherWidgetInfo);
        launcherWidgetView.setPadding(0, 0, 0, 0);
        ergodicLauncherView((ViewGroup) launcherWidgetView);
        ergodicLauncherView2((ViewGroup) launcherWidgetView, 1);

        launcherView = new LinearLayout(context);
        launcherView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams launcherViewLp = new LinearLayout.LayoutParams(launcherWidgetInfo.minWidth - launcherWidgetInfo.minHeight, launcherWidgetInfo.minHeight);
        launcherView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                float s1 = (float) launcherView.getHeight() / (float) launcherWidgetView.getHeight();
                float s2 = (float) launcherView.getWidth() / (float) launcherWidgetView.getWidth();
                if (s1 > s2) {
                    launcherWidgetView.setScaleY(s2);
                    launcherWidgetView.setScaleX(s2);
                } else {
                    launcherWidgetView.setScaleY(s1);
                    launcherWidgetView.setScaleX(s1);
                }
                launcherView.requestLayout();
            }
        });
        launcherView.addView(launcherWidgetView, launcherViewLp);

        AppWidgetProviderInfo popupWidgetInfo = appWidgetManager.getAppWidgetInfo(popup);
        Log.e(TAG, "NeteaseCloudMusicPlugin: " + popupWidgetInfo);
        final View popupWidgetView = appWidgetHost.createView(context, popup, popupWidgetInfo);
        popupWidgetView.setPadding(0, 0, 0, 0);
        ergodicPopupView((ViewGroup) popupWidgetView);

        popupView = new LinearLayout(context);
        popupView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams popupViewLp = new LinearLayout.LayoutParams(200, 100);
        popupView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                float s1 = (float) popupView.getHeight() / (float) popupWidgetView.getHeight();
                float s2 = (float) popupView.getWidth() / (float) popupWidgetView.getWidth();
                if (s1 > s2) {
                    popupWidgetView.setScaleY(s2);
                    popupWidgetView.setScaleX(s2);
                } else {
                    popupWidgetView.setScaleY(s1);
                    popupWidgetView.setScaleX(s1);
                }

                popupView.requestLayout();
            }
        });
        popupView.addView(popupWidgetView, popupViewLp);
    }

    @Override
    public void destroy() {
        super.destroy();
        appWidgetHost.stopListening();
        appWidgetHost = null;
        appWidgetManager = null;
    }

    private View popupV4, popupV5, popupV0;

    //以下是网易云音乐1*4组件的view id和名称的对照
    //app:id/amx 收藏标记
    //app:id/amt 循环标记
    //app:id/amq 封面

    private void ergodicPopupView(ViewGroup vg) {
        //先处理背景
        final ViewGroup bg = (ViewGroup) vg.getChildAt(0);
        if (bg != null) {
            bg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (bg.getBackground() != null) {
                        bg.getBackground().setAlpha(0);
                    }
                    final View cover = bg.getChildAt(0);
                    if (cover != null) {
                        cover.setVisibility(View.GONE);
                    }
                    if (bg.getChildAt(1) instanceof ViewGroup) {
                        ViewGroup vg1 = (ViewGroup) bg.getChildAt(1);
                        if (vg1.getChildAt(2) instanceof ViewGroup) {
                            if (vg1.getChildAt(0) instanceof TextView) {
                                Log.e(TAG, "onGlobalLayout: " + ((TextView) vg1.getChildAt(0)).getCompoundDrawables().length);
                                for (Drawable d : ((TextView) vg1.getChildAt(0)).getCompoundDrawables()) {
                                    if (d != null) {
                                        ((TextView) vg1.getChildAt(0)).setCompoundDrawables(null, null, null, null);
                                        break;
                                    }
                                }
                            }
                            vg1 = (ViewGroup) vg1.getChildAt(2);
                            if (vg1 != null) {
                                popupV0 = vg1.getChildAt(0);
                                if (popupV0 != null) {
                                    popupV0.setVisibility(View.GONE);
                                }
                                popupV4 = vg1.getChildAt(4);
                                if (popupV4 != null) {
                                    popupV4.setVisibility(View.GONE);
                                }
                                popupV5 = vg1.getChildAt(5);
                                if (popupV5 != null) {
                                    popupV5.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
            });


        }

    }

    private void ergodicLauncherView(ViewGroup vg) {
        //先处理背景
        final ViewGroup bg = (ViewGroup) vg.getChildAt(0);
        if (bg != null) {
            bg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (bg.getBackground() != null) {
                        bg.getBackground().setAlpha(0);
                    }
                    final View titleLayout = bg.getChildAt(0);
                    if (titleLayout != null) {
                        titleLayout.setVisibility(View.GONE);
                    }
                    if (bg.getChildAt(1) instanceof ViewGroup) {
                        ViewGroup vg1 = (ViewGroup) bg.getChildAt(1);
                        if (vg1.getChildAt(0) != null) {
                            vg1.getChildAt(0).setVisibility(View.GONE);
                        }

                        if (vg1.getChildAt(1) instanceof ViewGroup) {
                            vg1 = (ViewGroup) vg1.getChildAt(1);
                            if (vg1.getChildAt(3) instanceof ViewGroup) {
                                vg1 = (ViewGroup) vg1.getChildAt(3);
                                if (vg1.getChildAt(0) != null) {
                                    vg1.getChildAt(0).setVisibility(View.GONE);
                                }
                                if (vg1.getChildAt(1) != null) {
                                    vg1.getChildAt(1).setVisibility(View.GONE);
                                }
                                if (vg1.getChildAt(5) != null) {
                                    vg1.getChildAt(5).setVisibility(View.GONE);
                                }
                                if (vg1.getChildAt(6) != null) {
                                    vg1.getChildAt(6).setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private void ergodicLauncherView2(ViewGroup vg, int z) {
        //先处理背景
        for (int i = 0; i < vg.getChildCount(); i++) {
            Log.e(TAG, z + "    ergodicLauncherView: " + vg.getChildAt(i) + "       " + i);
            View v = vg.getChildAt(i);
            if (v instanceof ViewGroup) {
                int zz = z + 1;
                ergodicLauncherView2((ViewGroup) v, zz);
            }
        }
    }

    @Override
    public View getLauncherView() {
        if (launcherView != null) {
            return launcherView;
        }
        return launcherView;
    }

    @Override
    public View getPopupView() {
        if (popupView != null) {
            return popupView;
        }
        return popupView;
    }

    @Override
    public PopupViewProportion getPopupViewProportion() {
        return new PopupViewProportion(1, 2f);
    }
}
