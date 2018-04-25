package com.wow.carlauncher.ex.plugin.obd.protocol.gd;

import com.wow.carlauncher.ex.plugin.obd.ObdTask;

import static com.wow.carlauncher.ex.plugin.obd.protocol.gd.CommonCmd.CMD_RES_END;

/**
 * Created by 10124 on 2018/4/20.
 */

public class GetTpTask extends ObdTask {
    public static final int LF = 0;
    public static final int RF = 1;
    public static final int LB = 2;
    public static final int RB = 3;

    private static final String CMD_REQ_TP = "ATT";

    private int mark;
    private Float tp;
    private Integer temp;

    public int getMark() {
        return mark;
    }

    public Float getTp() {
        return tp;
    }

    public Integer getTemp() {
        return temp;
    }

    public GetTpTask(int mark) {
        this.mark = mark;
    }

    @Override
    public String getReqMessage() {
        return CMD_REQ_TP + mark;
    }

    @Override
    public void writeRes(byte[] message) {
        String msg = new String(message);
        if (msg.startsWith(mark + "") && msg.endsWith(CMD_RES_END)) {
            byte[] msgByte = msg.getBytes();
            float f1 = ((float) Integer.parseInt(new String(new byte[]{msgByte[3]}), 16)) / 10.0F + Integer.parseInt(new String(new byte[]{msgByte[4], msgByte[5]}), 16);
            tp = (float) (f1 / 14.5);
            int k = Integer.parseInt(new String(new byte[]{msgByte[6], msgByte[7]}), 16);
            temp = k - 40;
            markSuccess();
        } else {
            tp = -10000F;
            temp = -10000;
        }
    }
}
