package com.wow.carlauncher.view.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.CarLauncherApplication;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.baiduVoice.BaiduVoiceAssistant;
import com.wow.carlauncher.ex.manage.baiduVoice.event.MVaAsrStateChange;
import com.wow.carlauncher.ex.manage.baiduVoice.event.MVaNewWordFind;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class VoiceWin {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static VoiceWin instance = new VoiceWin();
    }

    public static VoiceWin self() {
        return VoiceWin.SingletonHolder.instance;
    }

    private VoiceWin() {

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

    private long actionTime = 0;

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

        consoleWin = (LinearLayout) View.inflate(context, R.layout.popup_voice, null);

        x.view().inject(this, consoleWin);
        EventBus.getDefault().register(this);
    }

    public void show() {
        if (!isShow) {
            wm.addView(consoleWin, winparams);
            isShow = true;
            tv_message.setText("你好请讲!");
            actionTime = System.currentTimeMillis();
        }
    }

    public void hide() {
        if (isShow) {
            wm.removeView(consoleWin);
            isShow = false;
        }
    }

    @ViewInject(R.id.tv_message)
    private TextView tv_message;

    @Event(value = {R.id.base})
    private void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.base: {
                BaiduVoiceAssistant.self().stopAsr();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MTimeSecondEvent event) {
        if (isShow) {
            if (System.currentTimeMillis() - actionTime > 30 * 1000) {
                BaiduVoiceAssistant.self().stopAsr();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MVaAsrStateChange event) {
        if (event.isRun()) {
            show();
        } else {
            hide();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MVaNewWordFind event) {
        if (tv_message != null) {
            tv_message.setText(event.getWord());
            actionTime = System.currentTimeMillis();
        }
    }
}
