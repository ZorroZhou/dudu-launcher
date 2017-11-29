package com.wow.carlauncher.plugin.music;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wow.frame.util.SharedPreUtil;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppWidgetManage;
import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.frame.util.ViewUtils;

import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_NCM_WIDGET2;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_NCM_WIDGET1;

public class NcMusicPlugin extends BasePlugin {
    private ImageView launcherCover, launcherIvPlay;
    private TextView launcherTitle, launcherArtist;
    private ProgressBar launcherProgress;


    private ImageView popupIvPlay;
    private TextView popupTitle;
    private ProgressBar popupProgress;

    private long playClickTime = -1;
    private boolean isruning = false;

    private Timer timer;

    public NcMusicPlugin(Context context, PluginManage pluginManage) {
        super(context, pluginManage);

        int popup = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_WIDGET1, -1);
        int launcher = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_WIDGET2, -1);
        if (launcher == -1 || popup == -1) {
            return;
        }
        startUpdate();
    }

    private void startUpdate() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (popupView != null) {
                    updatePopupView();
                }
                if (launcherView != null) {
                    updateLauncherView();
                }
            }
        }, 0, 100);
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
    }

    private TextView popupWidgetViewTitle;
    private ProgressBar popupWidgetViewProgressBar;
    private ImageView popupWidgetViewPrew, popupWidgetViewNext, popupWidgetViewPlay;
    private int popupWidgetViewTimeLastUpdateValue = -1;
    private int popupChangeTime = 0;

    public ViewGroup initPopupView() {
        int popup = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_WIDGET1, -1);
        if (popup == -1) {
            LinearLayout linearLayout = new LinearLayout(context);
            TextView textView = new TextView(context);
            textView.setPadding(20, 20, 20, 20);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            String msg = "请到系统设置-应用设置-插件设置选项,设置网易云音乐的1*4插件!";
            textView.setText(msg);
            linearLayout.addView(textView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            return linearLayout;
        }
        final View popupWidgetView = AppWidgetManage.self().getWidgetById(popup);
        popupWidgetView.setPadding(0, 0, 0, 0);

        RelativeLayout popupView = (RelativeLayout) View.inflate(context, R.layout.plugin_music_ncm_popup, null);
        LinearLayout popupHouse = (LinearLayout) popupView.findViewById(R.id.ll_house);
        popupHouse.addView(popupWidgetView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        popupTitle = (TextView) popupView.findViewById(R.id.tv_title);
        popupProgress = (ProgressBar) popupView.findViewById(R.id.pb_music);
        popupIvPlay = (ImageView) popupView.findViewById(R.id.iv_play);

        View.OnClickListener popupOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ll_prew: {
                        if (popupWidgetViewPrew != null) {
                            popupWidgetViewPrew.performClick();
                        }
                        playClickTime = System.currentTimeMillis();
                        if (!isruning) {
                            popupIvPlay.setImageResource(R.mipmap.ic_pause);
                            isruning = true;
                        }
                        break;
                    }
                    case R.id.ll_play: {
                        if (popupWidgetViewPlay != null) {
                            popupWidgetViewPlay.performClick();
                        }
                        playClickTime = System.currentTimeMillis();
                        if (!isruning) {
                            popupIvPlay.setImageResource(R.mipmap.ic_pause);
                            isruning = true;
                        } else {
                            popupIvPlay.setImageResource(R.mipmap.ic_play);
                            isruning = false;
                        }
                        break;
                    }
                    case R.id.ll_next: {
                        if (popupWidgetViewNext != null) {
                            popupWidgetViewNext.performClick();
                        }
                        playClickTime = System.currentTimeMillis();
                        if (!isruning) {
                            popupIvPlay.setImageResource(R.mipmap.ic_pause);
                            isruning = true;
                        }
                        break;
                    }
                }
            }
        };
        popupView.findViewById(R.id.ll_play).setOnClickListener(popupOnClickListener);
        popupView.findViewById(R.id.ll_prew).setOnClickListener(popupOnClickListener);
        popupView.findViewById(R.id.ll_next).setOnClickListener(popupOnClickListener);

        ergodicPopupView((ViewGroup) popupWidgetView);
        return popupView;
    }

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
                        popupIvPlay.setImageResource(R.mipmap.ic_pause);
                        isruning = true;
                    } else {
                        popupChangeTime++;
                        if (popupChangeTime > 30) {
                            popupIvPlay.setImageResource(R.mipmap.ic_play);
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


    private TextView launcherWidgetViewTitle, launcherWidgetViewArtist;
    private ProgressBar launcherWidgetViewProgressBar;
    private ImageView launcherWidgetViewPrew, launcherWidgetViewNext, launcherWidgetViewPlay, launcherWidgetViewCover;
    private int launcherWidgetViewTimeLastUpdateValue = 0;
    private int launcherChangeTime = 0;

    public ViewGroup initLauncherView() {
        int launcher = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_WIDGET2, -1);
        if (launcher == -1) {
            LinearLayout linearLayout = new LinearLayout(context);
            TextView textView = new TextView(context);
            textView.setPadding(20, 20, 20, 20);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            String msg = "请到系统设置-应用设置-插件设置选项,设置网易云音乐的2*4插件!";
            textView.setText(msg);
            linearLayout.addView(textView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            return linearLayout;
        }
        // 获取所选的Widget的AppWidgetProviderInfo信息
        final View launcherWidgetView = AppWidgetManage.self().getWidgetById(launcher);

        RelativeLayout launcherView = (RelativeLayout) View.inflate(context, R.layout.plugin_music_ncm_launcher, null);
        LinearLayout launcherHouse = (LinearLayout) launcherView.findViewById(R.id.ll_house);
        launcherHouse.addView(launcherWidgetView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        launcherCover = (ImageView) launcherView.findViewById(R.id.iv_cover);
        launcherTitle = (TextView) launcherView.findViewById(R.id.tv_title);
        launcherArtist = (TextView) launcherView.findViewById(R.id.tv_artist);
        launcherProgress = (ProgressBar) launcherView.findViewById(R.id.pb_music);
        launcherIvPlay = (ImageView) launcherView.findViewById(R.id.iv_play);
        View.OnClickListener launcherOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ll_prew: {
                        if (launcherWidgetViewPrew != null) {
                            launcherWidgetViewPrew.performClick();
                        }
                        playClickTime = System.currentTimeMillis();
                        if (!isruning) {
                            launcherIvPlay.setImageResource(R.mipmap.ic_pause);
                            isruning = true;
                        }
                        break;
                    }
                    case R.id.ll_play: {
                        if (launcherWidgetViewPlay != null) {
                            launcherWidgetViewPlay.performClick();
                        }
                        playClickTime = System.currentTimeMillis();
                        if (!isruning) {
                            launcherIvPlay.setImageResource(R.mipmap.ic_pause);
                            isruning = true;
                        } else {
                            launcherIvPlay.setImageResource(R.mipmap.ic_play);
                            isruning = false;
                        }
                        break;
                    }
                    case R.id.ll_next: {
                        if (launcherWidgetViewNext != null) {
                            launcherWidgetViewNext.performClick();
                        }
                        playClickTime = System.currentTimeMillis();
                        if (!isruning) {
                            launcherIvPlay.setImageResource(R.mipmap.ic_pause);
                            isruning = true;
                        }
                        break;
                    }
                }
            }
        };
        launcherView.findViewById(R.id.ll_play).setOnClickListener(launcherOnClickListener);
        launcherView.findViewById(R.id.ll_prew).setOnClickListener(launcherOnClickListener);
        launcherView.findViewById(R.id.ll_next).setOnClickListener(launcherOnClickListener);

        ergodicLauncherView((ViewGroup) launcherWidgetView);
        return launcherView;
    }

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
                        launcherIvPlay.setImageResource(R.mipmap.ic_pause);
                        isruning = true;
                    } else {
                        launcherChangeTime++;
                        if (launcherChangeTime > 30) {
                            launcherIvPlay.setImageResource(R.mipmap.ic_play);
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
}
