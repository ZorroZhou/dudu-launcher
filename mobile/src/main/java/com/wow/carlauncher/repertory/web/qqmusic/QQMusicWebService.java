package com.wow.carlauncher.repertory.web.qqmusic;

import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.common.util.HttpUtil;
import com.wow.carlauncher.repertory.web.qqmusic.res.BaseRes;
import com.wow.carlauncher.repertory.web.qqmusic.res.SearchRes;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by 10124 on 2017/10/29.
 */

public class QQMusicWebService {
    private static final String TAG = "QQMusicWebService";

    public static void searchMusic(String name, int num, final CommonCallback commonCallback) {

        RequestParams params = new RequestParams("https://c.y.qq.com/soso/fcgi-bin/client_search_cp?aggr=1&cr=1&flag_qc=0&p=1&n=" + num + "&w=" + HttpUtil.getURLEncoderString(name));
        LogEx.d(QQMusicWebService.class, "这里请求了" + params);
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogEx.d(QQMusicWebService.class, "onSuccess: " + result);
                if (result.length() > 10) {
                    result = result.substring(9);
                    result = result.substring(0, result.length() - 1);
                    LogEx.d(QQMusicWebService.class, "onSuccess: " + result);
                    if (commonCallback != null) {
                        commonCallback.callback(GsonUtil.getGson().fromJson(result, SearchRes.class));
                    }
                } else {
                    throw new RuntimeException("数据错误");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogEx.e(QQMusicWebService.class, "onError: ");
                ex.printStackTrace();
                if (commonCallback != null) {
                    commonCallback.callback(null);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogEx.d(QQMusicWebService.class, "onCancelled");
            }

            @Override
            public void onFinished() {
                LogEx.e(QQMusicWebService.class, "onFinished");
            }
        });
    }

    public static String picUrl(int id) {
        return "http://imgcache.qq.com/music/photo/album_300/" + (id % 100) + "/300_albumpic_" + id + "_0.jpg";
    }


    public static class CommonCallback<T extends BaseRes> {
        public void callback(T res) {

        }
    }
}
