package com.wow.carlauncher.ex.plugin.obd;

/**
 * Created by 10124 on 2018/4/19.
 */

public abstract class ObdTask {
    private boolean haveData = false;

    public boolean haveData() {
        return haveData;
    }

    protected void markNoData() {
        haveData = false;
    }

    private boolean success = false;

    protected void markSuccess() {
        haveData = true;
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
