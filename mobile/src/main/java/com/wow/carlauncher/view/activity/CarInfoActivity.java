package com.wow.carlauncher.view.activity;

import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarInfo;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import butterknife.BindView;

/**
 * Created by 10124 on 2018/4/25.
 */

public class CarInfoActivity extends BaseActivity {
    @BindView(R.id.tv_info_speed)
    TextView tv_info_speed;

    @BindView(R.id.tv_info_rev)
    TextView tv_info_rev;

    @BindView(R.id.tv_info_wtemp)
    TextView tv_info_wtemp;

    @BindView(R.id.tv_info_oil)
    TextView tv_info_oil;

    @BindView(R.id.tv_tp_lf)
    TextView tv_tp_lf;

    @BindView(R.id.tv_tp_rf)
    TextView tv_tp_rf;

    @BindView(R.id.tv_tp_lb)
    TextView tv_tp_lb;

    @BindView(R.id.tv_tp_rb)
    TextView tv_tp_rb;

    @BindView(R.id.tv_tp_title)
    TextView tv_tp_title;

    @Override
    public void init() {
        setContent(R.layout.activity_car_info);
    }

    @Override
    public void initView() {
        setTitle("车辆信息");

        onEventCall(ObdPlugin.self().getCurrentPObdEventCarInfo());
        onEventCall(ObdPlugin.self().getCurrentPObdEventCarTp());
        refreshObdState();
    }

    private void refreshObdState() {
        if (ObdPlugin.self().isConnect()) {
            setTitle("OBD(已连接)");
            if (ObdPlugin.self().supportTp()) {
                tv_tp_title.setText("胎压数据:");
            } else {
                tv_tp_title.setText("胎压数据(不支持):");
            }
        } else {
            setTitle("OBD(未连接)");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PObdEventConnect event) {
        refreshObdState();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCall(final PObdEventCarInfo event) {
        if (event.getSpeed() != null) {
            String msg = "车速:" + event.getSpeed() + "KM/H";
            tv_info_speed.setText(msg);
        }
        if (event.getRev() != null) {
            String msg = "转速:" + event.getRev() + "R/S";
            tv_info_rev.setText(msg);
        }
        if (event.getWaterTemp() != null) {
            String msg = "水温:" + event.getWaterTemp() + "℃";
            tv_info_wtemp.setText(msg);
        }
        if (event.getOilConsumption() != null) {
            String msg = "油量:" + event.getOilConsumption() + "%";
            tv_info_oil.setText(msg);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCall(final PObdEventCarTp event) {
        if (event.getlFTirePressure() != null && event.getlFTemp() != null) {
            String msg = "左前轮:" + String.format(Locale.CHINA, "%.1f", event.getlFTirePressure()) + "/" + event.getlFTemp() + "℃";
            tv_tp_lf.setText(msg);
        }
        if (event.getlBTemp() != null && event.getlBTirePressure() != null) {
            String msg = "左后轮:" + String.format(Locale.CHINA, "%.1f", event.getlBTirePressure()) + "/" + event.getlBTemp() + "℃";
            tv_tp_lb.setText(msg);
        }

        if (event.getrFTirePressure() != null && event.getrFTemp() != null) {
            String msg = "右前轮:" + String.format(Locale.CHINA, "%.1f", event.getrFTirePressure()) + "/" + event.getrFTemp() + "℃";
            tv_tp_rf.setText(msg);
        }
        if (event.getrBTirePressure() != null && event.getrBTemp() != null) {
            String msg = "右后轮:" + String.format(Locale.CHINA, "%.1f", event.getrBTirePressure()) + "/" + event.getrBTemp() + "℃";
            tv_tp_rb.setText(msg);
        }
    }
}
