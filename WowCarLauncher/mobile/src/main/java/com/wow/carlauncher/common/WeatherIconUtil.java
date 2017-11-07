package com.wow.carlauncher.common;

import com.wow.carlauncher.R;

import java.util.Calendar;

/**
 * Created by 10124 on 2017/11/3.
 */

public class WeatherIconUtil {
    public static int getWeatherResId(String paramString) {
        if (paramString.equals("晴")) {
            if (!isNight()) {
                return R.mipmap.wic_sunny;
            } else {
                return R.mipmap.wic_nigth_sunny;
            }
        } else if (paramString.equals("多云")) {
            if (!isNight()) {
                return R.mipmap.wic_cloudy;
            } else {
                return R.mipmap.wic_nigth_cloudy;
            }
        } else if (paramString.equals("阴")) {
            return R.mipmap.wic_overcast;
        } else if (paramString.equals("阵雨")) {
            return R.mipmap.wic_showery;
        } else if (paramString.equals("雷阵雨")) {
            return R.mipmap.wic_thundeshower;
        } else if (paramString.equals("雷阵雨伴有冰雹")) {
            return R.mipmap.wic_thundeshowehail;
        } else if (paramString.equals("雨夹雪")) {
            return R.mipmap.wic_sleet;
        } else if (paramString.equals("小雨")) {
            return R.mipmap.wic_light_rain;
        } else if (paramString.equals("中雨")) {
            return R.mipmap.wic_moderate_rain;
        } else if (paramString.equals("大雨")) {
            return R.mipmap.wic_big_rain;
        } else if (paramString.equals("暴雨")) {
            return R.mipmap.wic_big_rain;
        } else if (paramString.equals("大暴雨")) {
            return R.mipmap.wic_big_rain;
        } else if (paramString.equals("特大暴雨")) {
            return R.mipmap.wic_big_rain;
        } else if (paramString.equals("阵雪")) {
            return R.mipmap.wic_snow_shower;
        } else if (paramString.equals("小雪")) {
            return R.mipmap.wic_light_snow;
        } else if (paramString.equals("中雪")) {
            return R.mipmap.wic_moderate_snow;
        } else if (paramString.equals("大雪")) {
            return R.mipmap.wic_big_snow;
        } else if (paramString.equals("暴雪")) {
            return R.mipmap.wic_bbig_snow;
        } else if (paramString.equals("雾")) {
            return R.mipmap.wic_fog;
        } else if (paramString.equals("冻雨")) {
            return R.mipmap.wic_ice_rain;
        } else if (paramString.equals("小雨-中雨")) {
            return R.mipmap.wic_ltm_rain;
        } else if (paramString.equals("中雨-大雨")) {
            return R.mipmap.wic_mtb_rain;
        } else if (paramString.equals("大雨-暴雨")) {
            return R.mipmap.wic_btbb_rain;
        } else if (paramString.equals("暴雨-大暴雨")) {
            return R.mipmap.wic_bbtbbb_rain;
        } else if (paramString.equals("大暴雨-特大暴雨")) {
            return R.mipmap.wic_bbbtbbbb_rain;
        } else if (paramString.equals("小雪-中雪")) {
            return R.mipmap.wic_ltm_snow;
        } else if (paramString.equals("中雪-大雪")) {
            return R.mipmap.wic_mtb_snow;
        } else if (paramString.equals("大雪-暴雪")) {
            return R.mipmap.wic_btbb_snow;
        } else if (paramString.equals("沙尘暴")) {
            return R.mipmap.wic_sand_storm;
        } else if (paramString.equals("浮沉")) {
            return R.mipmap.wic_dust;
        } else if (paramString.equals("扬沙")) {
            return R.mipmap.wic_yangsha;
        } else if (paramString.equals("强沙尘暴")) {
            return R.mipmap.wic_big_storm;
        } else if (paramString.equals("轻雾")) {
            return R.mipmap.wic_wu;
        } else if (paramString.equals("霾")) {
            return R.mipmap.wic_wu;
        }
        return R.mipmap.wic_no;
    }

    static boolean isNight() {
        int i = Calendar.getInstance().get(Calendar.HOUR);
        return (i < 6) || (i >= 18);
    }
}
