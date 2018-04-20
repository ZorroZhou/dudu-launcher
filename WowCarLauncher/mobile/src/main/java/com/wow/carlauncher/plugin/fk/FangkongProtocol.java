package com.wow.carlauncher.plugin.fk;

import android.content.Context;

import java.util.UUID;

/**
 * Created by 10124 on 2018/3/29.
 */

public abstract class FangkongProtocol {
    private String address;
    protected Context context;
    protected ChangeModelCallBack changeModelCallBack;

    public String getAddress() {
        return address;
    }

    public FangkongProtocol(String address, Context context, ChangeModelCallBack changeModelCallBack) {
        this.address = address;
        this.changeModelCallBack = changeModelCallBack;
        this.context = context;
    }

    public abstract void receiveMessage(byte[] message);

    public abstract UUID getService();

    public abstract UUID getCharacter();

    public interface ChangeModelCallBack {
        void changeModel(String name);
    }
}
