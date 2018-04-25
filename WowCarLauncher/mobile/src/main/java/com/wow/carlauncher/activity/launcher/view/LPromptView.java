package com.wow.carlauncher.activity.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.activity.set.SetActivity;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.manage.ble.event.BleEventSearch;
import com.wow.carlauncher.event.EventWifiState;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventConnect;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.frame.util.DateUtil;
import com.wow.frame.util.NetWorkUtil;

import org.greenrobot.eventbus.Subscribe;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/22.
 */

public class LPromptView extends LBaseView {


    public LPromptView(@NonNull Context context) {
        super(context);
        initView();
    }

    public LPromptView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @ViewInject(R.id.tv_time)
    private TextView tv_time;

    @ViewInject(R.id.iv_carinfo_tp)
    private ImageView iv_carinfo_tp;

    @ViewInject(R.id.iv_obd)
    private ImageView iv_obd;

    @ViewInject(R.id.iv_ble)
    private ImageView iv_ble;

    @ViewInject(R.id.iv_fk)
    private ImageView iv_fk;

    @ViewInject(R.id.iv_wifi)
    private ImageView iv_wifi;

    private void initView() {
        addContent(R.layout.content_l_prompt);


        refreshWifiState();
        refreshBleState();
    }

    @Event(value = {R.id.iv_set, R.id.iv_wifi, R.id.iv_ble})
    private void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_set: {
                getActivity().startActivity(new Intent(getContext(), SetActivity.class));
                break;
            }
            case R.id.iv_wifi: {
                getActivity().startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); //直接进入手机中的wifi网络设置界面
                break;
            }
            case R.id.iv_ble: {
//                /BleManage.self().startSearch();
                Log.d(TAG, "clickEvent: 重新开启扫描");
                break;
            }
        }
    }

    private void refreshBleState() {
        if (BleManage.self().isSearching()) {
            iv_ble.setVisibility(VISIBLE);
        } else {
            iv_ble.setVisibility(INVISIBLE);
        }
    }

    private void refreshWifiState() {
        if (NetWorkUtil.isWifiConnected(getContext())) {
            iv_wifi.setVisibility(VISIBLE);
        } else {
            iv_wifi.setVisibility(INVISIBLE);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTimer();
        refreshWifiState();
        refreshBleState();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimer();
    }

    private Timer timer;

    private void startTimer() {
        Log.e(TAG, "startTimer: ");
        stopTimer();
        timer = new Timer();
        int yifenzhong = 1000 * 60;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                setTime();
            }
        }, yifenzhong - System.currentTimeMillis() % yifenzhong, yifenzhong);
        setTime();
    }

    private void setTime() {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                Date d = new Date();
                LPromptView.this.tv_time.setText(DateUtil.dateToString(d, "HH:mm   " + DateUtil.getWeekOfDate(d) + " yyyy/MM/dd "));
            }
        });
    }

    private void stopTimer() {
        if (timer != null) {
            Log.e(TAG, "stopTimer: ");
            timer.cancel();
            timer = null;
        }
    }

    private Activity getActivity() {
        return (Activity) getContext();
    }

    @Subscribe
    public void onEventMainThread(final PFkEventConnect event) {
        if (event.isConnected()) {
            iv_obd.setVisibility(VISIBLE);
        } else {
            iv_obd.setVisibility(INVISIBLE);
        }
    }

    @Subscribe
    public void onEventMainThread(final PObdEventConnect event) {
        if (event.isConnected()) {
            iv_fk.setVisibility(VISIBLE);
        } else {
            iv_fk.setVisibility(INVISIBLE);
        }
    }

    @Subscribe
    public void onEventMainThread(final EventWifiState event) {
        if (event.isUsable()) {
            iv_wifi.setVisibility(VISIBLE);
        } else {
            iv_wifi.setVisibility(INVISIBLE);
        }
    }

    @Subscribe
    public void onEventMainThread(final BleEventSearch event) {
        Log.d(TAG, "onEventMainThread: asdfasdads!!!!!!!!!!!!!!!!!");
        if (event.isSearch()) {
            iv_ble.setVisibility(VISIBLE);
        } else {
            iv_ble.setVisibility(INVISIBLE);
        }
    }
}
