package com.wow.carlauncher.ex.manage.ble;

import java.util.UUID;

public interface BleListener {

    String getMark();

    void connect(boolean success);

    default void receiveMessage(UUID service, UUID character, byte[] msg) {
    }

    void receiveMessage(byte[] msg);
}
