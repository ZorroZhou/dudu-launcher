package com.wow.carlauncher.plugin.fk.event;

/**
 * Created by 10124 on 2018/4/22.
 */
public class PFkEventConnect {
    private Boolean connected;

    public Boolean getConnected() {
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
