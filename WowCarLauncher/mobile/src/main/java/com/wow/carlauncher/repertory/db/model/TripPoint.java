package com.wow.carlauncher.repertory.db.model;

import com.wow.frame.repertory.dbTool.BaseEntity;
import com.wow.frame.repertory.dbTool.Table;

/**
 * Created by 10124 on 2018/5/12.
 */
@Table(name = "TripPoint")
public class TripPoint extends BaseEntity {
    private Long trip;
    private Double lat;
    private Double lon;
    private Integer speed;
    private Long time;

    public Long getTrip() {
        return trip;
    }

    public TripPoint setTrip(Long trip) {
        this.trip = trip;
        return this;
    }

    public Double getLat() {
        return lat;
    }

    public TripPoint setLat(Double lat) {
        this.lat = lat;
        return this;
    }

    public Double getLon() {
        return lon;
    }

    public TripPoint setLon(Double lon) {
        this.lon = lon;
        return this;
    }

    public Integer getSpeed() {
        return speed;
    }

    public TripPoint setSpeed(Integer speed) {
        this.speed = speed;
        return this;
    }

    public Long getTime() {
        return time;
    }

    public TripPoint setTime(Long time) {
        this.time = time;
        return this;
    }
}
