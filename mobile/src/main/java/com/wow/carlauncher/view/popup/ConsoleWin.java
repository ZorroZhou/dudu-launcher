package com.wow.carlauncher.view.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
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
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.DateUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.appInfo.event.MAppInfoRefreshShowEvent;
import com.wow.carlauncher.ex.manage.time.event.TMEvent3Second;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventAction;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventCoverRefresh;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wow.carlauncher.common.CommonData.MINUTE_MILL;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK1_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK2_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK3_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK4_CLASS;
import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.ex.plugin.fk.FangkongProtocolEnum.YLFK;
import static com.wow.carlauncher.ex.plugin.fk.protocol.YiLianProtocol.CENTER_CLICK;
import static com.wow.carlauncher.ex.plugin.fk.protocol.YiLianProtocol.LEFT_TOP_CLICK;
import static com.wow.carlauncher.ex.plugin.fk.protocol.YiLianProtocol.RIGHT_TOP_CLICK;

public class ConsoleWin {
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
        long t1 = System.currentTimeMillis();
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
        if (SharedPreUtil.getBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, true)) {
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

        consoleWin = (RelativeLayout) View.inflate(context, R.layout.popup_console, null);

        ButterKnife.bind(this, consoleWin);

        EventBus.getDefault().register(this);

        loadDock(false);
        LogEx.d(this, "init time:" + (System.currentTimeMillis() - t1));
    }

    private static final byte[] lock = new byte[0];

    public void show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(AppContext.self().getApplication())) {
            return;
        }
        synchronized (lock) {
            if (consoleWin == null) {
                init(AppContext.self().getApplication());
            }
        }
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

    private int selectApp = 0;

    private void selectApp(int selectApp) {
        this.selectApp = selectApp;
        TaskExecutor.self().autoPost(() -> {
            ll_dock1.setBackgroundResource(android.R.color.transparent);
            ll_dock2.setBackgroundResource(android.R.color.transparent);
            ll_dock3.setBackgroundResource(android.R.color.transparent);
            ll_dock4.setBackgroundResource(android.R.color.transparent);
            music_ll_prew.setBackgroundResource(android.R.color.transparent);
            music_ll_play.setBackgroundResource(android.R.color.transparent);
            music_ll_next.setBackgroundResource(android.R.color.transparent);
            if (selectApp == 0) {
                ll_dock1.setBackgroundResource(R.drawable.theme_console_select);
            } else if (selectApp == 1) {
                ll_dock2.setBackgroundResource(R.drawable.theme_console_select);
            } else if (selectApp == 2) {
                ll_dock3.setBackgroundResource(R.drawable.theme_console_select);
            } else if (selectApp == 3) {
                ll_dock4.setBackgroundResource(R.drawable.theme_console_select);
            } else if (selectApp == 4) {
                music_ll_prew.setBackgroundResource(R.drawable.theme_console_select);
            } else if (selectApp == 5) {
                music_ll_play.setBackgroundResource(R.drawable.theme_console_select);
            } else if (selectApp == 6) {
                music_ll_next.setBackgroundResource(R.drawable.theme_console_select);
            }
        });
    }

    @BindView(R.id.music_ll_prew)
    LinearLayout music_ll_prew;

    @BindView(R.id.music_ll_play)
    LinearLayout music_ll_play;

    @BindView(R.id.music_ll_next)
    LinearLayout music_ll_next;

    @BindView(R.id.ll_dock1)
    LinearLayout ll_dock1;

    @BindView(R.id.ll_dock2)
    LinearLayout ll_dock2;

    @BindView(R.id.ll_dock3)
    LinearLayout ll_dock3;

    @BindView(R.id.ll_dock4)
    LinearLayout ll_dock4;

    @BindView(R.id.iv_dock1)
    ImageView iv_dock1;

    @BindView(R.id.iv_dock2)
    ImageView iv_dock2;

    @BindView(R.id.iv_dock3)
    ImageView iv_dock3;

    @BindView(R.id.iv_dock4)
    ImageView iv_dock4;

    @BindView(R.id.music_tv_title)
    TextView music_tv_title;

    @BindView(R.id.music_iv_play)
    ImageView music_iv_play;

    @BindView(R.id.music_iv_cover)
    ImageView music_iv_cover;

    @BindView(R.id.music_iv_prew)
    ImageView music_iv_prew;

    @BindView(R.id.music_iv_next)
    ImageView music_iv_next;

    @BindView(R.id.tv_time)
    TextView tv_time;

    private boolean musicRun = false;

    private void refreshPlay() {
        if (musicRun) {
            music_iv_play.setImageResource(R.drawable.theme_ic_pause2);
        } else {
            music_iv_play.setImageResource(R.drawable.theme_ic_play2);
        }
    }

    @OnClick(value = {R.id.ll_dock1, R.id.ll_dock2, R.id.ll_dock3, R.id.ll_dock4, R.id.music_ll_play, R.id.music_ll_prew, R.id.music_ll_next, R.id.base})
    public void clickEvent(View v) {
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
                String packname = SharedPreUtil.getString(SDATA_DOCK1_CLASS);
                if (CommonUtil.isNull(packname)) {
                    ToastManage.self().show("没有选择APP,请跳转至首页选取");
                } else {
                    openDock(packname);
                }
                hide();
                break;
            }
            case R.id.ll_dock2: {
                String packname = SharedPreUtil.getString(SDATA_DOCK2_CLASS);
                if (CommonUtil.isNull(packname)) {
                    ToastManage.self().show("没有选择APP,请跳转至首页选取");
                } else {
                    openDock(packname);
                }
                hide();
                break;
            }
            case R.id.ll_dock3: {
                String packname = SharedPreUtil.getString(SDATA_DOCK3_CLASS);
                if (CommonUtil.isNull(packname)) {
                    ToastManage.self().show("没有选择APP,请跳转至首页选取");
                } else {
                    openDock(packname);
                }
                hide();
                break;
            }
            case R.id.ll_dock4: {
                String packname = SharedPreUtil.getString(SDATA_DOCK4_CLASS);
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
            ToastManage.self().show("APP打开失败,可能需要重新选择");
        }
    }

    private void loadDock(boolean remove) {
        String packname1 = SharedPreUtil.getString(SDATA_DOCK1_CLASS);
        if (CommonUtil.isNotNull(packname1) && AppInfoManage.self().checkApp(packname1)) {
            AppInfoManage.self().setIconWithSkin(iv_dock1, packname1);
        } else {
            iv_dock1.setImageResource(R.drawable.theme_add_app);
            if (remove) {
                SharedPreUtil.saveString(SDATA_DOCK1_CLASS, "");
            }
        }

        String packname2 = SharedPreUtil.getString(SDATA_DOCK2_CLASS);
        if (CommonUtil.isNotNull(packname2) && AppInfoManage.self().checkApp(packname2)) {
            AppInfoManage.self().setIconWithSkin(iv_dock2, packname2);
        } else {
            iv_dock2.setImageResource(R.drawable.theme_add_app);
            if (remove) {
                SharedPreUtil.saveString(SDATA_DOCK2_CLASS, "");
            }
        }

        String packname3 = SharedPreUtil.getString(SDATA_DOCK3_CLASS);
        if (CommonUtil.isNotNull(packname3) && AppInfoManage.self().checkApp(packname3)) {
            AppInfoManage.self().setIconWithSkin(iv_dock3, packname3);
        } else {
            iv_dock3.setImageResource(R.drawable.theme_add_app);
            if (remove) {
                SharedPreUtil.saveString(SDATA_DOCK3_CLASS, "");
            }
        }

        String packname4 = SharedPreUtil.getString(SDATA_DOCK4_CLASS);
        if (CommonUtil.isNotNull(packname4) && AppInfoManage.self().checkApp(packname4)) {
            AppInfoManage.self().setIconWithSkin(iv_dock4, packname4);
        } else {
            iv_dock4.setImageResource(R.drawable.theme_add_app);
            if (remove) {
                SharedPreUtil.saveString(SDATA_DOCK4_CLASS, "");
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
    public void onEvent(final PMusicEventCoverRefresh event) {
        if (event.isHave()) {
            ImageManage.self().loadImage(event.getUrl(), music_iv_cover, R.drawable.theme_music_dcover);
        } else {
            music_iv_cover.setImageResource(R.drawable.theme_music_dcover);
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
    public void onEvent(TMEvent3Second event) {
        long time = System.currentTimeMillis();
        long time1 = time / MINUTE_MILL;
        if (time1 != cur_min) {
            cur_min = time1;
            Date d = new Date();
            tv_time.setText(DateUtil.dateToString(d, "yyyy年MM月dd日 HH:mm"));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MAppInfoRefreshShowEvent event) {
        loadDock(true);
    }
}
