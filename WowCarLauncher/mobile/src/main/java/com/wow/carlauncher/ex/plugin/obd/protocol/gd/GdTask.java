package com.wow.carlauncher.ex.plugin.obd.protocol.gd;

import com.wow.carlauncher.ex.plugin.obd.ObdTask;

/**
 * Created by 10124 on 2018/4/25.
 */

abstract class GdTask extends ObdTask {
    String firstTreat(String msg) {
        msg = msg.replace(" ", "");
        msg = msg.replace(getReqMessage(), "");
        return msg;
    }

    @Override
    public void writeRes(String msg) {
        writeRes2(firstTreat(msg));
    }

    public abstract void writeRes2(String msg);
}
