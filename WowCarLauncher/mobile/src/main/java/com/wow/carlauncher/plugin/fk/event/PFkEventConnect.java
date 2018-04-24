package com.wow.carlauncher.plugin.fk.event;

/**
 * Created by 10124 on 2018/4/22.
 */
public class PFkEventConnect {
    private boolean connected;

    public boolean isConnected() {
        return connected;
    }

    public PFkEventConnect setConnected(Boolean connected) {
        this.connected = connected;
        return this;
    }

    public PFkEventConnect(Boolean connected) {
        this.connected = connected;
    }

    public PFkEventConnect() {
    }
}
