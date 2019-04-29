package com.wow.carlauncher.view.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.AppWidgetManage;
import com.wow.carlauncher.ex.manage.time.event.MTimeHalfSecondEvent;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventNavInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventState;
import com.wow.carlauncher.view.activity.set.event.SEventRefreshAmapPlugin;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_AMAP_PLUGIN;
import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.common.util.ViewUtils.getViewByIds;

public class NaviWin {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static NaviWin instance = new NaviWin();
    }

    public static NaviWin self() {
        return NaviWin.SingletonHolder.instance;
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

        x.view().inject(this, consoleWin);

        EventBus.getDefault().register(this);

        loadPlugin();

        Log.e(TAG + getClass().getSimpleName(), "init ");
    }

    public void show() {
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

    @Event(value = {R.id.base})
    private void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.base: {
                hide();
            }
        }
    }

    @ViewInject(R.id.iv_info)
    private ImageView iv_info;

    @ViewInject(R.id.fl_plugin)
    private FrameLayout fl_plugin;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final SEventRefreshAmapPlugin event) {
        loadPlugin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MTimeHalfSecondEvent event) {
        if (iv_gaode != null && iv_info != null && isShow) {
            iv_info.setImageDrawable(iv_gaode.getDrawable());
        }
    }

    private long lastTime = 0L;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PAmapEventNavInfo event) {
        if (event.getSegRemainDis() <= 100 && event.getRouteRemainDis() > 100) {
            if (!isShow && SharedPreUtil.getBoolean(CommonData.SDATA_USE_NAVI, false)) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PAmapEventState event) {
        if (!event.isRunning()) {
            hide();
        }
    }
}
