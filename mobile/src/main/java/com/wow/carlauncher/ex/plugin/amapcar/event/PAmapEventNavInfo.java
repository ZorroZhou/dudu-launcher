package com.wow.carlauncher.ex.plugin.amapcar.event;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PAmapEventNavInfo {
    //事件类型
    private int type;
    //图标
    private int icon;
    private int segRemainDis; //当前导航段剩余距离，对应的值为int类型，单位：米
    private String nextRoadName;//下一道路名
    private String curRoadName;//当前道路名称
    private int routeRemainDis;//路径剩余距离，对应的值为int类型，单位：米
    private int routeRemainTime;//路径剩余时间，对应的值为int类型，单位：秒
    private int routeAllDis;//路径总距离，对应的值为int类型，单位：米
    private int routeAllTime;//路径总时间，对应的值为int类型，单位：秒
    private int curSpeed;//当前车速，对应的值为int类型，单位：公里/小时
    private int cameraSpeed;//电子眼限速度，对应的值为int类型，无限速则为0，单位：公里/小时
    private int roadType;//当前道路类型，对应的值为int类型

    //0：高速公路
    //1：国道
    //2：省道
    //3：县道
    //4：乡公路
    //5：县乡村内部道路
    //6：主要大街、城市快速道
    //7：主要道路
    //8：次要道路
    //9：普通道路
    //10：非导航道路
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

    public int getSegRemainDis() {
        return segRemainDis;
    }

    public PAmapEventNavInfo setSegRemainDis(int segRemainDis) {
        this.segRemainDis = segRemainDis;
        return this;
    }

    public String getNextRoadName() {
        return nextRoadName;
    }

    public PAmapEventNavInfo setNextRoadName(String nextRoadName) {
        this.nextRoadName = nextRoadName;
        return this;
    }

    public String getCurRoadName() {
        return curRoadName;
    }

    public PAmapEventNavInfo setCurRoadName(String curRoadName) {
        this.curRoadName = curRoadName;
        return this;
    }

    public int getRouteRemainDis() {
        return routeRemainDis;
    }

    public PAmapEventNavInfo setRouteRemainDis(int routeRemainDis) {
        this.routeRemainDis = routeRemainDis;
        return this;
    }

    public int getRouteRemainTime() {
        return routeRemainTime;
    }

    public PAmapEventNavInfo setRouteRemainTime(int routeRemainTime) {
        this.routeRemainTime = routeRemainTime;
        return this;
    }

    public int getRouteAllDis() {
        return routeAllDis;
    }

    public PAmapEventNavInfo setRouteAllDis(int routeAllDis) {
        this.routeAllDis = routeAllDis;
        return this;
    }

    public int getRouteAllTime() {
        return routeAllTime;
    }

    public PAmapEventNavInfo setRouteAllTime(int routeAllTime) {
        this.routeAllTime = routeAllTime;
        return this;
    }

    public int getCurSpeed() {
        return curSpeed;
    }

    public PAmapEventNavInfo setCurSpeed(int curSpeed) {
        this.curSpeed = curSpeed;
        return this;
    }

    public int getCameraSpeed() {
        return cameraSpeed;
    }

    public PAmapEventNavInfo setCameraSpeed(int cameraSpeed) {
        this.cameraSpeed = cameraSpeed;
        return this;
    }

    public int getRoadType() {
        return roadType;
    }

    public PAmapEventNavInfo setRoadType(int roadType) {
        this.roadType = roadType;
        return this;
    }

    @Override
    public String toString() {
        return "PAmapEventNavInfo{" +
                "type=" + type +
                ", icon=" + icon +
                ", segRemainDis=" + segRemainDis +
                ", nextRoadName='" + nextRoadName + '\'' +
                ", curRoadName='" + curRoadName + '\'' +
                ", routeRemainDis=" + routeRemainDis +
                ", routeRemainTime=" + routeRemainTime +
                ", routeAllDis=" + routeAllDis +
                ", routeAllTime=" + routeAllTime +
                ", curSpeed=" + curSpeed +
                ", cameraSpeed=" + cameraSpeed +
                ", roadType=" + roadType +
                '}';
    }
}
