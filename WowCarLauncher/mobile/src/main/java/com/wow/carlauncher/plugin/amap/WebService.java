package com.wow.carlauncher.plugin.amap;

import android.util.Log;

import com.google.gson.Gson;
import com.wow.carlauncher.plugin.amap.res.BaseRes;
import com.wow.carlauncher.plugin.amap.res.WeatherRes;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

/**
 * Created by 10124 on 2017/10/29.
 */

public class WebService {
    private static final String TAG = "WebService";
    private static Gson gson = new Gson();
    private static final String KEY = "31d8bdc3dd120568d55288e82737da61";

    public static void getWeatherInfo(String adcode, final CommonCallback commonCallback) {
        RequestParams params = new RequestParams("http://restapi.amap.com/v3/weather/weatherInfo?key=" + KEY + "&city=" + adcode);
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                if (commonCallback != null) {
                    commonCallback.callback(gson.fromJson(result, WeatherRes.class));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    public static class CommonCallback<T extends BaseRes> {
        public void callback(T res) {

        }
    }
}
