package com.wow.carlauncher.repertory.db.model;

import com.wow.frame.repertory.dbTool.BaseEntity;

/**
 * Created by 10124 on 2018/5/12.
 */

public class Trip extends BaseEntity {
    public final static int STATE_RUNNING = 1;
    public final static int STATE_OVER = 2;

    private Long startTime;
    private Long lastMarkTime;
    private Integer state;
    private Integer mileage;//里程 米

    public Integer getState() {
        return state;
    }

    public Trip setState(Integer state) {
        this.state = state;
        return this;
    }

    public Long getLastMarkTime() {
        return lastMarkTime;
    }

    public Trip setLastMarkTime(Long lastMarkTime) {
        this.lastMarkTime = lastMarkTime;
        return this;
    }

    public Integer getMileage() {
        return mileage;
    }

    public Trip setMileage(Integer mileage) {
        this.mileage = mileage;
        return this;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Trip setStartTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }
}
