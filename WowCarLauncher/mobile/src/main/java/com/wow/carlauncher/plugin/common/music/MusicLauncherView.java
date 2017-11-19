package com.wow.carlauncher.plugin.common.music;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.plugin.pevent.PEventMusicInfoChange;
import com.wow.carlauncher.plugin.pevent.PEventMusicStateChange;
import com.wow.carlauncher.plugin.powerAmp.PowerAmpCarPlugin;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.x;

import static com.wow.carlauncher.common.CommonData.TAG;

public class MusicLauncherView extends LinearLayout implements View.OnClickListener, MusicView {
    private LayoutInflater inflater;

    private ImageView iv_play, iv_cover;
    private MusicController controller;
    private TextView tv_title, tv_artist;
    private ProgressBar pb_music;
    private FrameLayout fl_cover;
    private boolean playing = false;

    public void refreshInfo(final String title, final String artist) {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                if (tv_title != null) {
                    if (CommonUtil.isNotNull(title)) {
                        tv_title.setText(title);
                    } else {
                        tv_title.setText("标题");
                    }
                }
                if (tv_artist != null) {
                    if (CommonUtil.isNotNull(artist)) {
                        tv_artist.setText(artist);
                    } else {
                        tv_artist.setText("歌手");
                    }
                }
            }
        });
    }

    public void refreshProgress(final int curr_time, final int total_time) {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                if (pb_music != null && curr_time > 0 && total_time > 0) {
                    pb_music.setProgress(curr_time);
                    pb_music.setMax(total_time);
                }
            }
        });
    }

    public void refreshCover(final Bitmap cover) {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                if (fl_cover != null && cover != null) {
                    fl_cover.setVisibility(VISIBLE);
                    iv_cover.setImageBitmap(cover);
                } else {
                    fl_cover.setVisibility(GONE);
                }
            }
        });
    }

    public void refreshState(final boolean run) {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                playing = run;
                if (run) {
                    iv_play.setImageResource(R.mipmap.ic_pause);
                } else {
                    iv_play.setImageResource(R.mipmap.ic_play);
                }
            }
        });
    }

    public MusicLauncherView(Context context, MusicController controller) {
        super(context);
        this.controller = controller;
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        View linearLayout = inflater.inflate(R.layout.plugin_music_com_launcher, null);
        this.addView(linearLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        iv_play = (ImageView) findViewById(R.id.iv_play);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_artist = (TextView) findViewById(R.id.tv_artist);
        pb_music = (ProgressBar) findViewById(R.id.pb_music);

        fl_cover = (FrameLayout) findViewById(R.id.cover);
        iv_cover = (ImageView) findViewById(R.id.iv_cover);

        findViewById(R.id.ll_play).setOnClickListener(this);
        findViewById(R.id.ll_prew).setOnClickListener(this);
        findViewById(R.id.ll_next).setOnClickListener(this);

    }

    public void setName(String name) {
        ((TextView) findViewById(R.id.tv_pname)).setText(name);
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
                Log.e(TAG, "onClick: " + controller);
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