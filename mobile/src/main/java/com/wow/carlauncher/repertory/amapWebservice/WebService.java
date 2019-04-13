package com.wow.carlauncher.repertory.amapWebservice;

import android.util.Log;

import com.wow.carlauncher.repertory.amapWebservice.res.BaseRes;
import com.wow.carlauncher.repertory.amapWebservice.res.WeatherRes;
import com.wow.frame.SFrame;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;

/**
 * Created by 10124 on 2017/10/29.
 */

public class WebService {
    private static final String TAG = "WebService";
    private static final String KEY = "b8a80f002ec3fe70454a4c013eaabbb7";

    public static void getWeatherInfo(String adcode, final CommonCallback commonCallback) {
        RequestParams params = new RequestParams("http://restapi.amap.com/v3/weather/weatherInfo?key=" + KEY + "&city=" + getURLEncoderString(adcode));
        Log.e(TAG, "这里请求了" + params);
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                if (commonCallback != null) {
                    commonCallback.callback(SFrame.getGson().fromJson(result, WeatherRes.class));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: ");
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(TAG, "onCancelled");
            }

            @Override
            public void onFinished() {
                Log.e(TAG, "onFinished");
            }
        });
    }

    public static class CommonCallback<T extends BaseRes> {
        public void callback(T res) {

        }
    }

    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
