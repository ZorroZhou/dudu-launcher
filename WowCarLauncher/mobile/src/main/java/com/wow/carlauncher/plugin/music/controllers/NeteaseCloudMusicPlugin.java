package com.wow.carlauncher.plugin.music.controllers;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.plugin.music.MusicController;

import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_HOST_ID;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_NCM_LANNCHER;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_NCM_POPUP;

/**
 * Created by 10124 on 2017/10/26.
 */

public class NeteaseCloudMusicPlugin extends MusicController {
    private final static String TAG = "NeteaseCloudMusicPlugin";

    private AppWidgetHost appWidgetHost;
    private AppWidgetManager appWidgetManager;

    private RelativeLayout launcherView;
    private LinearLayout launcherHouse;
    private ImageView launcherCover, launcherPlay;
    private TextView launcherTitle, launcherTime, launcherArtist;
    private ProgressBar launcherProgress;


    private RelativeLayout popupView;
    private LinearLayout popupHouse;
    private ImageView popupPlay;
    private TextView popupTitle;
    private ProgressBar popupProgress;

    private long playClickTime = -1;
    private boolean isruning = false;

    private Timer timer;

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

        launcherView = (RelativeLayout) View.inflate(context, R.layout.plugin_music_ncm_launcher, null);
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

        popupView = (RelativeLayout) View.inflate(context, R.layout.plugin_music_ncm_popup, null);
        popupHouse = popupView.findViewById(R.id.ll_house);
        popupHouse.addView(popupWidgetView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        popupTitle = popupView.findViewById(R.id.tv_title);
        popupProgress = popupView.findViewById(R.id.pb_music);
        popupPlay = popupView.findViewById(R.id.iv_play);
        popupPlay.setOnClickListener(popupOnClickListener);
        popupView.findViewById(R.id.iv_prew).setOnClickListener(popupOnClickListener);
        popupView.findViewById(R.id.iv_next).setOnClickListener(popupOnClickListener);

        ergodicPopupView((ViewGroup) popupWidgetView);
        startUpdate();
    }

    private void startUpdate() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updatePopupView();
                updateLauncherView();
            }
        }, 0, 500);
    }

    private void stopUpdate() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        stopUpdate();
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
                    playClickTime = System.currentTimeMillis();
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
                    playClickTime = System.currentTimeMillis();
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
                    playClickTime = System.currentTimeMillis();
                    if (!isruning) {
                        launcherPlay.setImageResource(R.mipmap.pause);
                        isruning = true;
                    }
                    break;
                }
            }
        }
    };

    private View.OnClickListener popupOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_prew: {
                    if (popupWidgetViewPrew != null) {
                        popupWidgetViewPrew.performClick();
                    }
                    playClickTime = System.currentTimeMillis();
                    if (!isruning) {
                        popupPlay.setImageResource(R.mipmap.pause);
                        isruning = true;
                    }
                    break;
                }
                case R.id.iv_play: {
                    if (popupWidgetViewPlay != null) {
                        popupWidgetViewPlay.performClick();
                    }
                    playClickTime = System.currentTimeMillis();
                    if (!isruning) {
                        popupPlay.setImageResource(R.mipmap.pause);
                        isruning = true;
                    } else {
                        popupPlay.setImageResource(R.mipmap.play);
                        isruning = false;
                    }
                    break;
                }
                case R.id.iv_next: {
                    if (popupWidgetViewNext != null) {
                        popupWidgetViewNext.performClick();
                    }
                    playClickTime = System.currentTimeMillis();
                    if (!isruning) {
                        popupPlay.setImageResource(R.mipmap.pause);
                        isruning = true;
                    }
                    break;
                }
            }
        }
    };

    private TextView popupWidgetViewTitle;
    private ProgressBar popupWidgetViewProgressBar;
    private ImageView popupWidgetViewPrew, popupWidgetViewNext, popupWidgetViewPlay;
    private int popupWidgetViewTimeLastUpdateValue = -1;
    private int popupChangeTime = 0;

    //以下是网易云音乐1*4组件的view id和名称的对照
    //app:id/amx 收藏标记
    //app:id/amt 循环标记
    //app:id/amq 封面
    private void updatePopupView() {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                if (popupWidgetViewTitle != null) {
                    popupTitle.setText(popupWidgetViewTitle.getText());
                }

                if (popupWidgetViewProgressBar != null) {
                    popupProgress.setMax(popupWidgetViewProgressBar.getMax());
                    popupProgress.setProgress(popupWidgetViewProgressBar.getProgress());
                }


                if (System.currentTimeMillis() - playClickTime > 2000) {
                    if (popupProgress.getProgress() != popupWidgetViewTimeLastUpdateValue) {
                        popupChangeTime = 0;
                        popupPlay.setImageResource(R.mipmap.pause);
                        isruning = true;
                    } else {
                        popupChangeTime++;
                        if (popupChangeTime > 2) {
                            popupPlay.setImageResource(R.mipmap.play);
                            isruning = false;
                        }
                    }
                    popupWidgetViewTimeLastUpdateValue = popupProgress.getProgress();
                }
            }
        });
    }

    private void ergodicPopupView(ViewGroup vg) {
        //先处理背景
        final ViewGroup bg = (ViewGroup) vg.getChildAt(0);

        View v2 = ViewUtils.getDeepViewByIndex(bg, new int[]{1, 0});
        if (v2 instanceof TextView) {
            popupWidgetViewTitle = (TextView) v2;
            popupTitle.setText(popupWidgetViewTitle.getText());
        }

        View v5 = ViewUtils.getDeepViewByIndex(bg, new int[]{1, 1});
        if (v5 instanceof ProgressBar) {
            popupWidgetViewProgressBar = (ProgressBar) v5;
            popupProgress.setMax(popupWidgetViewProgressBar.getMax());
            popupProgress.setProgress(popupWidgetViewProgressBar.getProgress());
        }
        View v6 = ViewUtils.getDeepViewByIndex(bg, new int[]{1, 2, 2});
        if (v6 instanceof ImageView) {
            popupWidgetViewPrew = (ImageView) v6;
        }

        View v7 = ViewUtils.getDeepViewByIndex(bg, new int[]{1, 2, 3});
        if (v7 instanceof ImageView) {
            popupWidgetViewNext = (ImageView) v7;
        }

        View v8 = ViewUtils.getDeepViewByIndex(bg, new int[]{1, 2, 1});
        if (v8 instanceof ImageView) {
            popupWidgetViewPlay = (ImageView) v8;
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
    private TextView launcherWidgetViewTitle, launcherWidgetViewArtist, launcherWidgetViewTime;
    private ProgressBar launcherWidgetViewProgressBar;
    private ImageView launcherWidgetViewPrew, launcherWidgetViewNext, launcherWidgetViewPlay, launcherWidgetViewCover;
    private int launcherWidgetViewTimeLastUpdateValue = 0;
    private int launcherChangeTime = 0;

    private void updateLauncherView() {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                if (launcherWidgetViewTitle != null) {
                    launcherTitle.setText(launcherWidgetViewTitle.getText());
                }
                if (launcherWidgetViewArtist != null) {
                    launcherArtist.setText(launcherWidgetViewArtist.getText());
                }
                if (launcherWidgetViewTime != null) {
                    launcherTime.setText(launcherWidgetViewTime.getText());
                }
                if (launcherWidgetViewCover != null) {
                    launcherCover.setImageDrawable(launcherWidgetViewCover.getDrawable());
                }
                if (launcherWidgetViewProgressBar != null) {
                    launcherProgress.setProgress(launcherWidgetViewProgressBar.getProgress());
                    launcherProgress.setMax(launcherWidgetViewProgressBar.getMax());
                }
                if (System.currentTimeMillis() - playClickTime > 2000) {
                    if (launcherProgress.getProgress() != launcherWidgetViewTimeLastUpdateValue) {
                        launcherChangeTime = 0;
                        launcherPlay.setImageResource(R.mipmap.pause);
                        isruning = true;
                    } else {
                        launcherChangeTime++;
                        if (launcherChangeTime > 2) {
                            launcherPlay.setImageResource(R.mipmap.play);
                            isruning = false;
                        }
                    }
                    launcherWidgetViewTimeLastUpdateValue = launcherProgress.getProgress();
                }
            }
        });
    }

    private void ergodicLauncherView(final ViewGroup vg) {
        //先处理背景
        final ViewGroup bg = (ViewGroup) vg.getChildAt(0);
        View v1 = ViewUtils.getDeepViewByIndex(bg, new int[]{1, 0});
        if (v1 instanceof ImageView) {
            launcherWidgetViewCover = (ImageView) v1;
            launcherCover.setImageDrawable(launcherWidgetViewCover.getDrawable());
        }
        View v2 = ViewUtils.getDeepViewByIndex(bg, new int[]{1, 1, 0});
        if (v2 instanceof TextView) {
            launcherWidgetViewTitle = (TextView) v2;
            launcherTitle.setText(launcherWidgetViewTitle.getText());
        }
        View v3 = ViewUtils.getDeepViewByIndex(bg, new int[]{1, 1, 1, 0});
        if (v3 instanceof TextView) {
            launcherWidgetViewArtist = (TextView) v3;
            launcherArtist.setText(launcherWidgetViewArtist.getText());
        }

        View v4 = ViewUtils.getDeepViewByIndex(bg, new int[]{1, 1, 1, 1});
        if (v4 instanceof TextView) {
            launcherWidgetViewTime = (TextView) v4;
            launcherTime.setText(launcherWidgetViewTime.getText());
        }
        View v5 = ViewUtils.getDeepViewByIndex(bg, new int[]{1, 1, 2});
        if (v5 instanceof ProgressBar) {
            launcherWidgetViewProgressBar = (ProgressBar) v5;
            launcherProgress.setProgress(launcherWidgetViewProgressBar.getProgress());
            launcherProgress.setMax(launcherWidgetViewProgressBar.getMax());
        }
        View v6 = ViewUtils.getDeepViewByIndex(bg, new int[]{1, 1, 3, 3});
        if (v6 instanceof ImageView) {
            launcherWidgetViewPrew = (ImageView) v6;
        }

        View v7 = ViewUtils.getDeepViewByIndex(bg, new int[]{1, 1, 3, 4});
        if (v7 instanceof ImageView) {
            launcherWidgetViewNext = (ImageView) v7;
        }

        View v8 = ViewUtils.getDeepViewByIndex(bg, new int[]{1, 1, 3, 2});
        if (v8 instanceof ImageView) {
            launcherWidgetViewPlay = (ImageView) v8;
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
