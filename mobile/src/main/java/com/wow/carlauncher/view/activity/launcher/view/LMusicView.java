package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.MusicCoverManage;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventProgress;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventState;
import com.wow.carlauncher.view.base.BaseEXView;
import com.wow.frame.util.CommonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.ex.manage.ThemeManage.WHITE;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LMusicView extends BaseEXView {

    public LMusicView(@NonNull Context context) {
        super(context);
    }

    public LMusicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_music;
    }

    @Override
    public void onThemeChanged(ThemeManage manage) {
        Context context = getContext();
        rl_base.setBackgroundResource(manage.getCurrentThemeRes(context, R.drawable.n_l_item1_bg));
        tv_title.setTextColor(manage.getCurrentThemeColor(context, R.color.l_text1));

        refreshPlay();

        ll_prew.setImageResource(manage.getCurrentThemeRes(context, R.mipmap.ic_prev));
        ll_next.setImageResource(manage.getCurrentThemeRes(context, R.mipmap.ic_next));

        tv_music_title.setTextColor(manage.getCurrentThemeColor(context, R.color.l_music_title));
        tv_zuozhe.setTextColor(manage.getCurrentThemeColor(context, R.color.l_music_zuozhe));
    }

    private boolean run;

    @ViewInject(R.id.rl_base)
    private View rl_base;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.iv_play)
    private ImageView iv_play;

    @ViewInject(R.id.ll_prew)
    private ImageView ll_prew;

    @ViewInject(R.id.ll_next)
    private ImageView ll_next;

    @ViewInject(R.id.tv_music_title)
    private TextView tv_music_title;

    @ViewInject(R.id.tv_zuozhe)
    private TextView tv_zuozhe;

    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar;

    @ViewInject(R.id.music_iv_cover)
    private ImageView music_iv_cover;

    private void refreshPlay() {
        if (run) {
            if (ThemeManage.self().getThemeMode() == WHITE) {
                iv_play.setImageResource(R.mipmap.ic_pause);
            } else {
                iv_play.setImageResource(R.mipmap.ic_pause_b);
            }
        } else {
            if (ThemeManage.self().getThemeMode() == WHITE) {
                iv_play.setImageResource(R.mipmap.ic_play);
            } else {
                iv_play.setImageResource(R.mipmap.ic_play_b);
            }
        }
    }


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
        if (tv_music_title != null && tv_zuozhe != null) {
            if (CommonUtil.isNotNull(event.getTitle())) {
                tv_music_title.setText(event.getTitle());
            } else {
                tv_music_title.setText("音乐");
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
        if (iv_play != null) {
            run = event.isRun();
            refreshPlay();
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
