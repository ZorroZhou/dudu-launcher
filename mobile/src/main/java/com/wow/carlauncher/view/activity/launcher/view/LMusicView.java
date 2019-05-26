package com.wow.carlauncher.view.activity.launcher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.view.CustomRoundAngleImageView;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.skin.SkinManage;
import com.wow.carlauncher.ex.manage.skin.SkinUtil;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.music.event.MMEventControllerRefresh;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventCoverRefresh;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventProgress;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventState;
import com.wow.carlauncher.ex.plugin.music.event.PMusicRefresLrc;
import com.wow.carlauncher.view.activity.launcher.BaseThemeView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 10124 on 2018/4/20.
 */
@SuppressLint("RtlHardcoded")
public class LMusicView extends BaseThemeView {

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
    protected void initView() {
        refreshPlay();

        music_iv_cover = findViewById(R.id.music_iv_cover);

        TaskExecutor.self().run(() -> {
            MusicPlugin.self().requestLast();
            TaskExecutor.self().autoPost(() -> tv_title.setText(MusicPlugin.self().name()));
        }, 500);
        LogEx.d(this, "initView: ");
    }

    @Override
    public void changedSkin(SkinManage manage) {
        tv_title.setGravity(SkinUtil.analysisItemTitleAlign(manage.getString(R.string.theme_item_title_align)));

        if (SkinUtil.analysisMusicCoverType(manage.getString(R.string.theme_item_music_cover_type))) {
            ImageView temp2 = findViewById(R.id.music_iv_cover2);
            if (!temp2.equals(music_iv_cover)) {
                ImageView temp = music_iv_cover;
                music_iv_cover = temp2;
                music_iv_cover.setImageDrawable(temp.getDrawable());
                temp.setVisibility(GONE);
                temp2.setVisibility(VISIBLE);
            }
        } else {
            ImageView temp2 = findViewById(R.id.music_iv_cover);
            if (!temp2.equals(music_iv_cover)) {
                ImageView temp = music_iv_cover;
                music_iv_cover = temp2;
                music_iv_cover.setImageDrawable(temp.getDrawable());
                temp.setVisibility(GONE);
                temp2.setVisibility(VISIBLE);
            }
        }
    }

    private boolean run;

    @BindView(R.id.rl_base)
    View rl_base;

    @BindView(R.id.ll_prew)
    LinearLayout ll_prew;

    @BindView(R.id.ll_next)
    LinearLayout ll_next;

    @BindView(R.id.ll_play)
    LinearLayout ll_play;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_play)
    ImageView iv_play;

    @BindView(R.id.iv_prew)
    ImageView iv_prew;

    @BindView(R.id.iv_next)
    ImageView iv_next;

    @BindView(R.id.tv_music_title)
    TextView tv_music_title;

    @BindView(R.id.tv_zuozhe)
    TextView tv_zuozhe;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private ImageView music_iv_cover;

    private void refreshPlay() {
        if (run) {
            iv_play.setImageResource(R.drawable.theme_ic_pause);
        } else {
            iv_play.setImageResource(R.drawable.theme_ic_play);
        }
    }


    @OnClick(value = {R.id.ll_play, R.id.ll_prew, R.id.ll_next, R.id.rl_base})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.ll_prew: {
                MusicPlugin.self().pre();
                break;
            }
            case R.id.ll_play: {
                MusicPlugin.self().playOrPause();
                break;
            }
            case R.id.ll_next: {
                MusicPlugin.self().next();
                break;
            }
            case R.id.rl_base: {
                if (CommonUtil.isNotNull(MusicPlugin.self().clazz())) {
                    AppInfoManage.self().openApp(MusicPlugin.self().clazz());
                }
                break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicRefresLrc event) {
        tv_zuozhe.setText(event.getLrc());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventInfo event) {
        if (!event.isHaveLrc()) {
            if (tv_music_title != null && tv_zuozhe != null) {
                if (CommonUtil.isNotNull(event.getTitle())) {
                    tv_music_title.setText(event.getTitle());
                } else {
                    tv_music_title.setText("音乐");
                }

                if (CommonUtil.isNotNull(event.getArtist())) {
                    tv_zuozhe.setText(event.getArtist());
                    tv_zuozhe.setVisibility(VISIBLE);
                } else {
                    tv_zuozhe.setVisibility(GONE);
                }
            }
        } else {
            if (tv_music_title != null && tv_zuozhe != null) {
                if (CommonUtil.isNotNull(event.getTitle())) {
                    String msg = event.getTitle();
                    if (CommonUtil.isNotNull(event.getArtist())) {
                        msg = msg + "-" + event.getArtist();
                    }
                    tv_music_title.setText(msg);
                } else {
                    tv_music_title.setText("音乐");
                }

                tv_zuozhe.setText("");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventState event) {
        if (iv_play != null) {
            run = event.isRun();
            refreshPlay();
            if (event.isShowProgress()) {
                progressBar.setVisibility(VISIBLE);
            } else {
                progressBar.setVisibility(GONE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventProgress event) {
        if (progressBar != null) {
            progressBar.setProgress((int) (event.getCurrTime() * 100F / event.getTotalTime()));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventCoverRefresh event) {
        if (event.isHave()) {
            ImageManage.self().loadImage(event.getUrl(), music_iv_cover, R.drawable.theme_music_dcover);
        } else {
            music_iv_cover.setImageResource(R.drawable.theme_music_dcover);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MMEventControllerRefresh event) {
        if (tv_title != null) {
            tv_title.setText(MusicPlugin.self().name());
        }
    }


}
