package com.wow.carlauncher.plugin.music;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.frame.util.CommonUtil;

import org.xutils.x;

/**
 * Created by 10124 on 2017/10/28.
 */

public class MusicPopupView extends LinearLayout implements View.OnClickListener, MusicPluginListener {
    private LayoutInflater inflater;

    private ImageView iv_play;
    private MusicController controller;
    private boolean playing = false;
    private TextView tv_title;
    private ProgressBar pb_music;

    public void refreshInfo(final String title, final String artist) {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                if (tv_title != null) {
                    if (CommonUtil.isNotNull(title)) {
                        tv_title.setText(title);
                    } else {
                        tv_title.setText("标题");
                    }
                }
            }
        });
    }

    @Override
    public void refreshProgress(final int curr_time, final int total_time) {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                if (pb_music != null && curr_time > 0 && total_time > 0) {
                    pb_music.setProgress(curr_time);
                    pb_music.setMax(total_time);
                }
            }
        });

    }

    @Override
    public void refreshCover(final Bitmap cover) {

    }

    public void refreshState(final boolean run) {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                if (iv_play != null) {
                    playing = run;
                    if (run) {
                        iv_play.setImageResource(R.mipmap.ic_pause);
                    } else {
                        iv_play.setImageResource(R.mipmap.ic_play);
                    }
                }
            }
        });
    }

    public MusicPopupView(Context context, MusicController controller) {
        super(context);
        this.controller = controller;
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        View linearLayout = inflater.inflate(R.layout.plugin_music_popup, null);
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