package com.wow.carlauncher.ex.plugin.obd.protocol.gd;

import com.wow.carlauncher.ex.plugin.obd.ObdTask;

import static com.wow.carlauncher.ex.plugin.obd.protocol.gd.CommonCmd.*;

/**
 * Created by 10124 on 2018/4/20.
 */

public class GetSpeedTask extends GdTask {
    private static final String CMD_REQ = "010D";
    private static final String CMD_RES = "410D";
    private int speed;

    public int getSpeed() {
        return speed;
    }

    @Override
    public String getReqMessage() {
        return CMD_REQ;
    }

    @Override
    public void writeRes2(String msg) {
        if (msg.startsWith(CMD_RES)) {
            if (msg.length() >= 6) {
                speed = Integer.parseInt(msg.substring(4, 6), 16);
                markSuccess();
            }
        } else if (msg.startsWith(CMD_RES_NO_DATA)) {
            speed = 0;
            markSuccess();
            markNoData();
        }
    }
}
