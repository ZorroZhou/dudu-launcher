package com.wow.carlauncher.ex.plugin.obd;

import android.util.Log;

import com.wow.frame.util.DateUtil;

import java.util.Date;

/**
 * Created by 10124 on 2018/4/19.
 */

public abstract class ObdTask {
    private long sendTime;

    public ObdTask setSendTime(long sendTime) {
        this.sendTime = sendTime;
        return this;
    }

    public long getSendTime() {
        return sendTime;
    }

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

    public abstract void writeRes(String msg);

}
