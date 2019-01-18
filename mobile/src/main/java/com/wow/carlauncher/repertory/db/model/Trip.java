package com.wow.carlauncher.repertory.db.model;

import com.wow.frame.repertory.dbTool.BaseEntity;
import com.wow.frame.repertory.dbTool.Table;

/**
 * Created by 10124 on 2018/5/12.
 */
@Table(name = "Trip")
public class Trip extends BaseEntity {
    private Long startTime;
    private Long lastMarkTime;
    private Integer mileage;//里程 米

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
