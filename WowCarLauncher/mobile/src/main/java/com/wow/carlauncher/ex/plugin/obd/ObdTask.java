package com.wow.carlauncher.ex.plugin.obd;

/**
 * Created by 10124 on 2018/4/19.
 */

public abstract class ObdTask {
    public final static int STATE_WAIT_SEND = 1;
    public final static int STATE_WAIT_RECEIVE = 2;
    public final static int STATE_TIME_OUT = 3;

    private long createTime = System.currentTimeMillis();//创建时间,如果创建两秒后没有发送,则销毁
    private long sendTime;

    public long getSendTime() {
        return sendTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    private int state = STATE_WAIT_SEND;

    public int getState() {
        return state;
    }

    public ObdTask setState(int state) {
        this.state = state;
        if(state==STATE_WAIT_RECEIVE){

        }
        return this;
    }

    private boolean haveData = false;

    public boolean haveData() {
        return haveData;
    }

    protected void markNoData() {
        haveData = false;
    }

    private boolean success = false;

    protected void markSuccess() {
        haveData = true;
        success = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public byte[] getReqWarp() {
        return (getReqMessage() + '\r').getBytes();
    }

    public abstract String getReqMessage();

    public abstract void writeRes(String msg);

}
