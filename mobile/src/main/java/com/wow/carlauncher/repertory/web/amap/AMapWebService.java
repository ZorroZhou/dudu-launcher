package com.wow.carlauncher.repertory.web.amap;

import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.common.util.HttpUtil;
import com.wow.carlauncher.repertory.web.amap.res.BaseRes;
import com.wow.carlauncher.repertory.web.amap.res.WeatherRes;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by 10124 on 2017/10/29.
 */

public class AMapWebService {
    private static final String TAG = "AMapWebService";
    private static final String KEY = "b8a80f002ec3fe70454a4c013eaabbb7";

    public static void getWeatherInfo(String adcode, final CommonCallback commonCallback) {
        RequestParams params = new RequestParams("http://restapi.amap.com/v3/weather/weatherInfo?key=" + KEY + "&city=" + HttpUtil.getURLEncoderString(adcode));
        LogEx.e(AMapWebService.class, "这里请求了" + params);
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogEx.e(this, "onSuccess: " + result);
                if (commonCallback != null) {
                    commonCallback.callback(GsonUtil.getGson().fromJson(result, WeatherRes.class));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogEx.e(this, "onError: ");
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogEx.e(this, "onCancelled");
            }

            @Override
            public void onFinished() {
                LogEx.e(this, "onFinished");
            }
        });
    }

    public static class CommonCallback<T extends BaseRes> {
        public void callback(T res) {

        }
    }
}
