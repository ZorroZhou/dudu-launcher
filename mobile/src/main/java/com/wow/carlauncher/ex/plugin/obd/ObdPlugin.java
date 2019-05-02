package com.wow.carlauncher.ex.plugin.obd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.ble.BleListener;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.manage.ble.MyBleConnectStatusListener;
import com.wow.carlauncher.ex.manage.time.event.MTime3SecondEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarInfo;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.ex.plugin.obd.protocol.GoodDriverProtocol;
import com.wow.carlauncher.ex.plugin.obd.protocol.GoodDriverTPProtocol;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTING;
import static com.wow.carlauncher.common.CommonData.SDATA_OBD_CONTROLLER;

/**
 * Created by 10124 on 2017/11/4.
 */

public class ObdPlugin extends ContextEx {
    private static final String BLE_MARK = "BLE_OBD";
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

    public boolean isConnect() {
        String fkaddress = SharedPreUtil.getString(CommonData.SDATA_OBD_ADDRESS);
        return CommonUtil.isNotNull(fkaddress) && BleManage.self().getConnectStatus(fkaddress) == STATUS_DEVICE_CONNECTED;
    }

    private ObdProtocol obdProtocol;

    private MyBleConnectStatusListener myBleConnectStatusListener;

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
            return obdProtocol != null;
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

    private final BleListener bleListener = new BleListener() {
        @Override
        public String getMark() {
            return BLE_MARK;
        }

        @Override
        public void connect(boolean success) {
            postEvent(new PObdEventConnect().setConnected(success));
            if (obdProtocol != null) {
                if (success) {
                    ToastManage.self().show("OBD连接成功!");
                    obdProtocol.run();
                } else {
                    obdProtocol.stop();
                    disconnect();
                }
            }
            connecting = false;
        }

        @Override
        public void receiveMessage(byte[] msg) {
            if (obdProtocol != null) {
                obdProtocol.receiveMessage(msg);
            }
        }
    };

    public void init(Context context) {
        setContext(context);
        currentPObdEventCarInfo = new PObdEventCarInfo();
        currentPObdEventCarTp = new PObdEventCarTp();
        BleManage.self().addListener(bleListener);
        myBleConnectStatusListener = new MyBleConnectStatusListener(BLE_MARK);
        EventBus.getDefault().register(this);
        Log.e(TAG + getClass().getSimpleName(), "init ");
    }

    private boolean connecting = false;

    private synchronized void connect() {
        final String address = SharedPreUtil.getString(CommonData.SDATA_OBD_ADDRESS);
        if (connecting ||
                CommonUtil.isNull(address) ||
                BleManage.self().getConnectStatus(address) == STATUS_DEVICE_CONNECTED ||
                BleManage.self().getConnectStatus(address) == STATUS_DEVICE_CONNECTING) {
            return;
        }

        connecting = true;

        disconnect();

        ObdProtocolEnum p1 = ObdProtocolEnum.getById(SharedPreUtil.getInteger(SDATA_OBD_CONTROLLER, ObdProtocolEnum.YJ_TYB.getId()));
        switch (p1) {
            case YJ_TYB: {
                obdProtocol = new GoodDriverTPProtocol(getContext(), address, obdProtocolListener);
                break;
            }
            case YJ_PTB: {
                obdProtocol = new GoodDriverProtocol(getContext(), address, obdProtocolListener);
                break;
            }
            default:
                obdProtocol = new GoodDriverTPProtocol(getContext(), address, obdProtocolListener);
                break;
        }
        BleManage.self().connect(BLE_MARK, obdProtocol.getAddress(), obdProtocol.getNotifyService(), obdProtocol.getNotifyCharacter(), myBleConnectStatusListener);
    }

    public synchronized void disconnect() {
        BleManage.self().disconnect(BLE_MARK);
        if (obdProtocol != null) {
            obdProtocol.destroy();
            obdProtocol = null;
        }
    }

    private synchronized void write(byte[] msg) {
        if (obdProtocol != null) {
            BleManage.self().getClient().write(obdProtocol.getAddress(), obdProtocol.getWriteService(), obdProtocol.getWriteCharacter(), msg, code -> {
            });
        }
    }

    public boolean supportTp() {
        return obdProtocol != null && obdProtocol.supportTp();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final MTime3SecondEvent event) {
        String fkaddress = SharedPreUtil.getString(CommonData.SDATA_OBD_ADDRESS);
        if (CommonUtil.isNotNull(fkaddress)) {
            connect();
        }
    }
}
