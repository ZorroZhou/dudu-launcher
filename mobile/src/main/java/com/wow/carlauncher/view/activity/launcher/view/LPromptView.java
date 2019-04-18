package com.wow.carlauncher.view.activity.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventConnect;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.activity.AppMenuActivity;
import com.wow.carlauncher.view.activity.CarInfoActivity;
import com.wow.carlauncher.view.activity.launcher.LauncherActivity;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.base.BaseEBusView;
import com.wow.carlauncher.view.event.EventWifiState;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.DateUtil;
import com.wow.frame.util.NetWorkUtil;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;

import static com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED;
import static com.wow.carlauncher.common.CommonData.MINUTE_MILL;

/**
 * Created by 10124 on 2018/4/22.
 */

public class LPromptView extends BaseEBusView {


    public LPromptView(@NonNull Context context) {
        super(context);
    }

    public LPromptView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LPromptView(@NonNull Context context, Bundle savedInstanceState) {
        super(context, savedInstanceState);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_prompt;
    }

    @ViewInject(R.id.tv_time)
    private TextView tv_time;

    @ViewInject(R.id.iv_carinfo_tp)
    private ImageView iv_carinfo_tp;

    @ViewInject(R.id.iv_obd)
    private ImageView iv_obd;

    @ViewInject(R.id.iv_fk)
    private ImageView iv_fk;

    @ViewInject(R.id.iv_wifi)
    private ImageView iv_wifi;

    @Event(value = {R.id.iv_set, R.id.iv_wifi, R.id.iv_obd, R.id.rl_home, R.id.tv_time})
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
            case R.id.iv_obd: {
                getActivity().startActivity(new Intent(getContext(), CarInfoActivity.class));
                break;
            }
            case R.id.rl_home: {
                getActivity().startActivity(new Intent(getContext(), AppMenuActivity.class));
                break;
            }
            case R.id.tv_time: {
                if (SharedPreUtil.getSharedPreInteger(CommonData.SDATA_APP_THEME, R.style.AppThemeWhile) == R.style.AppThemeWhile) {
                    SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_APP_THEME, R.style.AppThemeBlack);
                    LauncherActivity.activity.recreate();
                } else {
                    SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_APP_THEME, R.style.AppThemeWhile);
                    LauncherActivity.activity.recreate();
                }
//                String packname = SharedPreUtil.getSharedPreString(SDATA_TIME_PLUGIN_OPEN_APP);
//                if (CommonUtil.isNotNull(packname)) {
//                    Intent appIntent = pm.getLaunchIntentForPackage(packname);
//                    if (appIntent != null) {
//                        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        getActivity().startActivity(appIntent);
//                    } else {
//                        ToastManage.self().show("APP丢失");
//                    }
//                }
                break;
            }
        }
    }

    private void refreshWifiState() {
        if (NetWorkUtil.isWifiConnected(getContext())) {
            iv_wifi.setVisibility(VISIBLE);
        } else {
            iv_wifi.setVisibility(GONE);
        }
    }

    private void refreshObdState(PObdEventConnect event) {
        if (event.isConnected()) {
            iv_obd.setVisibility(VISIBLE);
            if (ObdPlugin.self().supportTp()) {
                iv_carinfo_tp.setVisibility(VISIBLE);
            } else {
                iv_carinfo_tp.setVisibility(GONE);
            }
        } else {
            iv_obd.setVisibility(GONE);
            iv_carinfo_tp.setVisibility(GONE);
        }
    }

    private void refreshFKState(final PFkEventConnect event) {
        if (event.isConnected()) {
            iv_fk.setVisibility(VISIBLE);
        } else {
            iv_fk.setVisibility(GONE);
        }
    }

    private void refreshTpState(PObdEventCarTp event) {
        if (ObdPlugin.self().supportTp()) {
            iv_carinfo_tp.setVisibility(VISIBLE);
            boolean warn = false;
            if (event.getlBTirePressure() != null && event.getlBTirePressure() < 2) {
                warn = true;
            }
            if (event.getlFTirePressure() != null && event.getlFTirePressure() < 2) {
                warn = true;
            }
            if (event.getrBTirePressure() != null && event.getrBTirePressure() < 2) {
                warn = true;
            }
            if (event.getrBTirePressure() != null && event.getrBTirePressure() < 2) {
                warn = true;
            }

            if (warn) {
                iv_carinfo_tp.setImageResource(R.mipmap.ic_l_tp_warn);
            } else {
                iv_carinfo_tp.setImageResource(R.mipmap.ic_l_tp);
            }
        } else {
            iv_carinfo_tp.setVisibility(GONE);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                refreshWifiState();
                String fkaddress = SharedPreUtil.getSharedPreString(CommonData.SDATA_FANGKONG_ADDRESS);
                if (CommonUtil.isNotNull(fkaddress) && BleManage.self().client().getConnectStatus(fkaddress) != STATUS_DEVICE_CONNECTED) {
                    refreshFKState(new PFkEventConnect().setConnected(true));
                } else {
                    refreshFKState(new PFkEventConnect().setConnected(false));
                }

                String obdaddress = SharedPreUtil.getSharedPreString(CommonData.SDATA_OBD_ADDRESS);
                if (CommonUtil.isNotNull(obdaddress) && BleManage.self().client().getConnectStatus(obdaddress) != STATUS_DEVICE_CONNECTED) {
                    refreshObdState(new PObdEventConnect().setConnected(true));
                } else {
                    refreshObdState(new PObdEventConnect().setConnected(false));
                }

                refreshTpState(ObdPlugin.self().getCurrentPObdEventCarTp());
            }
        });
    }

    private Activity getActivity() {
        return (Activity) getContext();
    }

    private long cur_min = 0L;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MTimeSecondEvent event) {
        long time = System.currentTimeMillis();
        long time1 = time / MINUTE_MILL;
        if (time1 != cur_min) {
            cur_min = time1;
            Date d = new Date();
            String date = DateUtil.dateToString(d, "MM月dd日 " + DateUtil.getWeekOfDate(d) + " HH:mm");
            if (date.startsWith("0")) {
                date = date.substring(1);
            }
            this.tv_time.setText(date);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PFkEventConnect event) {
        if (event.isConnected()) {
            iv_fk.setVisibility(VISIBLE);
        } else {
            iv_fk.setVisibility(GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventConnect event) {
        refreshObdState(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final EventWifiState event) {
        if (event.isUsable()) {
            iv_wifi.setVisibility(VISIBLE);
        } else {
            iv_wifi.setVisibility(GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(final PObdEventCarTp event) {
        post(new Runnable() {
            @Override
            public void run() {
                refreshTpState(event);
            }
        });
    }
}
