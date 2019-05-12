package com.wow.carlauncher.ex.manage.location;

/**
 * Created by 10124 on 2018/5/12.
 */

public class LMEventNewLocation {
    private double latitude;
    private double longitude;
    private String adCode;
    private String city;
    private String district;
    private float speed;
    private float bearing;
    private int locationType;

    public int getLocationType() {
        return locationType;
    }

    public LMEventNewLocation setLocationType(int locationType) {
        this.locationType = locationType;
        return this;
    }

    public float getBearing() {
        return bearing;
    }

    public LMEventNewLocation setBearing(float bearing) {
        this.bearing = bearing;
        return this;
    }

    public float getSpeed() {
        return speed;
    }

    public LMEventNewLocation setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public LMEventNewLocation setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public LMEventNewLocation setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getAdCode() {
        return adCode;
    }

    public LMEventNewLocation setAdCode(String adCode) {
        this.adCode = adCode;
        return this;
    }

    public String getCity() {
        return city;
    }

    public LMEventNewLocation setCity(String city) {
        this.city = city;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public LMEventNewLocation setDistrict(String district) {
        this.district = district;
        return this;
    }
}
