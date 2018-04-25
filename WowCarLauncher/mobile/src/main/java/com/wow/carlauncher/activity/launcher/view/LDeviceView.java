package com.wow.carlauncher.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.plugin.fk.FangkongPlugin;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventConnect;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventModel;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarInfo;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.Subscribe;
import org.xutils.view.annotation.ViewInject;

import static com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED;
import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LDeviceView extends LBaseView implements View.OnClickListener {

    public LDeviceView(@NonNull Context context) {
        super(context);
        initView();
    }

    public LDeviceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @ViewInject(R.id.tv_fangkongname)
    private TextView tv_fangkongname;

    @ViewInject(R.id.tv_fangkongmoshi)
    private TextView tv_fangkongmoshi;

    @ViewInject(R.id.fl_fangkong)
    private FrameLayout fl_fangkong;


    @ViewInject(R.id.tv_obdname)
    private TextView tv_obdname;

    @ViewInject(R.id.ll_obd)
    private LinearLayout ll_obd;

    @ViewInject(R.id.tv_tp_lf)
    private TextView tv_tp_lf;

    @ViewInject(R.id.tv_tp_rf)
    private TextView tv_tp_rf;

    @ViewInject(R.id.tv_tp_rb)
    private TextView tv_tp_rb;

    @ViewInject(R.id.tv_tp_lb)
    private TextView tv_tp_lb;

    @ViewInject(R.id.tv_tp_title)
    private TextView tv_tp_title;

    @ViewInject(R.id.tv_zs)
    private TextView tv_zs;

    @ViewInject(R.id.tv_cs)
    private TextView tv_cs;

    @ViewInject(R.id.tv_sw)
    private TextView tv_sw;

    @ViewInject(R.id.tv_youliang)
    private TextView tv_youliang;

    private void initView() {
        addContent(R.layout.plugin_device_launcher);

        refreshFangkongState();
        refreshObdState();
        fl_fangkong.setOnClickListener(this);
        ll_obd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    private void refreshFangkongState() {
        String address = SharedPreUtil.getSharedPreString(CommonData.SDATA_FANGKONG_ADDRESS);
        if (CommonUtil.isNotNull(address)) {
            if (BleManage.self().client().getConnectStatus(address) == STATUS_DEVICE_CONNECTED) {
                tv_fangkongname.setText("方控(已连接)");
                tv_fangkongmoshi.setText(FangkongPlugin.self().getModelName());
            } else {
                tv_fangkongname.setText("方控(未连接)");
                tv_fangkongmoshi.setText("");
            }
        } else {
            tv_fangkongname.setText("方控(未绑定)");
            tv_fangkongmoshi.setText("");
        }
    }

    private void refreshObdState() {
        String address = SharedPreUtil.getSharedPreString(CommonData.SDATA_OBD_ADDRESS);
        if (CommonUtil.isNotNull(address)) {
            Log.d(TAG, "refreshObdState: " + BleManage.self().client().getConnectStatus(address) + " " + STATUS_DEVICE_CONNECTED);
            if (BleManage.self().client().getConnectStatus(address) == STATUS_DEVICE_CONNECTED) {
                tv_obdname.setText("OBD(已连接)");
                if (ObdPlugin.self().supportTp()) {
                    tv_tp_title.setText("胎压数据:");
                } else {
                    tv_tp_title.setText("胎压数据(不支持):");
                }
            } else {
                tv_obdname.setText("OBD(未连接)");
            }
        } else {
            tv_obdname.setText("OBD(未绑定)");
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        refreshFangkongState();
        refreshObdState();
    }

    @Subscribe
    public void onEventMainThread(final PFkEventConnect event) {
        refreshFangkongState();
    }

    @Subscribe
    public void onEventMainThread(final PFkEventModel event) {
        tv_fangkongmoshi.setText(event.getModelName());
    }

    @Subscribe
    public void onEventMainThread(PObdEventConnect event) {
        refreshObdState();
    }

    @Subscribe
    public void onEventMainThread(PObdEventCarInfo event) {
        if (event.getSpeed() != null) {
            tv_cs.setText("车速:" + event.getSpeed() + "KM/H");
        }
        if (event.getRev() != null) {
            tv_zs.setText("转速:" + event.getRev() + "R/S");
        }
        if (event.getWaterTemp() != null) {
            tv_sw.setText("水温:" + event.getWaterTemp() + "℃");
        }
        if (event.getOilConsumption() != null) {
            tv_youliang.setText("油量:" + event.getOilConsumption() + "%");
        }
    }

    @Subscribe
    public void onEventMainThread(PObdEventCarTp event) {
        if (event.getlFTirePressure() != null && event.getlFTemp() != null) {
            tv_tp_lf.setText(String.format("%.1f", event.getlFTirePressure()) + "/" + event.getlFTemp() + "℃");
        }

        if (event.getrFTirePressure() != null && event.getrFTemp() != null) {
            tv_tp_rf.setText(String.format("%.1f", event.getrFTirePressure()) + "/" + event.getrFTemp() + "℃");
        }

        if (event.getlBTemp() != null && event.getlBTirePressure() != null) {
            tv_tp_lb.setText(String.format("%.1f", event.getlBTirePressure()) + "/" + event.getlBTemp() + "℃");
        }

        if (event.getrBTirePressure() != null && event.getrBTemp() != null) {
            tv_tp_rb.setText(String.format("%.1f", event.getrBTirePressure()) + "/" + event.getrBTemp() + "℃");
        }
    }

}
