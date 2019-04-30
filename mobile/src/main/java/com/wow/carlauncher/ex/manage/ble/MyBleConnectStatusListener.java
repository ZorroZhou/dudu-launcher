package com.wow.carlauncher.ex.manage.ble;

import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;

import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;

class MyBleConnectStatusListener extends BleConnectStatusListener {
    private String mark;

    public MyBleConnectStatusListener(String mark) {
        this.mark = mark;
    }

    @Override
    public void onConnectStatusChanged(String mac, int status) {
        BleListener listener = BleManage.self().getListeners().get(mark);
        if (listener != null) {
            if (status == STATUS_CONNECTED) {
                listener.connect(true);
            } else {
                listener.connect(false);
            }
        }
    }
}
