package com.wow.carlauncher.ex.plugin.obd.protocol.gd;

import com.wow.carlauncher.ex.plugin.obd.ObdTask;

/**
 * Created by 10124 on 2018/4/20.
 */

public class ResetTask extends ObdTask {
    private static final String CMD_REQ_REV = "ATS0";//E0 L0 H0 S0

    @Override
    public String getReqMessage() {
        return CMD_REQ_REV;
    }

    @Override
    public void writeRes(byte[] message) {
        markSuccess();
    }
}
