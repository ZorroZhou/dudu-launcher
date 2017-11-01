package com.wow.carlauncher.plugin.music.controllers.qqMusicCar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.plugin.music.MusicController;
import com.wow.carlauncher.plugin.music.controllers.QQMusicCarPlugin;
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
    private QQMusicCarPlugin controller;
    private boolean playing = false;
    private TextView tv_title;

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
            }
        });
    }

    @Subscribe
    public void onEventMainThread(PEventMusicStateChange event) {
        playing = event.run;
        if (event.run) {
            iv_play.setImageResource(R.mipmap.pause);
        } else {
            iv_play.setImageResource(R.mipmap.play);
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

    public QQMusicCarPopupView(Context context, QQMusicCarPlugin controller) {
        super(context);
        this.controller = controller;
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.plugin_music_popup, null);
        this.addView(linearLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        iv_play = findViewById(R.id.iv_play);
        tv_title = findViewById(R.id.tv_title);

        iv_play.setOnClickListener(this);
        findViewById(R.id.iv_prew).setOnClickListener(this);
        findViewById(R.id.iv_next).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_prew: {
                if (controller != null) {
                    controller.pre();
                }
                break;
            }
            case R.id.iv_play: {
                if (controller != null) {
                    if (playing) {
                        controller.pause();
                    } else {
                        controller.play();
                    }
                }
                break;
            }
            case R.id.iv_next: {
                if (controller != null) {
                    controller.next();
                }
                break;
            }
        }
    }
}