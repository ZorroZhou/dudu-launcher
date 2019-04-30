package com.wow.carlauncher.ex.manage.ble;

import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;

import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;

class MyBleConnectStatusListener extends BleConnectStatusListener {
    private String mark;
    private BleManage bleManage;

    public MyBleConnectStatusListener(String mark, BleManage bleManage) {
        this.mark = mark;
        this.bleManage = bleManage;
    }

    @Override
    public void onConnectStatusChanged(String mac, int status) {
        BleListener listener = bleManage.getListeners().get(mark);
        if (listener != null) {
            if (status == STATUS_CONNECTED) {
                listener.connect(true);
            } else {
                listener.connect(false);
            }
        }
    }
}
