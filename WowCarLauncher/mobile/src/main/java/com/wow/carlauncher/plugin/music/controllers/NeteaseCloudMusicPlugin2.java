package com.wow.carlauncher.plugin.music.controllers;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.PopupViewProportion;
import com.wow.carlauncher.plugin.music.MusicController;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_HOST_ID;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_NCM_LANNCHER;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_NCM_POPUP;

/**
 * Created by 10124 on 2017/10/26.
 */

public class NeteaseCloudMusicPlugin2 extends MusicController {
    private final static String TAG = "NeteaseCloudMusicPlugin";

    private AppWidgetHost appWidgetHost;
    private AppWidgetManager appWidgetManager;
    private RelativeLayout launcherView;
    private LinearLayout launcherHouse;
    private ImageView launcherCover, launcherPlay;
    private TextView launcherTitle, launcherTime, launcherArtist;
    private ProgressBar launcherProgress;
    private long launcherPlayClickTime = -1;

    private LinearLayout popupView;

    private boolean isruning = false;

    public NeteaseCloudMusicPlugin2(Context context) {
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

        launcherView = (RelativeLayout) View.inflate(context, R.layout.plugin_music_ncm_lanncher, null);
        launcherHouse = launcherView.findViewById(R.id.ll_house);
        launcherHouse.addView(launcherWidgetView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        launcherCover = launcherView.findViewById(R.id.iv_cover);
        launcherTitle = launcherView.findViewById(R.id.tv_title);
        launcherTime = launcherView.findViewById(R.id.tv_time);
        launcherArtist = launcherView.findViewById(R.id.tv_artist);
        launcherProgress = launcherView.findViewById(R.id.pb_music);
        launcherPlay = launcherView.findViewById(R.id.iv_play);
        launcherPlay.setOnClickListener(launcherOnClickListener);
        launcherView.findViewById(R.id.iv_prew).setOnClickListener(launcherOnClickListener);
        launcherView.findViewById(R.id.iv_next).setOnClickListener(launcherOnClickListener);

        ergodicLauncherView((ViewGroup) launcherWidgetView);

        AppWidgetProviderInfo popupWidgetInfo = appWidgetManager.getAppWidgetInfo(popup);
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

    private View.OnClickListener launcherOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_prew: {
                    if (launcherWidgetViewPrew != null) {
                        launcherWidgetViewPrew.performClick();
                    }
                    launcherPlayClickTime = System.currentTimeMillis();
                    if (!isruning) {
                        launcherPlay.setImageResource(R.mipmap.pause);
                        isruning = true;
                    }
                    break;
                }
                case R.id.iv_play: {
                    if (launcherWidgetViewPlay != null) {
                        launcherWidgetViewPlay.performClick();
                    }
                    launcherPlayClickTime = System.currentTimeMillis();
                    if (!isruning) {
                        launcherPlay.setImageResource(R.mipmap.pause);
                        isruning = true;
                    } else {
                        launcherPlay.setImageResource(R.mipmap.play);
                        isruning = false;
                    }
                    break;
                }
                case R.id.iv_next: {
                    if (launcherWidgetViewNext != null) {
                        launcherWidgetViewNext.performClick();
                    }
                    launcherPlayClickTime = System.currentTimeMillis();
                    if (!isruning) {
                        launcherPlay.setImageResource(R.mipmap.pause);
                        isruning = true;
                    }
                    break;
                }
            }
        }
    };

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

    private ImageView launcherWidgetViewPrew, launcherWidgetViewNext, launcherWidgetViewPlay;
    private String launcherWidgetViewTimeLastUpdateValue = null;
    private long launcherWidgetViewProgressLastUpdateTime = -1;

    private void ergodicLauncherView(final ViewGroup vg) {
        //先处理背景
        final ViewGroup bg = (ViewGroup) vg.getChildAt(0);
        View v1 = getDeepView(bg, new int[]{1, 0});
        if (v1 instanceof ImageView) {
            final ImageView iv = (ImageView) v1;
            iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    launcherCover.setImageDrawable(iv.getDrawable());
                }
            });
        }
        View v2 = getDeepView(bg, new int[]{1, 1, 0});
        if (v2 instanceof TextView) {
            final TextView tv = (TextView) v2;
            tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    launcherTitle.setText(tv.getText());
                }
            });
        }
        View v3 = getDeepView(bg, new int[]{1, 1, 1, 0});
        if (v3 instanceof TextView) {
            final TextView tv = (TextView) v3;
            tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    launcherArtist.setText(tv.getText());
                }
            });
        }

        View v4 = getDeepView(bg, new int[]{1, 1, 1, 1});
        if (v4 instanceof TextView) {
            final TextView tv = (TextView) v4;
            tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Log.e(TAG, "时间: " + tv.getText());
                    if (System.currentTimeMillis() - launcherWidgetViewProgressLastUpdateTime < 1000) {
                        return;
                    }
                    launcherWidgetViewProgressLastUpdateTime = System.currentTimeMillis();

                    if (System.currentTimeMillis() - launcherPlayClickTime > 2000) {
                        if (!tv.getText().equals(launcherWidgetViewTimeLastUpdateValue)) {
                            launcherPlay.setImageResource(R.mipmap.pause);
                            isruning = true;
                        } else {
                            launcherPlay.setImageResource(R.mipmap.play);
                            isruning = false;
                        }
                    }
                    launcherWidgetViewTimeLastUpdateValue = tv.getText().toString();

                    launcherTime.setText(tv.getText());
                }
            });
        }
        View v5 = getDeepView(bg, new int[]{1, 1, 2});
        if (v5 instanceof ProgressBar) {
            final ProgressBar pb = (ProgressBar) v5;
            pb.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    launcherProgress.setMax(pb.getMax());
                    launcherProgress.setProgress(pb.getProgress());
                }
            });
        }
        View v6 = getDeepView(bg, new int[]{1, 1, 3, 3});
        if (v6 instanceof ImageView) {
            launcherWidgetViewPrew = (ImageView) v6;
        }

        View v7 = getDeepView(bg, new int[]{1, 1, 3, 4});
        if (v7 instanceof ImageView) {
            launcherWidgetViewNext = (ImageView) v7;
        }

        View v8 = getDeepView(bg, new int[]{1, 1, 3, 2});
        if (v8 instanceof ImageView) {
            launcherWidgetViewPlay = (ImageView) v8;
        }
    }

    private View getDeepView(View view, int[] deeps) {
        View r = null;
        for (int i = 0; i < deeps.length; i++) {
            int deep = deeps[i];
            if (view instanceof ViewGroup) {
                view = ((ViewGroup) view).getChildAt(deep);
            }
            if (deep != deeps.length - 1) {
                if (view == null) {
                    return null;
                }
            }
        }
        r = view;
        return r;
    }

    private void ergodicLauncherView2(ViewGroup vg, int z) {
        //先处理背景
        for (int i = 0; i < vg.getChildCount(); i++) {
            //if (z < 4) {
            Log.e(TAG, z + "    ergodicLauncherView: " + vg.getChildAt(i) + "       " + i);
            //}

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
