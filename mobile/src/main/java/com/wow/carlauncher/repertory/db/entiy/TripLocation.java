package com.wow.carlauncher.repertory.db.entiy;

import com.wow.carlauncher.repertory.db.manage.BaseEntity;
import com.wow.carlauncher.repertory.db.manage.Table;

@Table(name = "TripLocation")
public class TripLocation extends BaseEntity {
    private Long trip;
    private Long time;
    private String location;

    public Long getTrip() {
        return trip;
    }

    public void setTrip(Long trip) {
        this.trip = trip;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
