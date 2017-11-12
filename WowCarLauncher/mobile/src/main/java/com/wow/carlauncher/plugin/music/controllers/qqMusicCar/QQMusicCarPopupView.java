package com.wow.carlauncher.plugin.music.controllers.qqMusicCar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.plugin.music.controllers.QQMusicCarPluginOld;
import com.wow.carlauncher.plugin.music.event.PEventMusicInfoChange;
import com.wow.carlauncher.plugin.music.event.PEventMusicStateChange;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.x;

/**
 * Created by 10124 on 2017/10/28.
 */

public class QQMusicCarPopupView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "PluginMusicView";

    private LayoutInflater inflater;

    private ImageView iv_play;
    private QQMusicCarPluginOld controller;
    private boolean playing = false;
    private TextView tv_title;
    private ProgressBar pb_music;

    @Subscribe
    public void onEventMainThread(final PEventMusicInfoChange event) {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                if (tv_title != null && CommonUtil.isNotNull(event.title)) {
                    tv_title.setText(event.title);
                } else {
                    tv_title.setText("标题");
                }

                if (pb_music != null && event.curr_time > 0 && event.total_time > 0) {
                    pb_music.setProgress(event.curr_time);
                    pb_music.setMax(event.total_time);
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(PEventMusicStateChange event) {
        playing = event.run;
        if (event.run) {
            iv_play.setImageResource(R.mipmap.ic_pause);
        } else {
            iv_play.setImageResource(R.mipmap.ic_play);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    public QQMusicCarPopupView(Context context, QQMusicCarPluginOld controller) {
        super(context);
        this.controller = controller;
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        View linearLayout = inflater.inflate(R.layout.plugin_music_qcm_popup, null);
        this.addView(linearLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        iv_play = (ImageView) findViewById(R.id.iv_play);
        tv_title = (TextView) findViewById(R.id.tv_title);
        pb_music = (ProgressBar) findViewById(R.id.pb_music);

        findViewById(R.id.ll_play).setOnClickListener(this);
        findViewById(R.id.ll_prew).setOnClickListener(this);
        findViewById(R.id.ll_next).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_prew: {
                if (controller != null) {
                    controller.pre();
                }
                break;
            }
            case R.id.ll_play: {
                if (controller != null) {
                    if (playing) {
                        controller.pause();
                    } else {
                        controller.play();
                    }
                }
                break;
            }
            case R.id.ll_next: {
                if (controller != null) {
                    controller.next();
                }
                break;
            }
        }
    }
}