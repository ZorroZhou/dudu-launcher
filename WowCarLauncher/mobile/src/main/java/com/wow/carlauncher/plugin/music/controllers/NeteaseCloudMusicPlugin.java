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
        ergodicLauncherView2((ViewGroup) popupWidgetView, 1);
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
        //先处理背景
        final ViewGroup bg = (ViewGroup) vg.getChildAt(0);
        if (bg != null) {
            bg.getBackground().setAlpha(0);
            bg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (popupAmt != null) {
                        popupAmt.setVisibility(View.GONE);
                    }
                    if (popupAmx != null) {
                        popupAmx.setVisibility(View.GONE);
                    }
                }
            });

            final View cover = bg.getChildAt(0);
            if (cover != null) {
                cover.setVisibility(View.GONE);
            }
            if (bg.getChildAt(1) instanceof ViewGroup) {
                ViewGroup vg1 = (ViewGroup) bg.getChildAt(1);
                if (vg1.getChildAt(2) instanceof ViewGroup) {
                    if (vg1.getChildAt(0) instanceof TextView) {
                        ((TextView) vg1.getChildAt(0)).setCompoundDrawables(null, null, null, null);
                    }
                    vg1 = (ViewGroup) vg1.getChildAt(2);
                    if (vg1 != null) {
                        View v5 = vg1.getChildAt(4);
                        if (v5 != null) {
                            v5.setVisibility(View.GONE);
                            popupAmx = v5;
                        }
                        View v0 = vg1.getChildAt(0);
                        if (v0 != null) {
                            v0.setVisibility(View.GONE);
                            popupAmt = v0;
                        }
                    }
                }
            }
        }

//        for (int i = 0; i < vg.getChildCount(); i++) {
//            View v = vg.getChildAt(i);
//            //背景
//            if (v.toString().indexOf("app:id/amp") > 0) {
//                v.getBackground().setAlpha(0);
//                v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        Log.e(TAG, "onGlobalLayout: ！！！");
//                        launcherAn2.setVisibility(View.GONE);
//                        launcherAmx.setVisibility(View.GONE);
//                    }
//                });
//            }
//            if (v instanceof ViewGroup) {
//                ergodicPopupView((ViewGroup) v);
//            } else {
//                if (v.toString().indexOf("app:id/amq") > 0) {
//                    v.setVisibility(View.GONE);
//                }
//                if (v.toString().indexOf("app:id/amx") > 0) {
//                    v.setVisibility(View.GONE);
//                    popupAmx = v;
//                }
//                if (v.toString().indexOf("app:id/amt") > 0) {
//                    v.setVisibility(View.GONE);
//                    popupAmt = v;
//                }
//                if (v.toString().indexOf("app:id/amr") > 0) {
//                    //监听标题变化，变化之后立马处理布局，隐藏不需要出现的
//                    ((TextView) v).setCompoundDrawables(null, null, null, null);
//                    ((TextView) v).addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                        }
//
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable s) {
//                            x.task().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    popupAmt.setVisibility(View.GONE);
//                                    popupAmx.setVisibility(View.GONE);
//                                }
//                            }, 50);
//                        }
//                    });
//                }
//            }
//        }
    }

    private View launcherAn2, launcherAmx;

    private void ergodicLauncherView(ViewGroup vg) {
        //先处理背景
        final ViewGroup bg = (ViewGroup) vg.getChildAt(0);
        if (bg != null) {
            bg.getBackground().setAlpha(0);
            bg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (launcherAn2 != null) {
                        launcherAn2.setVisibility(View.GONE);
                    }
                    if (launcherAmx != null) {
                        launcherAmx.setVisibility(View.GONE);
                    }
                }
            });

            final View titleLayout = bg.getChildAt(0);
            if (titleLayout != null) {
                titleLayout.setVisibility(View.GONE);
            }
            if (bg.getChildAt(1) instanceof ViewGroup) {
                ViewGroup vg1 = (ViewGroup) bg.getChildAt(1);
                if (vg1.getChildAt(1) instanceof ViewGroup) {
                    vg1 = (ViewGroup) vg1.getChildAt(1);
                    if (vg1.getChildAt(3) instanceof ViewGroup) {
                        vg1 = (ViewGroup) vg1.getChildAt(3);
                        View v0 = vg1.getChildAt(0);
                        if (v0 != null) {
                            v0.setVisibility(View.GONE);
                            launcherAn2 = v0;
                        }
                        View v1 = vg1.getChildAt(1);
                        if (v1 != null) {
                            v1.setVisibility(View.GONE);
                        }
                        View v5 = vg1.getChildAt(5);
                        if (v5 != null) {
                            v5.setVisibility(View.GONE);
                            launcherAmx = v5;
                        }
                        View v6 = vg1.getChildAt(6);
                        if (v6 != null) {
                            v6.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }

    private void ergodicLauncherView2(ViewGroup vg, int z) {
        //先处理背景
        for (int i = 0; i < vg.getChildCount(); i++) {
            Log.e(TAG, z + "    ergodicLauncherView: " + vg.getChildAt(i) + "       " + i);
            View v = vg.getChildAt(i);
            //背景
//            if (v.toString().indexOf("app:id/amp") > 0) {
//                v.getBackground().setAlpha(0);
//                v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        launcherAn2.setVisibility(View.GONE);
//                        launcherAmx.setVisibility(View.GONE);
//                    }
//                });
//            }
            if (v instanceof ViewGroup) {
                int zz = z + 1;
                ergodicLauncherView2((ViewGroup) v, zz);
            } else {
//                if (v.toString().indexOf("app:id/amt") > 0) {
//                    ((ViewGroup) v.getParent()).setVisibility(View.GONE);
//                }
//                if (v.toString().indexOf("app:id/an2") > 0) {
//                    v.setVisibility(View.GONE);
//                    launcherAn2 = v;
//                }
//                if (v.toString().indexOf("app:id/an3") > 0) {
//                    v.setVisibility(View.GONE);
//                }
//                if (v.toString().indexOf("app:id/amx") > 0) {
//                    v.setVisibility(View.GONE);
//                    launcherAmx = v;
//                }
//                if (v.toString().indexOf("app:id/amy") > 0) {
//                    v.setVisibility(View.GONE);
//                    //an3 = v;
//                }
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
        return new PopupViewProportion(1, 2.5F);
    }
}
