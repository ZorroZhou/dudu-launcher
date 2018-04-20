package com.wow.carlauncher.plugin.obd.protocol.gd;

import android.util.Log;

import com.google.common.primitives.Shorts;
import com.wow.carlauncher.plugin.obd.ObdUtil;
import com.wow.carlauncher.plugin.obd.task.ObdTask;

import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.plugin.obd.protocol.gd.CommonCmd.*;

/**
 * Created by 10124 on 2018/4/20.
 */

public class GetSpeedTask extends ObdTask {
    private static final String CMD_REQ_SPEED = "010D";
    private static final String CMD_RES_SPEED = "410D";
    private int speed;

    public int getSpeed() {
        return speed;
    }

    @Override
    public String getReqMessage() {
        return CMD_REQ_SPEED;
    }

    @Override
    public void writeRes(byte[] message) {
        String msg = new String(message);
        if (msg.endsWith(CMD_RES_END) && msg.startsWith(CMD_RES_SPEED)) {
            msg = msg.replace(CMD_RES_END, "").replace(CMD_RES_SPEED, "").replace(" ", "");
            if (msg.length() > 2) {
                speed = ObdUtil.hexToByte(msg.substring(0, 2));
                markSuccess();
            }
        } else if (msg.endsWith(CMD_RES_END) && msg.startsWith(CMD_RES_NO_DATA)) {
            speed = 0;
            markSuccess();
            markNoData();
        }
    }
}
