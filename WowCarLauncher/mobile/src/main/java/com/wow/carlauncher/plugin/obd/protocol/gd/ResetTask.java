package com.wow.carlauncher.plugin.obd.protocol.gd;

import com.wow.carlauncher.plugin.obd.task.ObdTask;

/**
 * Created by 10124 on 2018/4/20.
 */

public class ResetTask extends ObdTask {
    private static final String CMD_REQ_REV = "ATZ";

    @Override
    public String getReqMessage() {
        return CMD_REQ_REV;
    }

    @Override
    public void writeRes(byte[] message) {
        markSuccess();
    }
}
