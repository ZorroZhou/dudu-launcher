package com.wow.carlauncher.ex.plugin.obd.evnet;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PObdEventCarTp {
    private Float lFTirePressure;
    private Integer lFTemp;
    private Float rFTirePressure;
    private Integer rFTemp;
    private Float lBTirePressure;
    private Integer lBTemp;
    private Float rBTirePressure;
    private Integer rBTemp;

    public Float getlFTirePressure() {
        return lFTirePressure;
    }

    public PObdEventCarTp setlFTirePressure(Float lFTirePressure) {
        this.lFTirePressure = lFTirePressure;
        return this;
    }

    public Integer getlFTemp() {
        return lFTemp;
    }

    public PObdEventCarTp setlFTemp(Integer lFTemp) {
        this.lFTemp = lFTemp;
        return this;
    }

    public Float getrFTirePressure() {
        return rFTirePressure;
    }

    public PObdEventCarTp setrFTirePressure(Float rFTirePressure) {
        this.rFTirePressure = rFTirePressure;
        return this;
    }

    public Integer getrFTemp() {
        return rFTemp;
    }

    public PObdEventCarTp setrFTemp(Integer rFTemp) {
        this.rFTemp = rFTemp;
        return this;
    }

    public Float getlBTirePressure() {
        return lBTirePressure;
    }

    public PObdEventCarTp setlBTirePressure(Float lBTirePressure) {
        this.lBTirePressure = lBTirePressure;
        return this;
    }

    public Integer getlBTemp() {
        return lBTemp;
    }

    public PObdEventCarTp setlBTemp(Integer lBTemp) {
        this.lBTemp = lBTemp;
        return this;
    }

    public Float getrBTirePressure() {
        return rBTirePressure;
    }

    public PObdEventCarTp setrBTirePressure(Float rBTirePressure) {
        this.rBTirePressure = rBTirePressure;
        return this;
    }

    public Integer getrBTemp() {
        return rBTemp;
    }

    public PObdEventCarTp setrBTemp(Integer rBTemp) {
        this.rBTemp = rBTemp;
        return this;
    }

    public PObdEventCarTp() {
    }

    public PObdEventCarTp(Float lFTirePressure, Integer lFTemp, Float rFTirePressure, Integer rFTemp, Float lBTirePressure, Integer lBTemp, Float rBTirePressure, Integer rBTemp) {
        this.lFTirePressure = lFTirePressure;
        this.lFTemp = lFTemp;
        this.rFTirePressure = rFTirePressure;
        this.rFTemp = rFTemp;
        this.lBTirePressure = lBTirePressure;
        this.lBTemp = lBTemp;
        this.rBTirePressure = rBTirePressure;
        this.rBTemp = rBTemp;
    }
}
