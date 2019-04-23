package com.wow.carlauncher.view.activity.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.DateUtil;
import com.wow.carlauncher.common.util.NetWorkUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventConnect;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.activity.AppMenuActivity;
import com.wow.carlauncher.view.activity.CarInfoActivity;
import com.wow.carlauncher.view.activity.launcher.event.LItemToFristEvent;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.base.BaseEXView;
import com.wow.carlauncher.view.consoleWindow.ConsoleWin;
import com.wow.carlauncher.view.event.EventWifiState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;

import per.goweii.anypermission.AnyPermission;
import per.goweii.anypermission.RequestListener;

import static com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED;
import static com.wow.carlauncher.common.CommonData.MINUTE_MILL;

/**
 * Created by 10124 on 2018/4/22.
 */

public class LPromptView extends BaseEXView {


    public LPromptView(@NonNull Context context) {
        super(context);
    }

    public LPromptView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_prompt;
    }

    @Override
    protected void initView() {
        pm = getActivity().getPackageManager();
    }

    @Override
    public void changedTheme(ThemeManage manage) {
        fl_base.setBackgroundResource(manage.getCurrentThemeRes(R.drawable.n_prompt_bg));
        tv_time.setTextColor(manage.getCurrentThemeColor(R.color.l_text1));
        iv_home.setImageResource(manage.getCurrentThemeRes(R.mipmap.n_home));


        iv_obd.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_l_obd));
        iv_carinfo_tp.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_l_tp));
        iv_fk.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_l_fk));
        iv_wifi.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_l_wifi));
        iv_set.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_l_set));
    }

    private PackageManager pm;

    @ViewInject(R.id.fl_base)
    private View fl_base;

    @ViewInject(R.id.tv_time)
    private TextView tv_time;

    @ViewInject(R.id.iv_carinfo_tp)
    private ImageView iv_carinfo_tp;

    @ViewInject(R.id.iv_home)
    private ImageView iv_home;

    @ViewInject(R.id.iv_set)
    private ImageView iv_set;

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
                EventBus.getDefault().post(new LItemToFristEvent());
                break;
            }
            case R.id.tv_time: {
                AnyPermission.with(getContext()).overlay()
                        .onWithoutPermission((data, executor) -> {
                            new AlertDialog.Builder(getContext()).setTitle("警告!")
                                    .setNegativeButton("取消", (dialog, which) -> executor.cancel())
                                    .setPositiveButton("确定", (dialog2, which2) -> executor.execute())
                                    .setMessage("请给与车机助手悬浮窗权限,否则无法使用这个功能").show();
                        })
                        .request(new RequestListener() {
                            @Override
                            public void onSuccess() {
                                ConsoleWin.self().show();
                            }

                            @Override
                            public void onFailed() {
                                ToastManage.self().show("没有悬浮窗权限!");
                            }
                        });
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventCarTp event) {
        refreshTpState(event);
    }
}
