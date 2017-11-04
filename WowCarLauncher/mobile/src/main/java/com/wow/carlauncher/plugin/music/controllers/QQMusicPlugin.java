package com.wow.carlauncher.plugin.music.controllers;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.PopupViewProportion;
import com.wow.carlauncher.plugin.music.MusicController;
import com.wow.carlauncher.plugin.music.MusicPlugin;

import org.json.JSONObject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_HOST_ID;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_QQMUSIC_LANNCHER;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_QQMUSIC_POPUP;

/**
 * Created by 10124 on 2017/10/26.
 */

public class QQMusicPlugin extends MusicController {
    private final static String TAG = "QQMusicPlugin";

    private AppWidgetHost appWidgetHost;
    private AppWidgetManager appWidgetManager;
    private LinearLayout launcherView;
    private LinearLayout popupView;

    public QQMusicPlugin(Context context) {
        super(context);

        appWidgetHost = new AppWidgetHost(context, APP_WIDGET_HOST_ID);
        appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetHost.startListening();

        int popup = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_POPUP, -1);
        int launcher = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_LANNCHER, -1);
        if (launcher == -1 || popup == -1) {
            return;
        }


        // 获取所选的Widget的AppWidgetProviderInfo信息
        AppWidgetProviderInfo launcherWidgetInfo = appWidgetManager.getAppWidgetInfo(launcher);
        View launcherWidgetView = appWidgetHost.createView(context, launcher, launcherWidgetInfo);
        launcherWidgetView.setScaleY(2);
        launcherWidgetView.setScaleX(2);
        launcherWidgetView.setPadding(0, 0, 0, 0);
        ergodicLauncherView((ViewGroup) launcherWidgetView);

        launcherView = new LinearLayout(context);
        launcherView.setGravity(Gravity.CENTER);
        final LinearLayout.LayoutParams launcherViewLp = new LinearLayout.LayoutParams(300, 300);
        launcherView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                launcherViewLp.width = launcherView.getWidth() / 2;
                launcherViewLp.height = launcherView.getHeight() / 2;
                launcherView.requestLayout();
            }
        });
        launcherView.addView(launcherWidgetView, launcherViewLp);

        AppWidgetProviderInfo popupWidgetInfo = appWidgetManager.getAppWidgetInfo(popup);
        View popupWidgetView = appWidgetHost.createView(context, popup, popupWidgetInfo);
        popupWidgetView.setScaleY(2);
        popupWidgetView.setScaleX(2);
        popupWidgetView.setPadding(0, 0, 0, 0);
        ergodicPopupView((ViewGroup) popupWidgetView);
        popupView = new LinearLayout(context);
        popupView.setGravity(Gravity.CENTER);
        final LinearLayout.LayoutParams popupViewLp = new LinearLayout.LayoutParams(300, 300);
        popupView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                popupViewLp.width = popupView.getWidth() / 2;
                popupViewLp.height = popupView.getHeight() / 2;
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

    private View popupAmt, popupAmx;

    //以下是网易云音乐1*4组件的view id和名称的对照
    //app:id/amx 收藏标记
    //app:id/amt 循环标记
    //app:id/amq 封面

    private void ergodicPopupView(ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            Log.e("~!~!!!!!!!!!!!!!", "" + vg.getChildAt(i));
            View v = vg.getChildAt(i);
            //背景
            if (v.toString().indexOf("app:id/u2") > 0) {
                v.getBackground().setAlpha(0);
            }
            if (v instanceof ViewGroup) {
                ergodicPopupView((ViewGroup) v);
            } else {
                if (v.toString().indexOf("app:id/th") > 0) {
                    v.setVisibility(View.GONE);
                }
                if (v.toString().endsWith("0,0-0,0}") && v.toString().indexOf("ImageView") > 0) {
                    v.setVisibility(View.GONE);
                }
                if (v.toString().indexOf("app:id/u5") > 0) {
                    v.setVisibility(View.GONE);
                }
                if (v.toString().indexOf("app:id/to") > 0) {
                    ViewGroup.LayoutParams lp = v.getLayoutParams();
                    lp.width = 0;
                    v.setLayoutParams(lp);
                }
                if (v.toString().indexOf("app:id/tp") > 0) {
                    ViewGroup.LayoutParams lp = v.getLayoutParams();
                    lp.width = 0;
                    v.setLayoutParams(lp);
                }
            }
        }
    }

    //以下是QQ音乐2*4组件的view id和名称的对照
    //app:id/u1  换肤
    //v.toString().endsWith("0,0-0,0}")&&v.toString().indexOf("ImageView") > 0 qq音乐标志的判断
    //app:id/th  2*4的封面
    //app:id/to 循环方式

    private void ergodicLauncherView(ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            //Log.e("~!~!!!!!!!!!!!!!", "" + vg.getChildAt(i));
            View v = vg.getChildAt(i);
            //背景
            if (v.toString().indexOf("app:id/tz") > 0) {
                v.getBackground().setAlpha(0);
            }
            if (v instanceof ViewGroup) {
                ergodicLauncherView((ViewGroup) v);
            } else {
                if (v.toString().indexOf("app:id/u1") > 0) {
                    v.setVisibility(View.GONE);
                }
                if (v.toString().endsWith("0,0-0,0}") && v.toString().indexOf("ImageView") > 0) {
                    v.setVisibility(View.GONE);
                }
                if (v.toString().indexOf("app:id/to") > 0) {
                    v.setVisibility(View.GONE);
                }
                if (v.toString().indexOf("app:id/tp") > 0) {
                    v.setVisibility(View.GONE);
                }
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
}
