package com.wow.carlauncher.ex.plugin.amapcar;

/**
 * Created by 10124 on 2017/11/6.
 */

public class NaviInfo {
    public static final int TYPE_NAVI = 1;
    public static final int TYPE_STATE = 2;
    private int type;
    private int state;
    private int icon;
    private int dis;
    private String wroad;
    private int remainDis;
    private int remainTime;

    public int getDis() {
        return dis;
    }

    public void setDis(int dis) {
        this.dis = dis;
    }

    public NaviInfo(int type) {
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getWroad() {
        return wroad;
    }

    public void setWroad(String wroad) {
        this.wroad = wroad;
    }

    public int getRemainDis() {
        return remainDis;
    }

    public void setRemainDis(int remainDis) {
        this.remainDis = remainDis;
    }

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

    @Override
    public String toString() {
        return "NaviInfo{" +
                "type=" + type +
                ", state=" + state +
                ", icon=" + icon +
                ", dis=" + dis +
                ", wroad='" + wroad + '\'' +
                ", remainDis=" + remainDis +
                ", remainTime=" + remainTime +
                '}';
    }
}
