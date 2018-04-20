package com.wow.carlauncher.activity.launcher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.plugin.fk.FangkongPlugin;
import com.wow.carlauncher.plugin.fk.FangkongPluginListener;
import com.wow.carlauncher.plugin.music.MusicPlugin;
import com.wow.carlauncher.plugin.obd.ObdPluginListener;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED;
import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LDeviceView extends FrameLayout implements View.OnClickListener {

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

    private void initView() {
        LinearLayout amapView = (LinearLayout) View.inflate(getContext(), R.layout.plugin_device_launcher, null);
        this.addView(amapView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        x.view().inject(this);

        refreshFangkongState();
        refreshObdState();
        fl_fangkong.setOnClickListener(this);
        ll_obd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_fangkong: {
                Log.d(TAG, "onClick: 重连方控");
                FangkongPlugin.self().connect();
                break;
            }
            case R.id.ll_obd: {
                Log.d(TAG, "onClick: 重连OBD");
                break;
            }
        }
    }

    private void refreshFangkongState() {
        String address = SharedPreUtil.getSharedPreString(CommonData.SDATA_FANGKONG_ADDRESS);
        if (CommonUtil.isNotNull(address)) {
            if (AppContext.self().getBluetoothClient().getConnectStatus(address) != STATUS_DEVICE_CONNECTED) {
                tv_fangkongname.setText("方控(已连接)");
                tv_fangkongmoshi.setVisibility(VISIBLE);
            } else {
                tv_fangkongname.setText("方控(未连接)");
            }
        } else {
            tv_fangkongname.setText("方控(未绑定)");
        }
    }

    private void refreshObdState() {
        String address = SharedPreUtil.getSharedPreString(CommonData.SDATA_OBD_ADDRESS);
        if (CommonUtil.isNotNull(address)) {
            if (AppContext.self().getBluetoothClient().getConnectStatus(address) != STATUS_DEVICE_CONNECTED) {
                tv_obdname.setText("OBD(已连接)");
            } else {
                tv_obdname.setText("OBD(未连接)");
            }
        } else {
            tv_obdname.setText("OBD(未绑定)");
        }
    }

    private ObdPluginListener obdPluginListener = new ObdPluginListener() {
        @Override
        public void connect(boolean success) {
            refreshObdState();
        }

        @Override
        public void carRunningInfo(Integer speed, Integer rev, Integer waterTemp, Float oilConsumption) {

        }

        @Override
        public void carTirePressureInfo(Float lFTirePressure, Integer lFTemp, Float rFTirePressure, Integer rFTemp, Float lBTirePressure, Integer lBTemp, Float rBTirePressure, Integer rBTemp) {

        }
    };

    private FangkongPluginListener fangkongPluginListener = new FangkongPluginListener() {
        @Override
        public void connect(boolean success) {
            refreshFangkongState();
        }

        @Override
        public void changeModel(String name) {
            tv_fangkongmoshi.setText(name);
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        FangkongPlugin.self().addListener(fangkongPluginListener);
        refreshFangkongState();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        FangkongPlugin.self().removeListener(fangkongPluginListener);
    }
}
