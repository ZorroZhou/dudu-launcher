package com.wow.carlauncher.ex.plugin.fk;

import android.content.Context;
import android.util.Log;

import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchResult;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.ble.BleListener;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.manage.ble.event.BMEventFindDevice;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventAction;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventBatterLevel;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventConnect;
import com.wow.carlauncher.ex.plugin.fk.protocol.YiLianProtocol;
import com.wow.carlauncher.ex.plugin.obd.ObdProtocolEnum;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.ex.plugin.obd.protocol.GoodDriverProtocol;
import com.wow.carlauncher.ex.plugin.obd.protocol.GoodDriverTPProtocol;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.x;

import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED;
import static com.wow.carlauncher.common.CommonData.SDATA_FANGKONG_CONTROLLER;
import static com.wow.carlauncher.common.CommonData.SDATA_OBD_CONTROLLER;
import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2017/11/4.
 */

public class FangkongPlugin extends ContextEx {

    private static FangkongPlugin self;

    public static FangkongPlugin self() {
        if (self == null) {
            self = new FangkongPlugin();
        }
        return self;
    }

    public static final String BLE_MARK = "BLE_FANGKONG";

    private FangkongPlugin() {

    }

    public void init(Context context) {
        setContext(context);
        BleManage.self().addListener(bleListener);
        EventBus.getDefault().register(this);
        Log.e(TAG + getClass().getSimpleName(), "init ");
    }

    private final BleListener bleListener = new BleListener() {
        @Override
        public String getMark() {
            return BLE_MARK;
        }

        @Override
        public void connect(boolean success) {
            postEvent(new PObdEventConnect().setConnected(success));
            if (fangkongProtocol != null) {
                if (success) {
                    ToastManage.self().show("方控连接成功!");
                }
            }
            connect = success;
            connecting = false;
        }

        @Override
        public void receiveMessage(byte[] msg) {
            if (fangkongProtocol != null) {
                fangkongProtocol.receiveMessage(msg);
            }
        }
    };
    private boolean connect = false;

    public boolean isConnect() {
        return connect;
    }

    private FangkongProtocol fangkongProtocol;

    private FangkongProtocolListener changeModelCallBack = new FangkongProtocolListener() {
        @Override
        public void batteryLevel(Integer level, Integer total) {
            EventBus.getDefault().post(new PFkEventBatterLevel().setLevel(level).setTotal(total));
        }

        @Override
        public void onAction(final int action) {
            x.task().run(() -> EventBus.getDefault().post(new PFkEventAction()
                    .setAction(action)
                    .setFangkongProtocol(FangkongProtocolEnum.getById(SharedPreUtil.getInteger(SDATA_FANGKONG_CONTROLLER, FangkongProtocolEnum.YLFK.getId())))
            ));
        }
    };

    private boolean connecting = false;

    private synchronized void connect() {
        final String fkaddress = SharedPreUtil.getString(CommonData.SDATA_FANGKONG_ADDRESS);
        if (connecting || CommonUtil.isNull(fkaddress) || BleManage.self().getConnectStatus(fkaddress) == STATUS_DEVICE_CONNECTED) {
            return;
        }

        connecting = true;

        disconnect();

        FangkongProtocolEnum p1 = FangkongProtocolEnum.getById(SharedPreUtil.getInteger(SDATA_FANGKONG_CONTROLLER, FangkongProtocolEnum.YLFK.getId()));
        switch (p1) {
            case YLFK: {
                fangkongProtocol = new YiLianProtocol(fkaddress, getContext(), changeModelCallBack);
                break;
            }
            default:
                fangkongProtocol = new YiLianProtocol(fkaddress, getContext(), changeModelCallBack);
                break;
        }

        BleManage.self().connect(BLE_MARK, fangkongProtocol.getAddress(), fangkongProtocol.getService(), fangkongProtocol.getCharacter());
    }

    public synchronized void disconnect() {
        BleManage.self().disconnect(BLE_MARK);
        if (fangkongProtocol != null) {
            fangkongProtocol.destroy();
            fangkongProtocol = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final BMEventFindDevice event) {
        String fkaddress = SharedPreUtil.getString(CommonData.SDATA_FANGKONG_ADDRESS);
        if (CommonUtil.isNotNull(fkaddress)) {
            boolean find = false;
            for (SearchResult device : event.getDeviceList()) {
                if (device.getAddress().equals(fkaddress)) {
                    find = true;
                    break;
                }
            }
            if (find) {
                connect();
            }
        }
    }
}
