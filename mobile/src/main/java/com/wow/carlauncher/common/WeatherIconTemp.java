package com.wow.carlauncher.common;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.ThemeManage;

import java.util.Calendar;

/**
 * Created by 10124 on 2017/11/3.
 */

public class WeatherIconTemp {
    public static int getWeatherResId(String paramString) {
        if (ThemeManage.self().getTheme() != ThemeManage.Theme.CBLACK) {
            if (paramString.equals("晴")) {
                if (!isNight()) {
                    return R.mipmap.n_weather_q;
                } else {
                    return R.mipmap.n_weather_q;
                }
            } else if (paramString.equals("多云")) {
                if (!isNight()) {
                    return R.mipmap.n_weather_yin;
                } else {
                    return R.mipmap.n_weather_yin;
                }
            } else if (paramString.equals("阴")) {
                return R.mipmap.n_weather_yin;
            } else if (paramString.equals("阵雨")) {
                return R.mipmap.n_weather_yu;
            } else if (paramString.equals("雷阵雨")) {
                return R.mipmap.n_weather_lei;
            } else if (paramString.equals("雷阵雨伴有冰雹")) {
                return R.mipmap.n_weather_lei;
            } else if (paramString.equals("雨夹雪")) {
                return R.mipmap.n_weather_xue;
            } else if (paramString.equals("小雨")) {
                return R.mipmap.n_weather_yu;
            } else if (paramString.equals("中雨")) {
                return R.mipmap.n_weather_yu;
            } else if (paramString.equals("大雨")) {
                return R.mipmap.n_weather_yu;
            } else if (paramString.equals("暴雨")) {
                return R.mipmap.n_weather_yu;
            } else if (paramString.equals("大暴雨")) {
                return R.mipmap.n_weather_yu;
            } else if (paramString.equals("特大暴雨")) {
                return R.mipmap.n_weather_yu;
            } else if (paramString.equals("阵雪")) {
                return R.mipmap.n_weather_xue;
            } else if (paramString.equals("小雪")) {
                return R.mipmap.n_weather_xue;
            } else if (paramString.equals("中雪")) {
                return R.mipmap.n_weather_xue;
            } else if (paramString.equals("大雪")) {
                return R.mipmap.n_weather_xue;
            } else if (paramString.equals("暴雪")) {
                return R.mipmap.n_weather_xue;
            } else if (paramString.equals("雾")) {
                return R.mipmap.n_weather_wu;
            } else if (paramString.equals("冻雨")) {
                return R.mipmap.n_weather_yu;
            } else if (paramString.equals("小雨-中雨")) {
                return R.mipmap.n_weather_yu;
            } else if (paramString.equals("中雨-大雨")) {
                return R.mipmap.n_weather_yu;
            } else if (paramString.equals("大雨-暴雨")) {
                return R.mipmap.n_weather_yu;
            } else if (paramString.equals("暴雨-大暴雨")) {
                return R.mipmap.n_weather_yu;
            } else if (paramString.equals("大暴雨-特大暴雨")) {
                return R.mipmap.n_weather_yu;
            } else if (paramString.equals("小雪-中雪")) {
                return R.mipmap.n_weather_xue;
            } else if (paramString.equals("中雪-大雪")) {
                return R.mipmap.n_weather_xue;
            } else if (paramString.equals("大雪-暴雪")) {
                return R.mipmap.n_weather_xue;
            } else if (paramString.equals("沙尘暴")) {
                return R.mipmap.n_weather_sha;
            } else if (paramString.equals("浮沉")) {
                return R.mipmap.n_weather_mai;
            } else if (paramString.equals("扬沙")) {
                return R.mipmap.n_weather_sha;
            } else if (paramString.equals("强沙尘暴")) {
                return R.mipmap.n_weather_sha;
            } else if (paramString.equals("轻雾")) {
                return R.mipmap.n_weather_wu;
            } else if (paramString.equals("霾")) {
                return R.mipmap.n_weather_mai;
            }
            return R.mipmap.n_weather_weizhi;
        } else {
            if (paramString.equals("晴")) {
                if (!isNight()) {
                    return R.mipmap.n_weather_q_cb;
                } else {
                    return R.mipmap.n_weather_q_n_cb;
                }
            } else if (paramString.equals("多云")) {
                if (!isNight()) {
                    return R.mipmap.n_weather_duoyun_cb;
                } else {
                    return R.mipmap.n_weather_duoyun_n_cb;
                }
            } else if (paramString.equals("阴")) {
                return R.mipmap.n_weather_yin_cb;
            } else if (paramString.equals("阵雨")) {
                return R.mipmap.n_weather_xy_cb;
            } else if (paramString.equals("雷阵雨")) {
                return R.mipmap.n_weather_leizhenyu_cb;
            } else if (paramString.equals("雷阵雨伴有冰雹")) {
                return R.mipmap.n_weather_leibanbingbao_cb;
            } else if (paramString.equals("雨夹雪")) {
                return R.mipmap.n_weather_xx_cb;
            } else if (paramString.equals("小雨")) {
                return R.mipmap.n_weather_xy_cb;
            } else if (paramString.equals("中雨")) {
                return R.mipmap.n_weather_zy_cb;
            } else if (paramString.equals("大雨")) {
                return R.mipmap.n_weather_dy_cb;
            } else if (paramString.equals("暴雨")) {
                return R.mipmap.n_weather_by_cb;
            } else if (paramString.equals("大暴雨")) {
                return R.mipmap.n_weather_dby_cb;
            } else if (paramString.equals("特大暴雨")) {
                return R.mipmap.n_weather_tdby_cb;
            } else if (paramString.equals("阵雪")) {
                if (!isNight()) {
                    return R.mipmap.n_weather_zhenxue_cb;
                } else {
                    return R.mipmap.n_weather_zhenxue_n_cb;
                }
            } else if (paramString.equals("小雪")) {
                return R.mipmap.n_weather_xx_cb;
            } else if (paramString.equals("中雪")) {
                return R.mipmap.n_weather_zx_cb;
            } else if (paramString.equals("大雪")) {
                return R.mipmap.n_weather_dx_cb;
            } else if (paramString.equals("暴雪")) {
                return R.mipmap.n_weather_bx_cb;
            } else if (paramString.equals("雾")) {
                return R.mipmap.n_weather_wu_cb;
            } else if (paramString.equals("冻雨")) {
                return R.mipmap.n_weather_yujiaxue_cb;
            } else if (paramString.equals("小雨-中雨")) {
                return R.mipmap.n_weather_zy_cb;
            } else if (paramString.equals("中雨-大雨")) {
                return R.mipmap.n_weather_dy_cb;
            } else if (paramString.equals("大雨-暴雨")) {
                return R.mipmap.n_weather_by_cb;
            } else if (paramString.equals("暴雨-大暴雨")) {
                return R.mipmap.n_weather_dby_cb;
            } else if (paramString.equals("大暴雨-特大暴雨")) {
                return R.mipmap.n_weather_tdby_cb;
            } else if (paramString.equals("小雪-中雪")) {
                return R.mipmap.n_weather_zx_cb;
            } else if (paramString.equals("中雪-大雪")) {
                return R.mipmap.n_weather_dx_cb;
            } else if (paramString.equals("大雪-暴雪")) {
                return R.mipmap.n_weather_bx_cb;
            } else if (paramString.equals("沙尘暴")) {
                return R.mipmap.n_weather_shachenbao_cb;
            } else if (paramString.equals("浮沉")) {
                return R.mipmap.n_weather_fuchen_cb;
            } else if (paramString.equals("扬沙")) {
                return R.mipmap.n_weather_yangsha_cb;
            } else if (paramString.equals("强沙尘暴")) {
                return R.mipmap.n_weather_qiangshachenbao_cb;
            } else if (paramString.equals("轻雾")) {
                return R.mipmap.n_weather_wu_cb;
            } else if (paramString.equals("霾")) {
                return R.mipmap.n_weather_mai_cb;
            }
            return R.mipmap.n_weather_weizhi_cb;
        }
    }

    static boolean isNight() {
        int i = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return (i < 6) || (i >= 18);
    }
}
