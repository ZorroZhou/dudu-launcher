package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.manage.skin.SkinManage;
import com.wow.carlauncher.ex.manage.skin.SkinUtil;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarInfo;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.activity.launcher.BaseThemeView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LObdView extends BaseThemeView {

    public LObdView(@NonNull Context context) {
        super(context);
    }

    public LObdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_obd;
    }

    @Override
    public void changedSkin(SkinManage manage) {
        tv_title.setGravity(SkinUtil.analysisItemTitleAlign(manage.getString(R.string.theme_item_title_align)));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        onEvent(new PObdEventConnect().setConnected(ObdPlugin.self().isConnect()));
        onEvent(ObdPlugin.self().getCurrentPObdEventCarInfo());
    }

    @Override
    protected void initView() {
        LogEx.d(this, "initView: ");
    }

    @OnClick(value = {R.id.fl_base})
    void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.fl_base: {
                if (!ObdPlugin.self().isConnect()) {
                    new AlertDialog.Builder(getContext()).setTitle("警告!")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", (dialog2, which2) -> {
                                BleManage.self().getClient().closeBluetooth();
                                TaskExecutor.self().run(() -> BleManage.self().getClient().openBluetooth(), 1000);
                            })
                            .setMessage("是否确认重启蓝牙").show();
                }
                break;
            }
        }
    }

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_sd)
    TextView tv_sd;

    @BindView(R.id.tv_zs)
    TextView tv_zs;

    @BindView(R.id.tv_sw)
    TextView tv_sw;

    @BindView(R.id.tv_yl)
    TextView tv_yl;

    @BindView(R.id.p_sd)
    ProgressBar p_sd;

    @BindView(R.id.p_zs)
    ProgressBar p_zs;

    @BindView(R.id.p_sw)
    ProgressBar p_sw;

    @BindView(R.id.p_yl)
    ProgressBar p_yl;

    @BindView(R.id.tv_msg)
    TextView tv_msg;

    @BindView(R.id.ll_msg)
    View ll_msg;

    @BindView(R.id.ll_obd)
    View ll_obd;

    private boolean connect = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventConnect event) {
        boolean show = false;
        if (event.isConnected()) {
            show = true;
        } else {
            tv_msg.setText(R.string.obd_not_connect);
        }
        connect = show;
        ll_obd.setVisibility(show ? VISIBLE : GONE);
        ll_msg.setVisibility(show ? GONE : VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventCarInfo event) {
        if (!connect) {
            onEvent(new PObdEventConnect().setConnected(ObdPlugin.self().isConnect()));
            connect = true;
        }

        if (event.getSpeed() != null) {
            String msg = event.getSpeed() + "KM/H";
            tv_sd.setText(msg);
            p_sd.setProgress(event.getSpeed());
        }
        if (event.getRev() != null) {
            String msg = event.getRev() + "R/S";
            tv_zs.setText(msg);
            p_zs.setProgress(event.getRev());
        }
        if (event.getWaterTemp() != null) {
            String msg = event.getWaterTemp() + "℃";
            tv_sw.setText(msg);
            p_sw.setProgress(event.getWaterTemp());
        }
        if (event.getOilConsumption() != null) {
            String msg = event.getOilConsumption() + "%";
            tv_yl.setText(msg);
            p_yl.setProgress(event.getOilConsumption());
        }
    }
}
