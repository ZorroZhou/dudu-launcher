package com.wow.carlauncher.plugin.obd;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.ex.BleManage;
import com.wow.carlauncher.common.ex.ToastManage;
import com.wow.carlauncher.common.ex.event.BleEventDeviceChange;
import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.obd.protocol.GoodDriverTPProtocol;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.x;

import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED;
import static com.wow.carlauncher.common.CommonData.SDATA_OBD_CONTROLLER;

/**
 * Created by 10124 on 2017/11/4.
 */

public class ObdPlugin extends BasePlugin<ObdPluginListener> {
    public static final String TAG = "WOW_CAR_OBD";

    private static ObdPlugin self;

    public static ObdPlugin self() {
        if (self == null) {
            self = new ObdPlugin();
        }
        return self;
    }

    private ObdPlugin() {

    }

    private BleConnectOptions options;

    private ObdProtocol obdProtocol;

    private ObdProtocolListener obdProtocolListener = new ObdProtocolListener() {
        @Override
        public void write(byte[] req) {
            ObdPlugin.this.write(req);
        }

        @Override
        public boolean isConnect() {
            Log.d(TAG, "检查是否连接: " + Constants.getStatusText(BleManage.self().client().getConnectStatus(obdProtocol.getAddress())));
            if (obdProtocol != null && BleManage.self().client().getConnectStatus(obdProtocol.getAddress()) == STATUS_DEVICE_CONNECTED) {
                return true;
            }
            return false;
        }

        @Override
        public void carRunningInfo(final Integer speed, final Integer rev, final Integer waterTemp, final Integer oilConsumption) {
            runListener(new ListenerRuner<ObdPluginListener>() {
                @Override
                public void run(ObdPluginListener obdPluginListener) {
                    obdPluginListener.carRunningInfo(speed, rev, waterTemp, oilConsumption);
                }
            });
        }

        @Override
        public void carTirePressureInfo(final Float lFTirePressure, final Integer lFTemp,
                                        final Float rFTirePressure, final Integer rFTemp,
                                        final Float lBTirePressure, final Integer lBTemp,
                                        final Float rBTirePressure, final Integer rBTemp) {
            runListener(new ListenerRuner<ObdPluginListener>() {
                @Override
                public void run(ObdPluginListener obdPluginListener) {
                    obdPluginListener.carTirePressureInfo(lFTirePressure, lFTemp, rFTirePressure, rFTemp, lBTirePressure, lBTemp, rBTirePressure, rBTemp);
                }
            });
        }
    };

    private final BleConnectStatusListener bleConnectStatusListener = new BleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (mac.equals(obdProtocol.getAddress())) {
                if (status == STATUS_CONNECTED) {
                    connectCallback(true);
                } else {
                    connectCallback(false);
                    obdProtocol.stop();
                }
            }
        }
    };

    public void init(Context context) {
        super.init(context);
        options = new BleConnectOptions.Builder()
                .setConnectRetry(Integer.MAX_VALUE)
                .setConnectTimeout(5000)   // 连接超时5s
                .setServiceDiscoverRetry(Integer.MAX_VALUE)
                .setServiceDiscoverTimeout(5000)  // 发现服务超时5s
                .build();
        EventBus.getDefault().register(this);
        BleManage.self().forceCallBack();
    }

    private boolean connecting = false;

    private synchronized void connect() {
        final String address = SharedPreUtil.getSharedPreString(CommonData.SDATA_OBD_ADDRESS);
        Log.d(TAG, "connect: " + Constants.getStatusText(BleManage.self().client().getConnectStatus(address)) + "  " + CommonUtil.isNull(address) + "  " + Constants.getStatusText(BleManage.self().client().getConnectStatus(address)));
        if (connecting || CommonUtil.isNull(address) || BleManage.self().client().getConnectStatus(address) == STATUS_DEVICE_CONNECTED) {
            return;
        }
        connecting = true;
        ObdProtocolEnum p1 = ObdProtocolEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_OBD_CONTROLLER, ObdProtocolEnum.YJ_TYB.getId()));
        switch (p1) {
            case YJ_TYB: {
                obdProtocol = new GoodDriverTPProtocol(getContext(), address, obdProtocolListener);
                break;
            }
            default:
                obdProtocol = new GoodDriverTPProtocol(getContext(), address, obdProtocolListener);
                break;
        }
        Log.d(TAG, "开始连接");
        BleManage.self().client().clearRequest(obdProtocol.getAddress(), 0);
        BleManage.self().client().refreshCache(obdProtocol.getAddress());
        BleManage.self().client().registerConnectStatusListener(obdProtocol.getAddress(), bleConnectStatusListener);
        BleManage.self().client().connect(obdProtocol.getAddress(), options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                if (code == REQUEST_SUCCESS) {
                    BleManage.self().client().notify(obdProtocol.getAddress(),
                            obdProtocol.getNotifyService(),
                            obdProtocol.getNotifyCharacter(),
                            new BleNotifyResponse() {
                                @Override
                                public void onNotify(UUID service, UUID character, byte[] msg) {
                                    if (obdProtocol != null) {
                                        obdProtocol.receiveMessage(msg);
                                    }
                                }

                                @Override
                                public void onResponse(int code) {
                                    connecting = false;
                                    Log.d(TAG, "查询状态: " + Constants.getStatusText(BleManage.self().client().getConnectStatus(obdProtocol.getAddress())));

                                    Log.d(TAG, "onResponse: " + code);
                                    if (code == REQUEST_SUCCESS) {
                                        ToastManage.self().show("OBD连接成功");
                                        obdProtocol.run();
                                    } else {
                                        BleManage.self().client().disconnect(obdProtocol.getAddress());
                                    }
                                }
                            });
                } else {
                    connecting = false;
                    BleManage.self().forceCallBack();
                    Log.d(TAG, "onResponse: 方控连接失败!!!");
                }
            }
        });
    }

    public synchronized void disconnect() {
        if (obdProtocol != null) {
            BleManage.self().client().unregisterConnectStatusListener(obdProtocol.getAddress(), bleConnectStatusListener);
            BleManage.self().client().disconnect(obdProtocol.getAddress());
            connectCallback(false);
        }
    }

    private synchronized void write(byte[] msg) {
        if (obdProtocol != null) {
            Log.d(TAG, "write: " + new String(msg));
            BleManage.self().client().write(obdProtocol.getAddress(), obdProtocol.getWriteService(), obdProtocol.getWriteCharacter(), msg, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {

                }
            });
        }
    }

    private void connectCallback(final boolean success) {
        x.task().postDelayed(new Runnable() {
            @Override
            public void run() {
                runListener(new ListenerRuner<ObdPluginListener>() {
                    @Override
                    public void run(ObdPluginListener listener) {
                        listener.connect(success);
                    }
                });
            }
        }, 50);
    }

    public boolean supportTp() {
        if (obdProtocol != null) {
            return obdProtocol.supportTp();
        }
        return false;
    }


    @Subscribe
    public void onEventAsync(final BleEventDeviceChange event) {
        String fkaddress = SharedPreUtil.getSharedPreString(CommonData.SDATA_FANGKONG_ADDRESS);
        if (CommonUtil.isNotNull(fkaddress)) {
            boolean have = false;
            for (BluetoothDevice device : event.getBluetoothDevices()) {
                if (device.getAddress().equals(fkaddress)) {
                    have = true;
                }
            }
            if (have) {
                Log.d(TAG, "扫描到绑定的方控: " + fkaddress);
                connect();
            }
        }
    }
}
