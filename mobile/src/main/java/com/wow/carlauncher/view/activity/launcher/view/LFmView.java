package com.wow.carlauncher.view.activity.launcher.view;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.AppWidgetManage;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.view.activity.launcher.event.LEventRefreshFmPluginTest;
import com.wow.carlauncher.view.base.BaseEXView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_FM_PLUGIN;
import static com.wow.carlauncher.common.CommonData.APP_WIDGET_HOST_ID;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_FM_PLUGIN;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.BLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.WHITE;

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

    @ViewInject(R.id.fl_www)
    private FrameLayout fl_www;

    @ViewInject(R.id.rl_base)
    private View rl_base;

    @Event(value = {R.id.rl_base, R.id.ll_prew, R.id.ll_next, R.id.ll_play})
    private void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.rl_base: {
                int widgetId = appWidgetHost.allocateAppWidgetId();
                Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
                pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                ((Activity) getContext()).startActivityForResult(pickIntent, REQUEST_SELECT_FM_PLUGIN);
                break;
            }
            case R.id.ll_play: {
                Intent intent2 = new Intent("com.nwd.action.SL_WIDGET_COMMAND");
                intent2.putExtra("extra_SL_WIDGET_COMMAND", "switch_band");
                getContext().sendBroadcast(intent2);
                break;
            }
            case R.id.ll_next: {
                Intent intent2 = new Intent("com.nwd.action.SL_WIDGET_COMMAND");
                intent2.putExtra("extra_SL_WIDGET_COMMAND", "decrease");
                getContext().sendBroadcast(intent2);
                break;
            }
            case R.id.ll_prew: {
                Intent intent2 = new Intent("com.nwd.action.SL_WIDGET_COMMAND");
                intent2.putExtra("extra_SL_WIDGET_COMMAND", "increase");
                getContext().sendBroadcast(intent2);
                break;
            }
        }
    }

    //com.nwd.action.ACTION_SEND_RADIO_FREQUENCE
    //设置频率   extra_radio_frequence  String 频率

//    public void onReceive(Context paramContext, Intent paramIntent)
//    {
//        String str1 = paramIntent.getAction();
//        String str2;
//        Intent localIntent;
//        if (str1.equals("com.nwd.action.SL_WIDGET_COMMAND"))
//        {
//            str2 = paramIntent.getStringExtra("extra_SL_WIDGET_COMMAND");
//            LOG.print("current source id = " + mSourceID);
//            if (4 != mSourceID) {
//                KernelUtils.requestChangeSourceDirect(paramContext, (byte)4);
//            }
//            localIntent = new Intent("com.nwd.action.ACTION_KEY_VALUE");
//            if (!str2.equals("decrease")) {
//                break label126;
//            }
//            LOG.print("-----decrease-----");
//            localIntent.putExtra("extra_key_value", (byte)63);
//        }
//        for (;;)
//        {
//            paramContext.sendBroadcast(localIntent);
//            if (str1.equals("com.nwd.action.ACTION_CHANGE_SOURCE")) {
//                mSourceID = paramIntent.getByteExtra("extra_source_id", (byte)0);
//            }
//            return;
//            label126:
//            if (str2.equals("increase"))
//            {
//                LOG.print("-----increase-----");
//                localIntent.putExtra("extra_key_value", (byte)62);
//            }
//            else if (str2.equals("switch_band"))
//            {
//                LOG.print("-----switch band-----");
//                localIntent.putExtra("extra_key_value", (byte)4);
//            }
//            else if (str2.equals("radio_ams"))
//            {
//                LOG.print("-----radio ams-----");
//                localIntent.putExtra("extra_key_value", (byte)46);
//            }
//        }
//    }

//    String str = paramIntent.getAction();
//    if (this.mIUpdateMusicWidget != null)
//    {
//        if (!str.equals("com.nwd.action.ACTION_SEND_RADIO_FREQUENCE")) {
//            break label45;
//        }
//        this.frequency = ((Frequency)paramIntent.getParcelableExtra("extra_radio_frequence"));
//        this.mIUpdateMusicWidget.updateView(paramContext);
//    }
//    label45:
//            do
//    {
//        return;
//        if ("com.nwd.ACTION_LAUNCHER_CREATE".equals(str))
//        {
//            this.isUpate = false;
//            this.mIUpdateMusicWidget.releaseView(paramContext);
//            this.frequency = null;
//            this.mIUpdateMusicWidget.updateView(paramContext);
//            return;
//        }
//    } while (!"android.intent.action.BOOT_COMPLETED".equals(str));
//    try
//    {
//        this.handler = new Handler()
//        {
//            public void handleMessage(Message paramAnonymousMessage)
//            {
//                if (paramAnonymousMessage.what == 7894) {
//                    RadioWidgetManager.this.mIUpdateMusicWidget.updateView(paramContext);
//                }
//            }
//        };
//        new UpTaskThread().start();
//        return;
//    }
//    catch (Exception paramContext)
//    {
//        for (;;) {}
//    }

    @Override
    protected void initView() {
        appWidgetHost = new AppWidgetHost(getContext(), APP_WIDGET_HOST_ID);
        loadPlugin();
    }

    private AppWidgetHost appWidgetHost;
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final LEventRefreshFmPluginTest event) {
        loadPlugin();
    }

    private void loadPlugin() {
        int popup = SharedPreUtil.getInteger(APP_WIDGET_FM_PLUGIN, 0);
        if (popup != 0) {
            final View amapView = AppWidgetManage.self().getWidgetById(popup);
            fl_www.removeAllViews();
            fl_www.addView(amapView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}
