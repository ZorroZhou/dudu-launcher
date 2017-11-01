package com.wow.carlauncher.plugin.music.controllers;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
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
            View v = vg.getChildAt(i);
            //背景
            if (v.toString().indexOf("app:id/amp") > 0) {
                v.getBackground().setAlpha(0);
            }
            if (v instanceof ViewGroup) {
                ergodicPopupView((ViewGroup) v);
            } else {
                if (v.toString().indexOf("app:id/amq") > 0) {
                    v.setVisibility(View.GONE);
                }
                if (v.toString().indexOf("app:id/amx") > 0) {
                    v.setVisibility(View.GONE);
                    popupAmx = v;
                }
                if (v.toString().indexOf("app:id/amt") > 0) {
                    v.setVisibility(View.GONE);
                    popupAmt = v;
                }
                if (v.toString().indexOf("app:id/amr") > 0) {
                    //监听标题变化，变化之后立马处理布局，隐藏不需要出现的
                    ((TextView) v).setCompoundDrawables(null, null, null, null);
                    ((TextView) v).addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            x.task().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    popupAmt.setVisibility(View.GONE);
                                    popupAmx.setVisibility(View.GONE);
                                }
                            }, 50);
                        }
                    });
                }
            }
        }
    }

    private View launcherAn2, launcherAmx;
    //以下是网易云音乐2*4组件的view id和名称的对照
    //app:id/amt  换肤
    //app:id/amz  搜索
    //app:id/amq  1*4的封面
    //app:id/an2 循环方式
    //app:id/amx 收藏标记
    //app:id/an3 下方的线
    //app:id/amv 上一首的id
    //app:id/amw 下一首
    //app:id/amu 播放按钮
    //app:id/amr 歌曲名称的id
    //app:id/an0 作者的
    //app:id/amq 封面的id

    private void ergodicLauncherView(ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);
            //背景
            if (v.toString().indexOf("app:id/amp") > 0) {
                v.getBackground().setAlpha(0);
            }
            if (v instanceof ViewGroup) {
                ergodicLauncherView((ViewGroup) v);
            } else {
                if (v.toString().indexOf("app:id/amt") > 0) {
                    ((ViewGroup) v.getParent()).setVisibility(View.GONE);
                }
                if (v.toString().indexOf("app:id/an2") > 0) {
                    v.setVisibility(View.GONE);
                    launcherAn2 = v;
                }
                if (v.toString().indexOf("app:id/an3") > 0) {
                    v.setVisibility(View.GONE);
                }
                if (v.toString().indexOf("app:id/amx") > 0) {
                    v.setVisibility(View.GONE);
                    launcherAmx = v;
                }
                if (v.toString().indexOf("app:id/amy") > 0) {
                    v.setVisibility(View.GONE);
                    //an3 = v;
                }
                if (v.toString().indexOf("app:id/amr") > 0) {
                    //监听标题变化，变化之后立马处理布局，隐藏不需要出现的
                    ((TextView) v).addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            x.task().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    launcherAn2.setVisibility(View.GONE);
                                    launcherAmx.setVisibility(View.GONE);
                                }
                            }, 100);
                        }
                    });
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

    @Override
    public PopupViewProportion getPopupViewProportion() {
        return new PopupViewProportion(1, 2);
    }
}
