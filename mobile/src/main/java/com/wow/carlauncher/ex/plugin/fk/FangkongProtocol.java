package com.wow.carlauncher.ex.plugin.fk;

import android.content.Context;

import java.util.UUID;

/**
 * Created by 10124 on 2018/3/29.
 */

public abstract class FangkongProtocol {
    private String address;
    protected Context context;
    protected FangkongProtocolListener listener;
    protected boolean simulatedDClick = false;

    public String getAddress() {
        return address;
    }

    public FangkongProtocol(String address, Context context, FangkongProtocolListener listener) {
        this.address = address;
        this.listener = listener;
        this.context = context;
    }

    public FangkongProtocol setSimulatedDClick(boolean simulatedDClick) {
        this.simulatedDClick = simulatedDClick;
        return this;
    }

    public abstract void receiveMessage(byte[] message);

    public abstract UUID getService();

    public abstract UUID getCharacter();

    public abstract void destroy();
}
