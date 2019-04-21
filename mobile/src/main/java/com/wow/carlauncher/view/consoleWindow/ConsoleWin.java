package com.wow.carlauncher.view.consoleWindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.musicCover.MusicCoverManage;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.musicCover.MusicCoverRefresh;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.wow.carlauncher.common.CommonData.SDATA_DOCK1_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK2_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK3_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK4_CLASS;
import static com.wow.carlauncher.common.CommonData.TAG;

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
    private View consoleWin;
    //插件试图
    private LinearLayout pluginHome;
    private TextView tv_time;

    public void init(CarLauncherApplication context) {

        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        EventBus.getDefault().register(this);

        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);


        winparams = new WindowManager.LayoutParams();
        // 类型
        winparams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
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

        consoleWin = View.inflate(context, R.layout.console_window, null);

        x.view().inject(this, consoleWin);

        loadDock();
    }


    public void show() {
        if (!isShow) {
            wm.addView(consoleWin, winparams);
            isShow = true;
        }
    }

    public void hide() {
        if (isShow) {
            wm.removeView(consoleWin);
            isShow = false;
        }
    }

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
            if (event.isRun()) {
                music_iv_play.setImageResource(R.mipmap.ic_pause2);
            } else {
                music_iv_play.setImageResource(R.mipmap.ic_play2);
            }
        }
    }

}
