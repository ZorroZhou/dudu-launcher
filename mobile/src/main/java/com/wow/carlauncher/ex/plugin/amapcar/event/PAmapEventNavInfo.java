package com.wow.carlauncher.ex.plugin.amapcar.event;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PAmapEventNavInfo {
    //事件类型
    private int type;
    //图标
    private int icon;
    //距离,米
    private int dis;
    //路名
    private String wroad;
    //剩余距离
    private int remainDis;
    //剩余时间
    private int remainTime;

    public int getType() {
        return type;
    }

    public PAmapEventNavInfo setType(int type) {
        this.type = type;
        return this;
    }

    public int getIcon() {
        return icon;
    }

    public PAmapEventNavInfo setIcon(int icon) {
        this.icon = icon;
        return this;
    }

    public int getDis() {
        return dis;
    }

    public PAmapEventNavInfo setDis(int dis) {
        this.dis = dis;
        return this;
    }

    public String getWroad() {
        return wroad;
    }

    public PAmapEventNavInfo setWroad(String wroad) {
        this.wroad = wroad;
        return this;
    }

    public int getRemainDis() {
        return remainDis;
    }

    public PAmapEventNavInfo setRemainDis(int remainDis) {
        this.remainDis = remainDis;
        return this;
    }

    public int getRemainTime() {
        return remainTime;
    }

    public PAmapEventNavInfo setRemainTime(int remainTime) {
        this.remainTime = remainTime;
        return this;
    }
}
