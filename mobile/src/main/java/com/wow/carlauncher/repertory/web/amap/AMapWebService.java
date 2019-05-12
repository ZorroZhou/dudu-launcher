package com.wow.carlauncher.repertory.web.amap;

import android.support.annotation.NonNull;

import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.common.util.HttpUtil;
import com.wow.carlauncher.ex.manage.okHttp.OkHttpManage;
import com.wow.carlauncher.repertory.web.amap.res.BaseRes;
import com.wow.carlauncher.repertory.web.amap.res.WeatherRes;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 10124 on 2017/10/29.
 */

public class AMapWebService {
    private static final String KEY = "b8a80f002ec3fe70454a4c013eaabbb7";

    public static void getWeatherInfo(String adcode, final CommonCallback<WeatherRes> commonCallback) {
        LogEx.d(AMapWebService.class, "这里请求了天气信息:" + adcode);
        OkHttpManage.self().get("http://restapi.amap.com/v3/weather/weatherInfo?key=" + KEY + "&city=" + HttpUtil.getURLEncoderString(adcode), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogEx.e(AMapWebService.class, "onError: ");
                e.printStackTrace();
                if (commonCallback != null) {
                    commonCallback.callback(null);
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    String result = response.body().string();
                    LogEx.d(AMapWebService.class, "onSuccess: " + result);
                    if (commonCallback != null) {
                        commonCallback.callback(GsonUtil.getGson().fromJson(result, WeatherRes.class));
                        return;
                    }
                }
                if (commonCallback != null) {
                    commonCallback.callback(null);
                }
            }
        });
    }

    public static class CommonCallback<T extends BaseRes> {
        public void callback(T res) {

        }
    }
}
