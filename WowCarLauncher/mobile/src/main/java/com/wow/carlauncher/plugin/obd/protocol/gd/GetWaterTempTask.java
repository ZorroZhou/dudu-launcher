package com.wow.carlauncher.plugin.obd.protocol.gd;

import com.wow.carlauncher.plugin.obd.ObdUtil;
import com.wow.carlauncher.plugin.obd.ObdTask;

import static com.wow.carlauncher.plugin.obd.protocol.gd.CommonCmd.CMD_RES_END;
import static com.wow.carlauncher.plugin.obd.protocol.gd.CommonCmd.CMD_RES_NO_DATA;

/**
 * Created by 10124 on 2018/4/20.
 */

public class GetWaterTempTask extends ObdTask {
    private static final String CMD_REQ = "0105";
    private static final String CMD_RES = "4105";
    private int temp;

    public int getTemp() {
        return temp;
    }

    @Override
    public String getReqMessage() {
        return CMD_REQ;
    }

    @Override
    public void writeRes(byte[] message) {
        String msg = new String(message);
        if (msg.endsWith(CMD_RES_END) && msg.startsWith(CMD_RES)) {
            if (msg.length() >= 6) {
                temp = Integer.parseInt(msg.substring(4, 6),16) - 40;
                markSuccess();
            }
        } else if (msg.endsWith(CMD_RES_END) && msg.startsWith(CMD_RES_NO_DATA)) {
            temp = -10000;
            markSuccess();
            markNoData();
        }
    }
}
