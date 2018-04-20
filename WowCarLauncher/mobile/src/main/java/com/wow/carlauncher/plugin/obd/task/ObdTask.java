package com.wow.carlauncher.plugin.obd.task;

/**
 * Created by 10124 on 2018/4/19.
 */

public abstract class ObdTask {
    private boolean success = false;

    protected void markSuccess() {
        success = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public byte[] getReqWarp() {
        return (getReqMessage() + '\r').getBytes();
    }

    public abstract String getReqMessage();

    public abstract void writeRes(byte[] msg);

}
