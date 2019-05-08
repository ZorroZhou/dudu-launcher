package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarInfo;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.base.BaseEXView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.BLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.WHITE;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LObdView extends BaseEXView {

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
    public void changedTheme(ThemeManage manage) {
        fl_base.setBackgroundResource(manage.getCurrentThemeRes(R.drawable.n_l_item1_bg));
        tv_title.setTextColor(manage.getCurrentThemeColor(R.color.l_text1));
        tv_msg.setTextColor(manage.getCurrentThemeColor(R.color.l_msg));
        iv_error.setImageResource(manage.getCurrentThemeRes(R.mipmap.ic_obderror));

        manage.setViewsBackround(this, new int[]{R.id.ll_cell1, R.id.ll_cell2, R.id.ll_cell3, R.id.ll_cell4}, R.drawable.n_cell_bg);


        manage.setTextViewsColor(this, new int[]{
                R.id.tv_text21,
                R.id.tv_text22,
                R.id.tv_text23,
                R.id.tv_text24,
                R.id.tv_sd,
                R.id.tv_zs,
                R.id.tv_sw,
                R.id.tv_yl
        }, R.color.l_text2);

        p_sd.setProgressDrawable(getResources().getDrawable(manage.getCurrentThemeRes(R.drawable.n_obd_progress)));
        p_zs.setProgressDrawable(getResources().getDrawable(manage.getCurrentThemeRes(R.drawable.n_obd_progress)));
        p_sw.setProgressDrawable(getResources().getDrawable(manage.getCurrentThemeRes(R.drawable.n_obd_progress)));
        p_yl.setProgressDrawable(getResources().getDrawable(manage.getCurrentThemeRes(R.drawable.n_obd_progress)));

        if (currentTheme == WHITE || currentTheme == BLACK) {
            tv_title.setGravity(Gravity.CENTER);
        } else {
            tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }
        LogEx.d(this, "changedTheme: ");
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

    @BindView(R.id.fl_base)
    View fl_base;

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

    @BindView(R.id.iv_error)
    ImageView iv_error;

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
