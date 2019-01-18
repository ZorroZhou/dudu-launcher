package com.wow.carlauncher.ex.plugin.obd.evnet;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PObdEventCarInfo {
    private Integer speed;
    private Integer rev;
    private Integer waterTemp;
    private Integer oilConsumption;

    public Integer getSpeed() {
        return speed;
    }

    public PObdEventCarInfo setSpeed(Integer speed) {
        this.speed = speed;
        return this;
    }

    public Integer getRev() {
        return rev;
    }

    public PObdEventCarInfo setRev(Integer rev) {
        this.rev = rev;
        return this;
    }

    public Integer getWaterTemp() {
        return waterTemp;
    }

    public PObdEventCarInfo setWaterTemp(Integer waterTemp) {
        this.waterTemp = waterTemp;
        return this;
    }

    public Integer getOilConsumption() {
        return oilConsumption;
    }

    public PObdEventCarInfo setOilConsumption(Integer oilConsumption) {
        this.oilConsumption = oilConsumption;
        return this;
    }

    public PObdEventCarInfo() {
    }

    public PObdEventCarInfo(Integer speed, Integer rev, Integer waterTemp, Integer oilConsumption) {
        this.speed = speed;
        this.rev = rev;
        this.waterTemp = waterTemp;
        this.oilConsumption = oilConsumption;
    }
}

