package com.wow.carlauncher.activity.launcher.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.plugin.fk.event.PFkEventConnect;
import com.wow.carlauncher.plugin.music.MusicPlugin;
import com.wow.carlauncher.plugin.music.MusicPluginListener;
import com.wow.carlauncher.plugin.music.event.PMusicEventCover;
import com.wow.carlauncher.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.plugin.music.event.PMusicEventProgress;
import com.wow.carlauncher.plugin.music.event.PMusicEventState;
import com.wow.frame.util.CommonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.xutils.x;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LMusicView extends LBaseView {

    public LMusicView(@NonNull Context context) {
        super(context);
        initView();
    }

    public LMusicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private ImageView music_iv_play, music_iv_cover;
    private TextView music_tv_title, music_tv_time;
    private ProgressBar music_pb_music;


    private void initView() {
        RelativeLayout musicView = (RelativeLayout) View.inflate(getContext(), R.layout.plugin_music_launcher, null);
        this.addView(musicView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        View.OnClickListener musicclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.ll_prew: {
                        MusicPlugin.self().pre();
                        break;
                    }
                    case R.id.iv_play: {
                        MusicPlugin.self().playOrPause();
                        break;
                    }
                    case R.id.ll_next: {
                        MusicPlugin.self().next();
                        break;
                    }
                }
            }
        };


        music_iv_play = (ImageView) musicView.findViewById(R.id.iv_play);
        music_tv_title = (TextView) musicView.findViewById(R.id.tv_title);
        music_iv_cover = (ImageView) musicView.findViewById(R.id.iv_cover);
        music_tv_time = (TextView) musicView.findViewById(R.id.tv_time);
        music_pb_music = (ProgressBar) musicView.findViewById(R.id.pb_music);

        musicView.findViewById(R.id.iv_play).setOnClickListener(musicclick);
        musicView.findViewById(R.id.ll_prew).setOnClickListener(musicclick);
        musicView.findViewById(R.id.ll_next).setOnClickListener(musicclick);
    }

    @Subscribe
    public void onEventMainThread(final PMusicEventCover event) {
        if (event.getCover() != null) {
            music_iv_cover.setImageBitmap(event.getCover());
        } else {

        }
    }

    @Subscribe
    public void onEventMainThread(final PMusicEventInfo event) {
        if (music_tv_title != null) {
            if (CommonUtil.isNotNull(event.getTitle())) {
                music_tv_title.setText(event.getTitle());
            } else {
                music_tv_title.setText("标题");
            }
        }
    }

    @Subscribe
    public void onEventMainThread(final PMusicEventProgress event) {
        if (music_pb_music != null && event.getCurrTime() > 0 && event.getTotalTime() > 0) {
            music_pb_music.setProgress(event.getCurrTime());
            music_pb_music.setMax(event.getTotalTime());
        }

        if (music_tv_time != null) {
            int tt = event.getTotalTime() / 1000;
            int cc = event.getCurrTime() / 1000;
            music_tv_time.setText(cc + ":" + tt);
        }
    }

    @Subscribe
    public void onEventMainThread(final PMusicEventState event) {
        if (music_iv_play != null) {
            if (event.isRun()) {
                music_iv_play.setImageResource(R.mipmap.ic_pause);
            } else {
                music_iv_play.setImageResource(R.mipmap.ic_play);
            }
        }
    }
}
