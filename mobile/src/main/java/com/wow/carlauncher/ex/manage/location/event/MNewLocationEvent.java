package com.wow.carlauncher.ex.manage.location.event;

/**
 * Created by 10124 on 2018/5/12.
 */

public class MNewLocationEvent {
    private double latitude;
    private double longitude;
    private String adCode;
    private String city;

    public double getLatitude() {
        return latitude;
    }

    public MNewLocationEvent setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public MNewLocationEvent setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getAdCode() {
        return adCode;
    }

    public MNewLocationEvent setAdCode(String adCode) {
        this.adCode = adCode;
        return this;
    }

    public String getCity() {
        return city;
    }

    public MNewLocationEvent setCity(String city) {
        this.city = city;
        return this;
    }
}
