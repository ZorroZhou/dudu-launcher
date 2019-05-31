package com.wow.carlauncher.view.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.AppWidgetManage;
import com.wow.carlauncher.ex.manage.time.event.TMEventHalfSecond;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventNavInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventState;
import com.wow.carlauncher.view.activity.set.event.SEventRefreshAmapPlugin;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_AMAP_PLUGIN;
import static com.wow.carlauncher.common.util.ViewUtils.getViewByIds;

public class NaviWin {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static NavWatch instance = new NavWatch();
    }

    public static NavWatch watch() {
        return NaviWin.SingletonHolder.instance;
    }

    //这里使用watch加载,确保使用的时候才加载!!
    public static class NavWatch {
        private boolean isShow = false;

        private CarLauncherApplication context;

        private NaviWin naviWin;

        private NavWatch() {

        }

        public void init(CarLauncherApplication context) {
            this.context = context;
            EventBus.getDefault().register(NavWatch.this);
        }

        private long lastTime = 0L;

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onEvent(final PAmapEventNavInfo event) {
            if (event.getSegRemainDis() <= 300 && event.getRouteRemainDis() > 100) {
                if (!isShow && SharedPreUtil.getBoolean(CommonData.SDATA_USE_NAVI_POP, false)) {
                    show();
                    lastTime = System.currentTimeMillis();
                }
            } else if (event.getRouteRemainDis() < 100) {
                hide();
            } else {
                if (System.currentTimeMillis() - lastTime > 5000 && isShow) {
                    hide();
                }
            }
        }

        public void show() {
            if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(context)) {
                return;
            }
            synchronized (lock) {
                if (naviWin == null) {
                    naviWin = new NaviWin();
                    naviWin.init(AppContext.self().getApplication());
                }
            }

            naviWin.show();
        }

        public void hide() {
            naviWin.hide();
        }
    }

    private NaviWin() {

    }

    //窗口管理器
    private WindowManager wm;
    //窗口的布局参数
    private WindowManager.LayoutParams winparams;
    //是否展示了
    private boolean isShow = false;
    private CarLauncherApplication context;
    //窗口视图
    private LinearLayout consoleWin;

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

        consoleWin = (LinearLayout) View.inflate(context, R.layout.popup_navi, null);

        ButterKnife.bind(this, consoleWin);

        EventBus.getDefault().register(this);

        loadPlugin();

        LogEx.d(this, "init time:" + (System.currentTimeMillis() - t1));
    }

    private static final byte[] lock = new byte[0];

    public void show() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(context)) {
            return;
        }
        synchronized (lock) {
            if (consoleWin == null) {
                init(AppContext.self().getApplication());
            }
        }

        if (!isShow && iv_gaode != null) {
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

    private void loadPlugin() {
        int popup = SharedPreUtil.getInteger(APP_WIDGET_AMAP_PLUGIN, 0);
        if (popup != 0) {
            final View amapView = AppWidgetManage.self().getWidgetById(popup);
            fl_plugin.removeAllViews();
            fl_plugin.addView(amapView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (amapView instanceof ViewGroup) {
                View vv = getViewByIds(amapView, new Object[]{"widget_container", "daohang_container", 0, "gongban_daohang_right_blank_container", "daohang_widget_image"});
                if (vv instanceof ImageView) {
                    iv_gaode = (ImageView) vv;
                } else {
                    iv_gaode = null;
                }
            } else {
                iv_gaode = null;
            }
        } else {
            iv_gaode = null;
        }
    }

    private ImageView iv_gaode;

    @OnClick(value = {R.id.base})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.base: {
                hide();
            }
        }
    }

    @BindView(R.id.iv_info)
    ImageView iv_info;

    @BindView(R.id.fl_plugin)
    FrameLayout fl_plugin;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final SEventRefreshAmapPlugin event) {
        loadPlugin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final TMEventHalfSecond event) {
        if (iv_gaode != null && iv_info != null && isShow) {
            iv_info.setImageDrawable(iv_gaode.getDrawable());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PAmapEventState event) {
        if (!event.isRunning()) {
            hide();
        }
    }


}
