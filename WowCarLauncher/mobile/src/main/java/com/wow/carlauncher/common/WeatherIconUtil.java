package com.wow.carlauncher.common;

import com.wow.carlauncher.R;

import java.util.Calendar;

/**
 * Created by 10124 on 2017/11/3.
 */

public class WeatherIconUtil {
    public static int getWeatherResId(String paramString) {
        if (!isNight()) {
            if (paramString.equals("晴")) {
                return R.mipmap.ic_sunny_big;
            }
            if ((paramString.equals("小雪") | paramString.equals("小雪-中雪"))) {
                return R.mipmap.ic_snow_big;
            }
            if ((paramString.equals("大雪") | paramString.equals("暴雪"))) {
                return R.mipmap.ic_heavysnow_big;
            }
            if (paramString.equals("雷阵雨")) {
                return R.mipmap.ic_thundeshower_big;
            }
            if (paramString.equals("雷阵雨伴有冰雹")) {
                return R.mipmap.ic_thundeshowehail_big;
            }
            if (paramString.equals("浮沉")) {
                return R.mipmap.ic_dust_big;
            }
            if (paramString.equals("扬沙")) {
                return R.mipmap.ic_dustblowing_big;
            }
            if (paramString.equals("冰雹")) {
                return R.mipmap.ic_hailstone_big;
            }
            if (paramString.equals("沙尘暴")) {
                return R.mipmap.ic_sandstorm_big;
            }
            if (paramString.equals("阴霾")) {
                return R.mipmap.ic_haze_big;
            }
            if (paramString.equals("多云-阵雨")) {
                return R.mipmap.ic_sunny_to_showery;
            }
            if (paramString.equals("晴-阵雨")) {
                return R.mipmap.ic_sunny_to_showery;
            }
            if (paramString.equals("晴")) {
                return R.mipmap.ic_nightsunny_big;
            }
            if (paramString.equals("阴")) {
                return R.mipmap.ic_overcast_big;
            }
            if ((paramString.equals("多云") | paramString.equals("多云-阴") | paramString.equals("多云-晴"))) {
                return R.mipmap.ic_cloudy_big;
            }
            if ((paramString.equals("阵雨") | paramString.equals("阵雨") | paramString.equals("小雨") | paramString.equals("小雨-阴") | paramString.equals("小雨-多云") | paramString.equals("小雨-中雨"))) {
                return R.mipmap.ic_lightrain_big;
            }
            if ((paramString.equals("中雨") | paramString.equals("中雨-大雨"))) {
                return R.mipmap.ic_moderraterain_big;
            }
            if ((paramString.equals("大雨") | paramString.equals("大雨-暴雨") | paramString.equals("暴雨") | paramString.equals("暴雨-大暴雨") | paramString.equals("大暴雨-特大暴雨"))) {
                return R.mipmap.ic_heavyrain_big;
            }
        } else {
            if (paramString.equals("晴")) {
                return R.mipmap.ic_nightsunny_big;
            }
            if ((paramString.equals("小雪") | paramString.equals("小雪-中雪"))) {
                return R.mipmap.ic_snow_big;
            }
            if ((paramString.equals("大雪") | paramString.equals("暴雪"))) {
                return R.mipmap.ic_heavysnow_big;
            }
            if (paramString.equals("雷阵雨")) {
                return R.mipmap.ic_thundeshower_big;
            }
            if (paramString.equals("雷阵雨伴有冰雹")) {
                return R.mipmap.ic_thundeshowehail_big;
            }
            if (paramString.equals("浮沉")) {
                return R.mipmap.ic_dust_big;
            }
            if (paramString.equals("扬沙")) {
                return R.mipmap.ic_dustblowing_big;
            }
            if (paramString.equals("冰雹")) {
                return R.mipmap.ic_hailstone_big;
            }
            if (paramString.equals("沙尘暴")) {
                return R.mipmap.ic_sandstorm_big;
            }
            if (paramString.equals("阴霾")) {
                return R.mipmap.ic_haze_big;
            }
            if (paramString.equals("多云-阵雨")) {
                return R.mipmap.ic_sunny_to_showery;
            }
            if (paramString.equals("晴-阵雨")) {
                return R.mipmap.ic_sunny_to_showery;
            }
            if (paramString.equals("晴")) {
                return R.mipmap.ic_nightsunny_big;
            }
            if (paramString.equals("阴")) {
                return R.mipmap.ic_overcast_big;
            }
            if ((paramString.equals("多云") | paramString.equals("多云-阴") | paramString.equals("多云-晴"))) {
                return R.mipmap.ic_nightcloudy_big;
            }
            if ((paramString.equals("阵雨") | paramString.equals("阵雨") | paramString.equals("小雨") | paramString.equals("小雨-阴") | paramString.equals("小雨-多云") | paramString.equals("小雨-中雨"))) {
                return R.mipmap.ic_nightrain_big;
            }
            if ((paramString.equals("中雨") | paramString.equals("中雨-大雨"))) {
                return R.mipmap.ic_moderraterain_big;
            }
            if ((paramString.equals("大雨") | paramString.equals("大雨-暴雨") | paramString.equals("暴雨") | paramString.equals("暴雨-大暴雨") | paramString.equals("大暴雨-特大暴雨"))) {
                return R.mipmap.ic_heavyrain_big;
            }
        }

        return R.mipmap.ic_default_big;
    }

    public static boolean isNight() {
        int i = Calendar.getInstance().get(Calendar.HOUR);
        return (i < 6) || (i >= 18);
    }
}
