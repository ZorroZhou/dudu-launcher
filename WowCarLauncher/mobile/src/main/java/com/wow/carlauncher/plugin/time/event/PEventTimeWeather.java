package com.wow.carlauncher.plugin.time.event;

import com.wow.carlauncher.plugin.amap.res.WeatherRes;

import java.util.Map;

/**
 * Created by 10124 on 2017/10/28.
 */

public class PEventTimeWeather {
    public String location;
    public WeatherRes.CityWeather weather;

    public PEventTimeWeather() {
    }

    public PEventTimeWeather(String location, WeatherRes.CityWeather weather) {
        this.location = location;
        this.weather = weather;
    }
}
