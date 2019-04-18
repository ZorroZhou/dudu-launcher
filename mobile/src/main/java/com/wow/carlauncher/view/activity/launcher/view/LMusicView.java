package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.ex.manage.MusicCoverManage;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventProgress;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventState;
import com.wow.carlauncher.view.base.BaseEBusView;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LMusicView extends BaseEBusView {

    public LMusicView(@NonNull Context context) {
        super(context);
    }

    public LMusicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LMusicView(@NonNull Context context, Bundle savedInstanceState) {
        super(context, savedInstanceState);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_music;
    }

    @ViewInject(R.id.iv_play)
    private ImageView music_iv_play;

    @ViewInject(R.id.tv_title)
    private TextView music_tv_title;

    @ViewInject(R.id.tv_zuozhe)
    private TextView tv_zuozhe;

    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar;

    @ViewInject(R.id.music_iv_cover)
    private ImageView music_iv_cover;


    @Event(value = {R.id.iv_play, R.id.ll_prew, R.id.ll_next})
    private void clickEvent(View view) {
        Log.d(TAG, "clickEvent: " + view);
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

    private String key = "";

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventInfo event) {
        if (music_tv_title != null && tv_zuozhe != null) {
            if (CommonUtil.isNotNull(event.getTitle())) {
                music_tv_title.setText(event.getTitle());
            } else {
                music_tv_title.setText("音乐");
            }

            if (CommonUtil.isNotNull(event.getArtist())) {
                tv_zuozhe.setText(event.getArtist());
            } else {
                tv_zuozhe.setText("未知作家");
            }

            if (music_iv_cover != null) {
                String nowkey = event.getTitle() + event.getArtist();
                if (!nowkey.equals(key)) {
                    key = nowkey;
                    MusicCoverManage.self().loadMusicCover(event.getTitle(), event.getArtist(), new MusicCoverManage.Callback() {
                        @Override
                        public void loadCover(boolean success, String title, String zuojia, final Bitmap cover) {
                            String kk = title + zuojia;
                            if (key.equals(kk)) {
                                if (success) {
                                    x.task().autoPost(new Runnable() {
                                        @Override
                                        public void run() {
                                            music_iv_cover.setImageBitmap(cover);
                                        }
                                    });
                                } else {
                                    music_iv_cover.setImageResource(R.mipmap.music_dlogo);
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventState event) {
        if (music_iv_play != null) {
            if (event.isRun()) {
                if (SharedPreUtil.getSharedPreInteger(CommonData.SDATA_APP_THEME, R.style.AppThemeWhile) == R.style.AppThemeWhile) {
                    music_iv_play.setImageResource(R.mipmap.ic_pause);
                } else {
                    music_iv_play.setImageResource(R.mipmap.ic_pause_b);
                }
            } else {
                if (SharedPreUtil.getSharedPreInteger(CommonData.SDATA_APP_THEME, R.style.AppThemeWhile) == R.style.AppThemeWhile) {
                    music_iv_play.setImageResource(R.mipmap.ic_play);
                } else {
                    music_iv_play.setImageResource(R.mipmap.ic_play_b);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventProgress event) {
        if (progressBar != null) {
            progressBar.setVisibility(VISIBLE);
            progressBar.setProgress((int) (event.getCurrTime() * 100F / event.getTotalTime()));
        } else {
            progressBar.setVisibility(GONE);
        }
    }

}
