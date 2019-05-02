package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.base.BaseEXView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.BLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.WHITE;
import static com.wow.carlauncher.view.activity.launcher.view.LShadowView.SizeEnum.FIVE;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LFmView extends BaseEXView {

    public LFmView(@NonNull Context context) {
        super(context);
    }

    public LFmView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_fm;
    }

    @Override
    public void changedTheme(ThemeManage manage) {
        rl_base.setBackgroundResource(manage.getCurrentThemeRes(R.drawable.n_l_item1_bg));
        tv_title.setTextColor(manage.getCurrentThemeColor(R.color.l_title));

        ll_play.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        if (ll_play.getHeight() > 0) {
                            ll_play.getViewTreeObserver().removeOnPreDrawListener(this);
                            ViewGroup.LayoutParams lp = iv_play.getLayoutParams();
                            if (currentTheme == WHITE || currentTheme == BLACK) {
                                lp.width = ll_play.getHeight();
                                lp.height = ll_play.getHeight();
                            } else {
                                lp.width = (int) (ll_play.getHeight() * 0.55);
                                lp.height = (int) (ll_play.getHeight() * 0.55);
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
                                lp.width = (int) (ll_prew.getHeight() * 0.7);
                                lp.height = (int) (ll_prew.getHeight() * 0.7);
                            } else {
                                lp.width = (int) (ll_prew.getHeight() * 0.5);
                                lp.height = (int) (ll_prew.getHeight() * 0.5);
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
                                lp.width = (int) (ll_next.getHeight() * 0.7);
                                lp.height = (int) (ll_next.getHeight() * 0.7);
                            } else {
                                lp.width = (int) (ll_next.getHeight() * 0.5);
                                lp.height = (int) (ll_next.getHeight() * 0.5);
                            }
                            iv_next.setLayoutParams(lp);
                        }
                        return true;
                    }
                });
        iv_next.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_next));
    }

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

    @ViewInject(R.id.rl_base)
    private View rl_base;

    @Event(value = {R.id.rl_base})
    private void clickEvent(View view) {
    }

    private boolean run = false;

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
}
