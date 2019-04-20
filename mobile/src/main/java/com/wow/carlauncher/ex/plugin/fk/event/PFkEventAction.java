package com.wow.carlauncher.ex.plugin.fk.event;

import com.wow.carlauncher.ex.plugin.fk.FangkongProtocolEnum;

public class PFkEventAction {
    private FangkongProtocolEnum fangkongProtocol;
    private int action;

    public FangkongProtocolEnum getFangkongProtocol() {
        return fangkongProtocol;
    }

    public PFkEventAction setFangkongProtocol(FangkongProtocolEnum fangkongProtocol) {
        this.fangkongProtocol = fangkongProtocol;
        return this;
    }

    public int getAction() {
        return action;
    }

    public PFkEventAction setAction(int action) {
        this.action = action;
        return this;
    }
}
