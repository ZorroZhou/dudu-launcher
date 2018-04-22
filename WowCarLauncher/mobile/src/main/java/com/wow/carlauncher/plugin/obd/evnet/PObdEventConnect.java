package com.wow.carlauncher.plugin.obd.evnet;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PObdEventConnect {
    private boolean connected;

    public boolean isConnected() {
        return connected;
    }

    public PObdEventConnect setConnected(boolean connected) {
        this.connected = connected;
        return this;
    }
}

