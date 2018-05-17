package com.wow.carlauncher.ex.plugin.obd;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothManager;
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
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.manage.time.TimeManage;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventConnect;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarInfo;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.ex.plugin.obd.protocol.GoodDriverTPProtocol;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED;
import static com.wow.carlauncher.common.CommonData.SDATA_OBD_CONTROLLER;

/**
 * Created by 10124 on 2017/11/4.
 */

public class ObdPlugin extends ContextEx {
    public static final String TAG = "WOW_CAR_OBD";

    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static ObdPlugin instance = new ObdPlugin();
    }

    public static ObdPlugin self() {
        return ObdPlugin.SingletonHolder.instance;
    }

    private ObdPlugin() {

    }

    private boolean connect = false;

    public boolean notConnect() {
        return !connect;
    }

    private BleConnectOptions options;

    private ObdProtocol obdProtocol;

    private PObdEventCarInfo currentPObdEventCarInfo;

    public PObdEventCarInfo getCurrentPObdEventCarInfo() {
        return currentPObdEventCarInfo;
    }

    private PObdEventCarTp currentPObdEventCarTp;

    public PObdEventCarTp getCurrentPObdEventCarTp() {
        return currentPObdEventCarTp;
    }

    private ObdProtocolListener obdProtocolListener = new ObdProtocolListener() {
        @Override
        public void write(byte[] req) {
            ObdPlugin.this.write(req);
        }

        @Override
        public boolean isConnect() {
            return obdProtocol != null && BleManage.self().client().getConnectStatus(obdProtocol.getAddress()) == STATUS_DEVICE_CONNECTED;
        }

        @Override
        public void carRunningInfo(final Integer speed, final Integer rev, final Integer waterTemp, final Integer oilConsumption) {
            postEvent(new PObdEventCarInfo().setSpeed(speed).setRev(rev).setWaterTemp(waterTemp).setOilConsumption(oilConsumption));
            if (speed != null) {
                currentPObdEventCarInfo.setSpeed(speed);
            }
            if (rev != null) {
                currentPObdEventCarInfo.setRev(rev);
            }
            if (waterTemp != null) {
                currentPObdEventCarInfo.setWaterTemp(waterTemp);
            }
            if (oilConsumption != null) {
                currentPObdEventCarInfo.setOilConsumption(oilConsumption);
            }
        }

        @Override
        public void carTirePressureInfo(final Float lFTirePressure, final Integer lFTemp,
                                        final Float rFTirePressure, final Integer rFTemp,
                                        final Float lBTirePressure, final Integer lBTemp,
                                        final Float rBTirePressure, final Integer rBTemp) {
            postEvent(new PObdEventCarTp()
                    .setlBTirePressure(lBTirePressure).setlBTemp(lBTemp)
                    .setlFTirePressure(lFTirePressure).setlFTemp(lFTemp)
                    .setrBTirePressure(rBTirePressure).setrBTemp(rBTemp)
                    .setrFTirePressure(rFTirePressure).setrFTemp(rFTemp));

            if (lFTirePressure != null) {
                currentPObdEventCarTp.setlFTirePressure(lFTirePressure).setlFTemp(lFTemp);
            }
            if (lBTirePressure != null) {
                currentPObdEventCarTp.setlBTirePressure(lBTirePressure).setlBTemp(lBTemp);
            }
            if (rFTirePressure != null) {
                currentPObdEventCarTp.setrFTirePressure(rFTirePressure).setrFTemp(rFTemp);
            }
            if (rBTirePressure != null) {
                currentPObdEventCarTp.setrBTirePressure(rBTirePressure).setrBTemp(rBTemp);
            }
        }
    };

    private final BleConnectStatusListener bleConnectStatusListener = new BleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (mac.equals(obdProtocol.getAddress())) {
                if (status != STATUS_CONNECTED) {
                    obdProtocol.stop();
                }
            }
        }
    };

    public void init(Context context) {
        setContext(context);

        currentPObdEventCarInfo = new PObdEventCarInfo();
        currentPObdEventCarTp = new PObdEventCarTp();

        options = new BleConnectOptions.Builder()
                .setConnectRetry(Integer.MAX_VALUE)
                .setConnectTimeout(5000)   // 连接超时5s
                .setServiceDiscoverRetry(Integer.MAX_VALUE)
                .setServiceDiscoverTimeout(5000)  // 发现服务超时5s
                .build();

        connect();

        EventBus.getDefault().register(this);
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
                    Log.d(TAG, "onResponse: 方控连接失败!!!");
                }
            }
        });
    }

    public synchronized void disconnect() {
        if (obdProtocol != null) {
            BleManage.self().client().unregisterConnectStatusListener(obdProtocol.getAddress(), bleConnectStatusListener);
            BleManage.self().client().disconnect(obdProtocol.getAddress());
        }
    }

    private synchronized void write(byte[] msg) {
        if (obdProtocol != null) {
            BleManage.self().client().write(obdProtocol.getAddress(), obdProtocol.getWriteService(), obdProtocol.getWriteCharacter(), msg, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {

                }
            });
        }
    }

    public boolean supportTp() {
        return obdProtocol != null && obdProtocol.supportTp();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final MTimeSecondEvent event) {
        String fkaddress = SharedPreUtil.getSharedPreString(CommonData.SDATA_OBD_ADDRESS);

        if (CommonUtil.isNotNull(fkaddress) && BleManage.self().client().getConnectStatus(fkaddress) == STATUS_DEVICE_CONNECTED) {
            connect = true;
            postEvent(new PObdEventConnect().setConnected(true));
        } else {
            connect = false;
            postEvent(new PObdEventConnect().setConnected(false));
        }

        if (CommonUtil.isNotNull(fkaddress) && BleManage.self().client().getConnectStatus(fkaddress) != STATUS_DEVICE_CONNECTED) {
            connect();
        }
    }
}
