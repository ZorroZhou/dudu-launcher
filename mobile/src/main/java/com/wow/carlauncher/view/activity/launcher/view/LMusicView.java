package com.wow.carlauncher.view.activity.launcher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.common.view.CustomRoundAngleImageView;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.musicCover.MusicCoverRefresh;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventProgress;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventState;
import com.wow.carlauncher.view.base.BaseEXView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.BLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.CBLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.WHITE;


/**
 * Created by 10124 on 2018/4/20.
 */
@SuppressLint("RtlHardcoded")
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
    public void changedTheme(ThemeManage manage) {
        rl_base.setBackgroundResource(manage.getCurrentThemeRes(R.drawable.n_l_item1_bg));
        tv_title.setTextColor(manage.getCurrentThemeColor(R.color.l_text1));

        ll_play.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        ll_play.getViewTreeObserver().removeOnPreDrawListener(this);
                        ViewGroup.LayoutParams lp = iv_play.getLayoutParams();
                        if (currentTheme == WHITE || currentTheme == BLACK) {
                            lp.width = ll_prew.getHeight();
                            lp.height = ll_prew.getHeight();
                        } else {
                            lp.width = (int) (ll_prew.getHeight() * 0.5);
                            lp.height = (int) (ll_prew.getHeight() * 0.5);
                        }
                        iv_play.setLayoutParams(lp);
                        return true;
                    }
                });
        refreshPlay();

        ll_prew.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        ll_prew.getViewTreeObserver().removeOnPreDrawListener(this);
                        ViewGroup.LayoutParams lp = iv_prew.getLayoutParams();
                        if (currentTheme == WHITE || currentTheme == BLACK) {
                            lp.width = (int) (ll_prew.getHeight() * 0.7);
                            lp.height = (int) (ll_prew.getHeight() * 0.7);
                        } else {
                            lp.width = (int) (ll_prew.getHeight() * 0.5);
                            lp.height = (int) (ll_prew.getHeight() * 0.5);
                        }
                        iv_prew.setLayoutParams(lp);
                        return true;
                    }
                });
        iv_prew.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_prev));


        ll_next.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        ll_next.getViewTreeObserver().removeOnPreDrawListener(this);
                        ViewGroup.LayoutParams lp = iv_next.getLayoutParams();
                        if (currentTheme == WHITE || currentTheme == BLACK) {
                            lp.width = (int) (ll_prew.getHeight() * 0.7);
                            lp.height = (int) (ll_prew.getHeight() * 0.7);
                        } else {
                            lp.width = (int) (ll_prew.getHeight() * 0.5);
                            lp.height = (int) (ll_prew.getHeight() * 0.5);
                        }
                        iv_next.setLayoutParams(lp);
                        return true;
                    }
                });
        iv_next.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_next));

        progressBar.setProgressDrawable(getResources().getDrawable(manage.getCurrentThemeRes(R.drawable.n_music_progress)));
        progressBar2.setProgressDrawable(getResources().getDrawable(manage.getCurrentThemeRes(R.drawable.n_music_progress)));

        if (currentTheme == WHITE || currentTheme == BLACK) {
            progressBar.setVisibility(VISIBLE);
            progressBar2.setVisibility(INVISIBLE);
            currProgressBar = progressBar;
        } else {
            progressBar2.setVisibility(VISIBLE);
            progressBar.setVisibility(INVISIBLE);
            currProgressBar = progressBar2;
        }

        tv_music_title.setTextColor(manage.getCurrentThemeColor(R.color.l_music_title));
        tv_zuozhe.setTextColor(manage.getCurrentThemeColor(R.color.l_music_zuozhe));

        if (currentTheme == WHITE || currentTheme == BLACK) {
            tv_title.setGravity(Gravity.CENTER);
            music_iv_cover.setCircular(false);
        } else {
            tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            music_iv_cover.setCircular(true);
        }

    }

    private boolean run;

    @ViewInject(R.id.rl_base)
    private View rl_base;

    @ViewInject(R.id.ll_prew)
    private LinearLayout ll_prew;

    @ViewInject(R.id.ll_next)
    private LinearLayout ll_next;

    @ViewInject(R.id.ll_play)
    private LinearLayout ll_play;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.iv_play)
    private ImageView iv_play;

    @ViewInject(R.id.iv_prew)
    private ImageView iv_prew;

    @ViewInject(R.id.iv_next)
    private ImageView iv_next;

    @ViewInject(R.id.tv_music_title)
    private TextView tv_music_title;

    @ViewInject(R.id.tv_zuozhe)
    private TextView tv_zuozhe;

    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar;

    @ViewInject(R.id.progressBar2)
    private ProgressBar progressBar2;

    private ProgressBar currProgressBar;


    @ViewInject(R.id.music_iv_cover)
    private CustomRoundAngleImageView music_iv_cover;

    private void refreshPlay() {
        if (run) {
            if (ThemeManage.self().getTheme() == WHITE) {
                iv_play.setImageResource(R.mipmap.ic_pause);
            } else if (ThemeManage.self().getTheme() == BLACK) {
                iv_play.setImageResource(R.mipmap.ic_pause_b);
            } else {
                iv_play.setImageResource(R.mipmap.ic_pause_cb);
            }
        } else {
            if (ThemeManage.self().getTheme() == WHITE) {
                iv_play.setImageResource(R.mipmap.ic_play);
            } else if (ThemeManage.self().getTheme() == BLACK) {
                iv_play.setImageResource(R.mipmap.ic_play_b);
            } else {
                iv_play.setImageResource(R.mipmap.ic_play_cb);
            }
        }
    }


    @Event(value = {R.id.ll_play, R.id.ll_prew, R.id.ll_next, R.id.rl_base})
    private void clickEvent(View view) {
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
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MusicCoverRefresh event) {
        if (music_iv_cover != null) {
            music_iv_cover.setImageBitmap(event.getCover());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventState event) {
        if (iv_play != null) {
            run = event.isRun();
            refreshPlay();
            if (!run) {
                progressBar.setVisibility(INVISIBLE);
                progressBar2.setVisibility(INVISIBLE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventProgress event) {
        if (currProgressBar != null) {
            currProgressBar.setVisibility(VISIBLE);
            currProgressBar.setProgress((int) (event.getCurrTime() * 100F / event.getTotalTime()));
        }
    }

}
