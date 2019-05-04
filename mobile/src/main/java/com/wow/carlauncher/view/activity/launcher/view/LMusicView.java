package com.wow.carlauncher.view.activity.launcher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
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
import com.wow.carlauncher.common.view.CustomRoundAngleImageView;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventCoverRefresh;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventProgress;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventState;
import com.wow.carlauncher.ex.plugin.music.event.PMusicRefresLrc;
import com.wow.carlauncher.view.base.BaseEXView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.BLACK;
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
    protected void initView() {
        x.task().postDelayed(() -> MusicPlugin.self().requestLast(), 500);

        Log.e(TAG + getClass().getSimpleName(), "initView: ");
    }

    @Override
    public void changedTheme(ThemeManage manage) {
        rl_base.setBackgroundResource(manage.getCurrentThemeRes(R.drawable.n_l_item1_bg));
        tv_title.setTextColor(manage.getCurrentThemeColor(R.color.l_text1));

        ll_play.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        if (ll_play.getHeight() > 0) {
                            ll_play.getViewTreeObserver().removeOnPreDrawListener(this);
                            ViewGroup.LayoutParams lp = iv_play.getLayoutParams();
                            if (currentTheme == WHITE || currentTheme == BLACK) {
                                lp.width = (int) (ll_play.getHeight() * 0.6);
                                lp.height = (int) (ll_play.getHeight() * 0.6);
                            } else {
                                lp.width = (int) (ll_play.getHeight() * 0.35);
                                lp.height = (int) (ll_play.getHeight() * 0.35);
                            }
                            iv_play.setLayoutParams(lp);
                        }
                        return true;
                    }
                });
        refreshPlay();

        ll_prew.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        if (ll_prew.getHeight() > 0) {
                            ll_prew.getViewTreeObserver().removeOnPreDrawListener(this);
                            ViewGroup.LayoutParams lp = iv_prew.getLayoutParams();
                            if (currentTheme == WHITE || currentTheme == BLACK) {
                                lp.width = (int) (ll_prew.getHeight() * 0.5);
                                lp.height = (int) (ll_prew.getHeight() * 0.5);
                            } else {
                                lp.width = (int) (ll_prew.getHeight() * 0.35);
                                lp.height = (int) (ll_prew.getHeight() * 0.35);
                            }
                            iv_prew.setLayoutParams(lp);
                        }
                        return true;
                    }
                });
        iv_prew.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_prev));


        ll_next.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        if (ll_next.getHeight() > 0) {
                            ll_next.getViewTreeObserver().removeOnPreDrawListener(this);
                            ViewGroup.LayoutParams lp = iv_next.getLayoutParams();
                            if (currentTheme == WHITE || currentTheme == BLACK) {
                                lp.width = (int) (ll_next.getHeight() * 0.5);
                                lp.height = (int) (ll_next.getHeight() * 0.5);
                            } else {
                                lp.width = (int) (ll_next.getHeight() * 0.35);
                                lp.height = (int) (ll_next.getHeight() * 0.35);
                            }
                            iv_next.setLayoutParams(lp);
                        }
                        return true;
                    }
                });
        iv_next.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_next));

        progressBar.setProgressDrawable(getResources().getDrawable(manage.getCurrentThemeRes(R.drawable.n_music_progress)));

        tv_music_title.setTextColor(manage.getCurrentThemeColor(R.color.l_music_title));
        tv_zuozhe.setTextColor(manage.getCurrentThemeColor(R.color.l_music_zuozhe));

        if (currentTheme == WHITE || currentTheme == BLACK) {
            tv_title.setGravity(Gravity.CENTER);
            music_iv_cover.setCircular(false);
        } else {
            tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            music_iv_cover.setCircular(true);
        }
        Log.e(TAG + getClass().getSimpleName(), "changedTheme: ");
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
        if (music_iv_cover != null) {
            ImageManage.self().loadImage(event.getUrl(), music_iv_cover, R.mipmap.music_dlogo);
        }
    }

}
