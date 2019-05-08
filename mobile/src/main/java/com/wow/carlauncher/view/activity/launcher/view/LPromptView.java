package com.wow.carlauncher.view.activity.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.DateUtil;
import com.wow.carlauncher.common.util.NetWorkUtil;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.location.event.MNewLocationEvent;
import com.wow.carlauncher.ex.manage.time.event.MTime3SecondEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.fk.FangkongPlugin;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventConnect;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.activity.CarInfoActivity;
import com.wow.carlauncher.view.activity.launcher.LayoutEnum;
import com.wow.carlauncher.view.activity.launcher.event.LItemToFristEvent;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.base.BaseEXView;
import com.wow.carlauncher.view.event.EventWifiState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;

import static com.wow.carlauncher.common.CommonData.MINUTE_MILL;
import static com.wow.carlauncher.common.CommonData.TAG;

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
    public void changedTheme(ThemeManage manage) {
        if (this.layoutEnum.equals(LayoutEnum.LAYOUT1)) {
            fl_base.setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_prompt_bg));
        } else {
            fl_base.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        }

        tv_time.setTextColor(manage.getCurrentThemeColor(R.color.l_text1));

        iv_location.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_l_location));
        iv_obd.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_l_obd));
        iv_carinfo_tp.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_l_tp));
        iv_fk.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_l_fk));
        iv_wifi.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_l_wifi));
        iv_set.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_l_set));
        Log.e(TAG + getClass().getSimpleName(), "changedTheme: ");
    }

    @Override
    protected void initView() {
        Log.e(TAG + getClass().getSimpleName(), "initView: ");
    }

    @ViewInject(R.id.fl_base)
    private View fl_base;

    @ViewInject(R.id.tv_time)
    private TextView tv_time;

    @ViewInject(R.id.iv_carinfo_tp)
    private ImageView iv_carinfo_tp;

    @ViewInject(R.id.iv_location)
    private ImageView iv_location;

    @ViewInject(R.id.iv_set)
    private ImageView iv_set;

    @ViewInject(R.id.iv_obd)
    private ImageView iv_obd;

    @ViewInject(R.id.iv_fk)
    private ImageView iv_fk;

    @ViewInject(R.id.iv_wifi)
    private ImageView iv_wifi;

    @Event(value = {R.id.iv_set, R.id.iv_wifi, R.id.iv_obd, R.id.tv_time, R.id.tv_time, R.id.iv_location, R.id.iv_wifi})
    private void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_location: {
                if (locationEvent != null) {
                    ToastManage.self().show("当前定位:" + locationEvent.getCity());
                }
                break;
            }
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
            case R.id.iv_carinfo_tp: {
                getActivity().startActivity(new Intent(getContext(), CarInfoActivity.class));
                break;
            }
            case R.id.tv_time: {
                EventBus.getDefault().post(new LItemToFristEvent());
                break;
            }
        }
    }

    @Event(value = {R.id.iv_obd, R.id.iv_fk}, type = OnLongClickListener.class)
    private void clickLongEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_obd: {
                ObdPlugin.self().disconnect();
                break;
            }
            case R.id.iv_fk: {
                FangkongPlugin.self().disconnect();
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
        if (ObdPlugin.self().isConnect() && ObdPlugin.self().supportTp()) {
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
                iv_carinfo_tp.setImageResource(ThemeManage.self().getCurrentThemeRes(R.mipmap.ic_l_tp));
            }
        } else {
            iv_carinfo_tp.setVisibility(GONE);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        x.task().autoPost(() -> {
            refreshWifiState();
            refreshFKState(new PFkEventConnect().setConnected(FangkongPlugin.self().isConnect()));
            refreshObdState(new PObdEventConnect().setConnected(ObdPlugin.self().isConnect()));
            refreshTpState(ObdPlugin.self().getCurrentPObdEventCarTp());
        });
    }

    private LayoutEnum layoutEnum = LayoutEnum.LAYOUT1;

    public void setLayoutEnum(LayoutEnum layoutEnum) {
        if (layoutEnum == null) {
            return;
        }
        if (!layoutEnum.equals(this.layoutEnum)) {
            this.layoutEnum = layoutEnum;
            if (this.layoutEnum.equals(LayoutEnum.LAYOUT1)) {
                fl_base.setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_prompt_bg));
            } else {
                fl_base.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
            }
        }
    }

    private Activity getActivity() {
        return (Activity) getContext();
    }

    private long cur_min = 0L;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MTime3SecondEvent event) {
        long time = System.currentTimeMillis();
        long time1 = time / MINUTE_MILL;
        if (time1 != cur_min) {
            cur_min = time1;
            Date d = new Date();
            String date = DateUtil.dateToString(d, "yyyy年 MM月 dd日 " + DateUtil.getWeekOfDate(d) + " HH:mm");
            if (date.startsWith("0")) {
                date = date.substring(1);
            }
            this.tv_time.setText(date);
        }
    }

    private MNewLocationEvent locationEvent;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MNewLocationEvent event) {
        this.locationEvent = event;
        iv_location.setVisibility(VISIBLE);
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
