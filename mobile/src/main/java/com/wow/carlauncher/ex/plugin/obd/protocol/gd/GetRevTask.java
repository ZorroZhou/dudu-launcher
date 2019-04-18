package com.wow.carlauncher.ex.plugin.obd.protocol.gd;

import com.wow.carlauncher.base.Shorts;
import com.wow.carlauncher.ex.plugin.obd.ObdUtil;

import static com.wow.carlauncher.ex.plugin.obd.protocol.gd.CommonCmd.CMD_RES_NO_DATA;

/**
 * Created by 10124 on 2018/4/20.
 */

public class GetRevTask extends GdTask {
    private static final String CMD_REQ = "010C";
    private static final String CMD_RES = "410C";
    private int rev;

    public int getRev() {
        return rev;
    }

    @Override
    public String getReqMessage() {
        return CMD_REQ;
    }

    @Override
    public void writeRes2(String msg) {
        //410C0C30>
        if (msg.startsWith(CMD_RES)) {
            msg = msg.substring(4, 8);
            if (msg.length() >= 4) {
                rev = Shorts.fromBytes(ObdUtil.hexToByte(msg.substring(0, 2)), ObdUtil.hexToByte(msg.substring(2, 4))) / 4;
                markSuccess();
            }

        } else if (msg.startsWith(CMD_RES_NO_DATA)) {
            rev = 0;
            markSuccess();
            markNoData();
        }
    }
}
