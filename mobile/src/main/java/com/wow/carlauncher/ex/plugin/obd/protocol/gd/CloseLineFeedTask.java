package com.wow.carlauncher.ex.plugin.obd.protocol.gd;

import com.wow.carlauncher.ex.plugin.obd.ObdTask;

import static com.wow.carlauncher.ex.plugin.obd.protocol.gd.CommonCmd.CMD_RES_SUCCESS;

/**
 * Created by 10124 on 2018/4/20.
 */

public class CloseLineFeedTask extends ObdTask {
    private static final String CMD_REQ_REV = "ATL0";//E0 L0 H0 S0

    @Override
    public String getReqMessage() {
        return CMD_REQ_REV;
    }

    @Override
    public void writeRes(String message) {
        if (message.contains(CMD_RES_SUCCESS)) {
            markSuccess();
        }
    }
}
