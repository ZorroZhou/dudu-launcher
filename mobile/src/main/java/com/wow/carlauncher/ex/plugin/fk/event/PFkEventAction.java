package com.wow.carlauncher.ex.plugin.fk.event;

import com.wow.carlauncher.ex.plugin.fk.FangkongProtocolEnum;

/**
 * 这个事件必须用ThreadMode.PostThread 进程进行处理,并标记事件优先级
 */
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
