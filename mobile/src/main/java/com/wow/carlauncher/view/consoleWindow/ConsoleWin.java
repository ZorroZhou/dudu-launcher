package com.wow.carlauncher.view.consoleWindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.DateUtil;
import com.wow.carlauncher.common.util.LunarUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.musicCover.MusicCoverRefresh;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.console.ConsolePlugin;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventAction;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventState;
import com.wow.carlauncher.view.activity.launcher.event.LDockRefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Calendar;
import java.util.Date;

import per.goweii.anypermission.AnyPermission;
import per.goweii.anypermission.RequestListener;

import static com.wow.carlauncher.common.CommonData.DAY_MILL;
import static com.wow.carlauncher.common.CommonData.MINUTE_MILL;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK1_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK2_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK3_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK4_CLASS;
import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.BLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.WHITE;
import static com.wow.carlauncher.ex.plugin.fk.FangkongProtocolEnum.YLFK;
import static com.wow.carlauncher.ex.plugin.fk.protocol.YiLianProtocol.CENTER_CLICK;
import static com.wow.carlauncher.ex.plugin.fk.protocol.YiLianProtocol.CENTER_LONG_CLICK;
import static com.wow.carlauncher.ex.plugin.fk.protocol.YiLianProtocol.LEFT_BOTTOM_CLICK;
import static com.wow.carlauncher.ex.plugin.fk.protocol.YiLianProtocol.LEFT_TOP_CLICK;
import static com.wow.carlauncher.ex.plugin.fk.protocol.YiLianProtocol.LEFT_TOP_LONG_CLICK;
import static com.wow.carlauncher.ex.plugin.fk.protocol.YiLianProtocol.RIGHT_BOTTOM_CLICK;
import static com.wow.carlauncher.ex.plugin.fk.protocol.YiLianProtocol.RIGHT_TOP_CLICK;
import static com.wow.carlauncher.ex.plugin.fk.protocol.YiLianProtocol.RIGHT_TOP_LONG_CLICK;

public class ConsoleWin implements ThemeManage.OnThemeChangeListener {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static ConsoleWin instance = new ConsoleWin();
    }

    public static ConsoleWin self() {
        return ConsoleWin.SingletonHolder.instance;
    }

    private ConsoleWin() {

    }

    //窗口管理器
    private WindowManager wm;
    //窗口的布局参数
    private WindowManager.LayoutParams winparams;
    //是否展示了
    private Boolean isShow = false;
    private CarLauncherApplication context;
    //窗口视图
    private RelativeLayout consoleWin;

    public void init(CarLauncherApplication context) {

        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        winparams = new WindowManager.LayoutParams();
        // 类型
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//6.0
            winparams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            winparams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        if (SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, true)) {
            winparams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        } else {
            winparams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        winparams.format = PixelFormat.TRANSLUCENT;
        winparams.width = outMetrics.widthPixels;
        winparams.height = outMetrics.heightPixels;

        winparams.gravity = Gravity.TOP | Gravity.START;
        winparams.x = 0;
        winparams.y = 0;

        consoleWin = (RelativeLayout) View.inflate(context, R.layout.console_window, null);

        x.view().inject(this, consoleWin);

        EventBus.getDefault().register(this);

        onThemeChanged(ThemeManage.self());
        ThemeManage.self().registerThemeChangeListener(this);
    }


    public void show() {
        if (!isShow) {
            wm.addView(consoleWin, winparams);
            isShow = true;
            selectApp(0);
        }
    }

    public void hide() {
        if (isShow) {
            wm.removeView(consoleWin);
            isShow = false;
        }
    }

    @Override
    public void onThemeChanged(ThemeManage manage) {
        ll_item1.setBackgroundResource(manage.getCurrentThemeRes(R.drawable.n_l_item1_bg));
        ll_item2.setBackgroundResource(manage.getCurrentThemeRes(R.drawable.n_l_item1_bg));
        ll_item3.setBackgroundResource(manage.getCurrentThemeRes(R.drawable.n_l_item1_bg));
        refreshPlay();
        music_iv_prew.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_prev2));
        music_iv_next.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_next2));

        manage.setTextViewsColor(consoleWin, new int[]{
                R.id.tv_app_name,
                R.id.tv_time,
                R.id.music_tv_title
        }, R.color.l_text1);

        loadDock();
    }

    private int selectApp = 0;

    private void selectApp(int selectApp) {
        this.selectApp = selectApp;
        x.task().autoPost(() -> {
            ll_dock1.setBackgroundResource(android.R.color.transparent);
            ll_dock2.setBackgroundResource(android.R.color.transparent);
            ll_dock3.setBackgroundResource(android.R.color.transparent);
            ll_dock4.setBackgroundResource(android.R.color.transparent);
            music_ll_prew.setBackgroundResource(android.R.color.transparent);
            music_ll_play.setBackgroundResource(android.R.color.transparent);
            music_ll_next.setBackgroundResource(android.R.color.transparent);
            if (selectApp == 0) {
                ll_dock1.setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_cell_bg));
            } else if (selectApp == 1) {
                ll_dock2.setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_cell_bg));
            } else if (selectApp == 2) {
                ll_dock3.setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_cell_bg));
            } else if (selectApp == 3) {
                ll_dock4.setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_cell_bg));
            } else if (selectApp == 4) {
                music_ll_prew.setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_cell_bg));
            } else if (selectApp == 5) {
                music_ll_play.setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_cell_bg));
            } else if (selectApp == 6) {
                music_ll_next.setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_cell_bg));
            }
        });
    }

    @ViewInject(R.id.music_ll_prew)
    private LinearLayout music_ll_prew;

    @ViewInject(R.id.music_ll_play)
    private LinearLayout music_ll_play;

    @ViewInject(R.id.music_ll_next)
    private LinearLayout music_ll_next;

    @ViewInject(R.id.ll_dock1)
    private LinearLayout ll_dock1;

    @ViewInject(R.id.ll_dock2)
    private LinearLayout ll_dock2;

    @ViewInject(R.id.ll_dock3)
    private LinearLayout ll_dock3;

    @ViewInject(R.id.ll_dock4)
    private LinearLayout ll_dock4;


    @ViewInject(R.id.ll_item1)
    private LinearLayout ll_item1;
    @ViewInject(R.id.ll_item2)
    private LinearLayout ll_item2;
    @ViewInject(R.id.ll_item3)
    private LinearLayout ll_item3;


    @ViewInject(R.id.iv_dock1)
    private ImageView iv_dock1;

    @ViewInject(R.id.iv_dock2)
    private ImageView iv_dock2;

    @ViewInject(R.id.iv_dock3)
    private ImageView iv_dock3;

    @ViewInject(R.id.iv_dock4)
    private ImageView iv_dock4;

    @ViewInject(R.id.music_tv_title)
    private TextView music_tv_title;

    @ViewInject(R.id.music_iv_play)
    private ImageView music_iv_play;

    @ViewInject(R.id.music_iv_cover)
    private ImageView music_iv_cover;

    @ViewInject(R.id.music_iv_prew)
    private ImageView music_iv_prew;

    @ViewInject(R.id.music_iv_next)
    private ImageView music_iv_next;

    @ViewInject(R.id.tv_time)
    private TextView tv_time;

    private boolean musicRun = false;

    private void refreshPlay() {
        if (musicRun) {
            if (ThemeManage.self().getTheme() == WHITE) {
                music_iv_play.setImageResource(R.mipmap.ic_pause2);
            } else if (ThemeManage.self().getTheme() == BLACK) {
                music_iv_play.setImageResource(R.mipmap.ic_pause2_b);
            } else {
                music_iv_play.setImageResource(R.mipmap.ic_pause2_cb);
            }
        } else {
            if (ThemeManage.self().getTheme() == WHITE) {
                music_iv_play.setImageResource(R.mipmap.ic_play2);
            } else if (ThemeManage.self().getTheme() == BLACK) {
                music_iv_play.setImageResource(R.mipmap.ic_play2_b);
            } else {
                music_iv_play.setImageResource(R.mipmap.ic_play2_cb);
            }
        }
    }

    @Event(value = {R.id.ll_dock1, R.id.ll_dock2, R.id.ll_dock3, R.id.ll_dock4, R.id.music_ll_play, R.id.music_ll_prew, R.id.music_ll_next, R.id.base})
    private void clickEvent(View v) {
        Log.d(TAG, "clickEvent: " + v);
        switch (v.getId()) {
            case R.id.base: {
                hide();
                break;
            }
            case R.id.music_ll_prew: {
                MusicPlugin.self().pre();
                break;
            }
            case R.id.music_ll_play: {
                MusicPlugin.self().playOrPause();
                break;
            }
            case R.id.music_ll_next: {
                MusicPlugin.self().next();
                break;
            }
            case R.id.ll_dock1: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK1_CLASS);
                if (CommonUtil.isNull(packname)) {
                    ToastManage.self().show("没有选择APP,请跳转至首页选取");
                } else {
                    openDock(packname);
                }
                hide();
                break;
            }
            case R.id.ll_dock2: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK2_CLASS);
                if (CommonUtil.isNull(packname)) {
                    ToastManage.self().show("没有选择APP,请跳转至首页选取");
                } else {
                    openDock(packname);
                }
                hide();
                break;
            }
            case R.id.ll_dock3: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK3_CLASS);
                if (CommonUtil.isNull(packname)) {
                    ToastManage.self().show("没有选择APP,请跳转至首页选取");
                } else {
                    openDock(packname);
                }
                hide();
                break;
            }
            case R.id.ll_dock4: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK4_CLASS);
                if (CommonUtil.isNull(packname)) {
                    ToastManage.self().show("没有选择APP,请跳转至首页选取");
                } else {
                    openDock(packname);
                }
                hide();
                break;
            }
        }
    }

    private void openDock(String clazz) {
        if (!AppInfoManage.self().openApp(clazz)) {
            loadDock();
        }
    }

    private void loadDock() {
        String packname1 = SharedPreUtil.getSharedPreString(SDATA_DOCK1_CLASS);
        if (CommonUtil.isNotNull(packname1)) {
            if (AppInfoManage.self().checkApp(packname1)) {
                iv_dock1.setImageDrawable(AppInfoManage.self().getIcon(packname1));
            } else {
                ToastManage.self().show("dock1加载失败");
                SharedPreUtil.saveSharedPreString(SDATA_DOCK1_CLASS, null);
            }
        }
        String packname2 = SharedPreUtil.getSharedPreString(SDATA_DOCK2_CLASS);
        if (CommonUtil.isNotNull(packname2)) {
            if (AppInfoManage.self().checkApp(packname2)) {
                iv_dock2.setImageDrawable(AppInfoManage.self().getIcon(packname2));
            } else {
                ToastManage.self().show("dock2加载失败");
                SharedPreUtil.saveSharedPreString(SDATA_DOCK2_CLASS, null);
            }
        }

        String packname3 = SharedPreUtil.getSharedPreString(SDATA_DOCK3_CLASS);
        if (CommonUtil.isNotNull(packname3)) {
            if (AppInfoManage.self().checkApp(packname3)) {
                iv_dock3.setImageDrawable(AppInfoManage.self().getIcon(packname3));
            } else {
                ToastManage.self().show("dock3加载失败");
                SharedPreUtil.saveSharedPreString(SDATA_DOCK3_CLASS, null);
            }
        }

        String packname4 = SharedPreUtil.getSharedPreString(SDATA_DOCK4_CLASS);
        if (CommonUtil.isNotNull(packname4)) {
            if (AppInfoManage.self().checkApp(packname4)) {
                iv_dock4.setImageDrawable(AppInfoManage.self().getIcon(packname4));
            } else {
                ToastManage.self().show("dock4加载失败");
                SharedPreUtil.saveSharedPreString(SDATA_DOCK4_CLASS, null);
            }
        }
    }

    @Subscribe(priority = 100)
    public void onEvent(PFkEventAction event) {
        if (!isShow) {
            return;
        }
        if (YLFK.equals(event.getFangkongProtocol())) {
            switch (event.getAction()) {
                case LEFT_TOP_CLICK: {
                    if (selectApp == 0) {
                        selectApp = 6;
                    } else {
                        selectApp = selectApp - 1;
                    }
                    selectApp(selectApp);
                    break;
                }
                case RIGHT_TOP_CLICK: {
                    if (selectApp == 6) {
                        selectApp = 0;
                    } else {
                        selectApp = selectApp + 1;
                    }
                    selectApp(selectApp);
                    break;
                }
                case CENTER_CLICK: {
                    switch (selectApp) {
                        case 0:
                            clickEvent(ll_dock1);
                            break;
                        case 1:
                            clickEvent(ll_dock2);
                            break;
                        case 2:
                            clickEvent(ll_dock3);
                            break;
                        case 3:
                            clickEvent(ll_dock4);
                            break;
                        case 4:
                            clickEvent(music_ll_prew);
                            break;
                        case 5:
                            clickEvent(music_ll_play);
                            break;
                        case 6:
                            clickEvent(music_ll_next);
                            break;
                    }
                    break;
                }
                default: {
                    hide();
                }
            }
            EventBus.getDefault().cancelEventDelivery(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventInfo event) {
        if (music_tv_title != null) {
            if (CommonUtil.isNotNull(event.getTitle())) {
                music_tv_title.setText(event.getTitle());
            } else {
                music_tv_title.setText("音乐");
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
        if (music_iv_play != null) {
            musicRun = event.isRun();
            refreshPlay();
        }
    }

    private long cur_min = 0;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MTimeSecondEvent event) {
        long time = System.currentTimeMillis();
        long time1 = time / MINUTE_MILL;
        if (time1 != cur_min) {
            cur_min = time1;
            Date d = new Date();
            tv_time.setText(DateUtil.dateToString(d, "yyyy年MM月dd日 HH:mm"));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LDockRefreshEvent event) {
        loadDock();
    }
}
