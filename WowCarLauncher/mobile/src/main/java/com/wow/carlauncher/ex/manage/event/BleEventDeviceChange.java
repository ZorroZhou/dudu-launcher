package com.wow.carlauncher.ex.manage.event;

import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * Created by 10124 on 2018/4/22.
 */

public class BleEventDeviceChange {
    private List<BluetoothDevice> bluetoothDevices;

    public List<BluetoothDevice> getBluetoothDevices() {
        return bluetoothDevices;
    }

    public BleEventDeviceChange setBluetoothDevices(List<BluetoothDevice> bluetoothDevices) {
        this.bluetoothDevices = bluetoothDevices;
        return this;
    }
}
