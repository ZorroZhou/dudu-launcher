package com.wow.carlauncher.ex.plugin.fk.event;

/**
 * 这个事件必须用ThreadMode.PostThread 进程进行处理,并标记事件优先级
 */
public class PFkEventAction extends PFkBaseEventAction {
    private int action;

    public int getAction() {
        return action;
    }

    public PFkEventAction setAction(int action) {
        this.action = action;
        return this;
    }
}
