package com.wow.carlauncher.plugin.fk;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.ex.BleManageEx;
import com.wow.carlauncher.common.ex.ToastEx;
import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.fk.protocol.YiLianProtocol;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;

import org.xutils.x;

import java.util.List;
import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED;
import static com.wow.carlauncher.common.CommonData.SDATA_FANGKONG_CONTROLLER;
import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2017/11/4.
 */

public class FangkongPlugin extends BasePlugin<FangkongPluginListener> {

    private static FangkongPlugin self;

    public static FangkongPlugin self() {
        if (self == null) {
            self = new FangkongPlugin();
        }
        return self;
    }

    private BleConnectOptions options;

    private FangkongPlugin() {

    }

    public void init(Context context) {
        super.init(context);
        options = new BleConnectOptions.Builder()
                .setConnectRetry(Integer.MAX_VALUE)
                .setConnectTimeout(5000)   // 连接超时5s
                .setServiceDiscoverRetry(Integer.MAX_VALUE)
                .setServiceDiscoverTimeout(5000)  // 发现服务超时5s
                .build();

        BleManageEx.self().addListener(new BleManageEx.BleDeviceSearchListener() {
            @Override
            public void deviceListChange(final List<BluetoothDevice> bluetoothDevices) {
                x.task().post(new Runnable() {
                    @Override
                    public void run() {
                        String fkaddress = SharedPreUtil.getSharedPreString(CommonData.SDATA_FANGKONG_ADDRESS);
                        Log.d(TAG, "fkaddress: " + fkaddress);
                        Log.d(TAG, "fkaddress: " + bluetoothDevices);
                        if (CommonUtil.isNotNull(fkaddress)) {
                            boolean have = false;
                            for (BluetoothDevice device : bluetoothDevices) {
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
                });
            }
        });

        BleManageEx.self().forceCallBack();
    }

    private FangkongProtocol fangkongProtocol;

    private final BleConnectStatusListener bleConnectStatusListener = new BleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            //允许重连,同时是断开连接了,同时方控是由参数的,同时是方控的地址
            if (mac.equals(fangkongProtocol.getAddress())) {
                if (status == STATUS_CONNECTED) {
                    connectCallback(true);
                } else {
                    connectCallback(false);
                }
            }
        }
    };

    private FangkongProtocol.ChangeModelCallBack changeModelCallBack = new FangkongProtocol.ChangeModelCallBack() {
        @Override
        public void changeModel(final String name) {
            runListener(new ListenerRuner<FangkongPluginListener>() {
                @Override
                public void run(FangkongPluginListener fangkongPluginListener) {
                    fangkongPluginListener.changeModel(name);
                }
            });
        }
    };

    private boolean connecting = false;

    private synchronized void connect() {
        final String fkaddress = SharedPreUtil.getSharedPreString(CommonData.SDATA_FANGKONG_ADDRESS);
        Log.d(TAG, "connect: " + Constants.getStatusText(BleManageEx.self().client().getConnectStatus(fkaddress)) + "  " + CommonUtil.isNull(fkaddress) + "  " + Constants.getStatusText(BleManageEx.self().client().getConnectStatus(fkaddress)));
        if (connecting || CommonUtil.isNull(fkaddress) || BleManageEx.self().client().getConnectStatus(fkaddress) == STATUS_DEVICE_CONNECTED) {
            return;
        }
        connecting = true;
        FangkongProtocolEnum p1 = FangkongProtocolEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_FANGKONG_CONTROLLER, FangkongProtocolEnum.YLFK.getId()));
        switch (p1) {
            case YLFK: {
                fangkongProtocol = new YiLianProtocol(fkaddress, getContext(), changeModelCallBack);
                break;
            }
            default:
                fangkongProtocol = new YiLianProtocol(fkaddress, getContext(), changeModelCallBack);
                break;
        }
        Log.d(TAG, "开始连接");
        BleManageEx.self().client().clearRequest(fangkongProtocol.getAddress(), 0);
        BleManageEx.self().client().refreshCache(fangkongProtocol.getAddress());
        BleManageEx.self().client().registerConnectStatusListener(fangkongProtocol.getAddress(), bleConnectStatusListener);
        BleManageEx.self().client().connect(fangkongProtocol.getAddress(), options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                if (code == REQUEST_SUCCESS) {
                    BleManageEx.self().client().notify(fangkongProtocol.getAddress(),
                            fangkongProtocol.getService(),
                            fangkongProtocol.getCharacter(),
                            new BleNotifyResponse() {
                                @Override
                                public void onNotify(UUID service, UUID character, byte[] msg) {
                                    if (fangkongProtocol != null) {
                                        fangkongProtocol.receiveMessage(msg);
                                    }
                                }

                                @Override
                                public void onResponse(int code) {
                                    connecting = false;
                                    if (code == REQUEST_SUCCESS) {
                                        ToastEx.self().show("方控连接成功");
                                        Log.d(TAG, "onResponse: 开始测试!!!");
                                        Log.d(TAG, Constants.getStatusText(BleManageEx.self().client().getConnectStatus(fkaddress)) + "  " + CommonUtil.isNull(fkaddress) + "  " + Constants.getStatusText(BleManageEx.self().client().getConnectStatus(fkaddress)));
                                    } else {
                                        BleManageEx.self().client().disconnect(fangkongProtocol.getAddress());
                                    }
                                }
                            });
                } else {
                    connecting = false;
                    BleManageEx.self().forceCallBack();
                    Log.d(TAG, "onResponse: 方控连接失败!!!");
                }
            }
        });
    }

    public synchronized void disconnect() {
        if (fangkongProtocol != null) {
            BleManageEx.self().client().unregisterConnectStatusListener(fangkongProtocol.getAddress(), bleConnectStatusListener);
            BleManageEx.self().client().disconnect(fangkongProtocol.getAddress());
            connectCallback(false);
        }
    }

    private void connectCallback(final boolean success) {
        x.task().postDelayed(new Runnable() {
            @Override
            public void run() {
                runListener(new ListenerRuner<FangkongPluginListener>() {
                    @Override
                    public void run(FangkongPluginListener fangkongPluginListener) {
                        fangkongPluginListener.connect(success);
                    }
                });
            }
        }, 50);
    }

    public String getModelName() {
        return fangkongProtocol.getModelName();
    }
}
