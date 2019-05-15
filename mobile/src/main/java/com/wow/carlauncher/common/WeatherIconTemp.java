package com.wow.carlauncher.common;

import com.wow.carlauncher.R;

import java.util.Calendar;

/**
 * Created by 10124 on 2017/11/3.
 */

public class WeatherIconTemp {
    public static int getWeatherResId(String paramString) {
        if (paramString.equals("晴")) {
            if (!isNight()) {
                return R.drawable.theme_weather_q;
            } else {
                return R.drawable.theme_weather_q_n;
            }
        } else if (paramString.equals("多云")) {
            if (!isNight()) {
                return R.drawable.theme_weather_duoyun;
            } else {
                return R.drawable.theme_weather_duoyun_n;
            }
        } else if (paramString.equals("阴")) {
            return R.drawable.theme_weather_yin;
        } else if (paramString.equals("阵雨")) {
            return R.drawable.theme_weather_xy;
        } else if (paramString.equals("雷阵雨")) {
            return R.drawable.theme_weather_leizhenyu;
        } else if (paramString.equals("雷阵雨伴有冰雹")) {
            return R.drawable.theme_weather_leibanbingbao;
        } else if (paramString.equals("雨夹雪")) {
            return R.drawable.theme_weather_yujiaxue;
        } else if (paramString.equals("小雨")) {
            return R.drawable.theme_weather_xy;
        } else if (paramString.equals("中雨")) {
            return R.drawable.theme_weather_zy;
        } else if (paramString.equals("大雨")) {
            return R.drawable.theme_weather_dy;
        } else if (paramString.equals("暴雨")) {
            return R.drawable.theme_weather_by;
        } else if (paramString.equals("大暴雨")) {
            return R.drawable.theme_weather_dby;
        } else if (paramString.equals("特大暴雨")) {
            return R.drawable.theme_weather_tdby;
        } else if (paramString.equals("阵雪")) {
            if (!isNight()) {
                return R.drawable.theme_weather_zhenxue;
            } else {
                return R.drawable.theme_weather_zhenxue_n;
            }
        } else if (paramString.equals("小雪")) {
            return R.drawable.theme_weather_xx;
        } else if (paramString.equals("中雪")) {
            return R.drawable.theme_weather_zx;
        } else if (paramString.equals("大雪")) {
            return R.drawable.theme_weather_dx;
        } else if (paramString.equals("暴雪")) {
            return R.drawable.theme_weather_bx;
        } else if (paramString.equals("雾")) {
            return R.drawable.theme_weather_wu;
        } else if (paramString.equals("冻雨")) {
            return R.drawable.theme_weather_yujiaxue;
        } else if (paramString.equals("小雨-中雨")) {
            return R.drawable.theme_weather_zy;
        } else if (paramString.equals("中雨-大雨")) {
            return R.drawable.theme_weather_dy;
        } else if (paramString.equals("大雨-暴雨")) {
            return R.drawable.theme_weather_by;
        } else if (paramString.equals("暴雨-大暴雨")) {
            return R.drawable.theme_weather_dby;
        } else if (paramString.equals("大暴雨-特大暴雨")) {
            return R.drawable.theme_weather_tdby;
        } else if (paramString.equals("小雪-中雪")) {
            return R.drawable.theme_weather_zx;
        } else if (paramString.equals("中雪-大雪")) {
            return R.drawable.theme_weather_dx;
        } else if (paramString.equals("大雪-暴雪")) {
            return R.drawable.theme_weather_bx;
        } else if (paramString.equals("沙尘暴")) {
            return R.drawable.theme_weather_shachenbao;
        } else if (paramString.equals("浮沉")) {
            return R.drawable.theme_weather_fuchen;
        } else if (paramString.equals("扬沙")) {
            return R.drawable.theme_weather_yangsha;
        } else if (paramString.equals("强沙尘暴")) {
            return R.drawable.theme_weather_qiangshachenbao;
        } else if (paramString.equals("轻雾")) {
            return R.drawable.theme_weather_wu;
        } else if (paramString.equals("霾")) {
            return R.drawable.theme_weather_mai;
        }
        return R.drawable.theme_weather_weizhi;
    }

    static boolean isNight() {
        int i = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return (i < 6) || (i >= 18);
    }
}
