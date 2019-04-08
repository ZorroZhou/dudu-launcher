package com.wow.carlauncher.repertory.amapWebservice.res;

import java.util.List;

/**
 * Created by 10124 on 2017/10/29.
 */

public class WeatherRes extends BaseRes {
    private List<CityWeather> lives;

    public List<CityWeather> getLives() {
        return lives;
    }

    public void setLives(List<CityWeather> lives) {
        this.lives = lives;
    }

    public static class CityWeather {
        private String province;
        private String city;
        private String adcode;
        private String weather;
        private Integer temperature;
        private String winddirection;
        private String windpower;
        private Integer humidity;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public Integer getTemperature() {
            return temperature;
        }

        public void setTemperature(Integer temperature) {
            this.temperature = temperature;
        }

        public String getWinddirection() {
            return winddirection;
        }

        public void setWinddirection(String winddirection) {
            this.winddirection = winddirection;
        }

        public String getWindpower() {
            return windpower;
        }

        public void setWindpower(String windpower) {
            this.windpower = windpower;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public void setHumidity(Integer humidity) {
            this.humidity = humidity;
        }
    }
}
