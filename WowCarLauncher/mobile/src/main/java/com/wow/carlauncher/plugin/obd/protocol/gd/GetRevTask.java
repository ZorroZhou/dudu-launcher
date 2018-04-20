package com.wow.carlauncher.plugin.obd.protocol.gd;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Shorts;
import com.wow.carlauncher.plugin.obd.ObdUtil;
import com.wow.carlauncher.plugin.obd.task.ObdTask;

import static com.wow.carlauncher.plugin.obd.protocol.gd.CommonCmd.CMD_RES_END;
import static com.wow.carlauncher.plugin.obd.protocol.gd.CommonCmd.CMD_RES_NO_DATA;

/**
 * Created by 10124 on 2018/4/20.
 */

public class GetRevTask extends ObdTask {
    private static final String CMD_REQ_REV = "010C";
    private static final String CMD_RES_REV = "410C";
    private int rev;

    public int getRev() {
        return rev;
    }

    @Override
    public String getReqMessage() {
        return CMD_REQ_REV;
    }

    @Override
    public void writeRes(byte[] message) {
        String msg = new String(message);
        if (msg.endsWith(CMD_RES_END) && msg.startsWith(CMD_RES_REV)) {
            msg = msg.replace(CMD_RES_END, "").replace(CMD_RES_REV, "");
            if (msg.length() == 4) {
                rev = Shorts.fromBytes(ObdUtil.hexToByte(msg.substring(0, 2)), ObdUtil.hexToByte(msg.substring(2, 4)));
                markSuccess();
            }

        } else if (msg.endsWith(CMD_RES_END) && msg.startsWith(CMD_RES_NO_DATA)) {
            rev = 0;
            markSuccess();
        }
    }
}
