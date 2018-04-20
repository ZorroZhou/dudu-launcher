package com.wow.carlauncher.plugin.obd.protocol.gd;

import com.google.common.primitives.Shorts;
import com.wow.carlauncher.plugin.obd.ObdUtil;
import com.wow.carlauncher.plugin.obd.task.ObdTask;

import static com.wow.carlauncher.plugin.obd.protocol.gd.CommonCmd.CMD_RES_END;
import static com.wow.carlauncher.plugin.obd.protocol.gd.CommonCmd.CMD_RES_NO_DATA;

/**
 * Created by 10124 on 2018/4/20.
 */

public class GetOilConTask extends ObdTask {
    private static final String CMD_REQ = "015E";//2F
    private static final String CMD_RES = "415E";

    @Override
    public String getReqMessage() {
        return CMD_REQ;
    }

    @Override
    public void writeRes(byte[] message) {
        String msg = new String(message);
        if (msg.endsWith(CMD_RES_END) && msg.startsWith(CMD_RES)) {
            markSuccess();
        } else if (msg.endsWith(CMD_RES_END) && msg.startsWith(CMD_RES_NO_DATA)) {
            markSuccess();
            markNoData();
        }
    }
}
