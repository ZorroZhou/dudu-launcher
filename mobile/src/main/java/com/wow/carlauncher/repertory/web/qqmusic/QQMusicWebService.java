package com.wow.carlauncher.repertory.web.qqmusic;

import android.support.annotation.NonNull;

import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.common.util.HttpUtil;
import com.wow.carlauncher.ex.manage.okHttp.OkHttpManage;
import com.wow.carlauncher.repertory.web.qqmusic.res.BaseRes;
import com.wow.carlauncher.repertory.web.qqmusic.res.SearchRes;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 10124 on 2017/10/29.
 */

public class QQMusicWebService {
    public static void searchMusic(String name, int num, final CommonCallback<SearchRes> commonCallback) {
        OkHttpManage.self().get("https://c.y.qq.com/soso/fcgi-bin/client_search_cp?aggr=1&cr=1&flag_qc=0&p=1&n=" + num + "&w=" + HttpUtil.getURLEncoderString(name), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogEx.e(QQMusicWebService.class, "onError: ");
                e.printStackTrace();
                if (commonCallback != null) {
                    commonCallback.callback(null);
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    String result = response.body().string();
                    LogEx.d(QQMusicWebService.class, "onSuccess: " + result);
                    if (result.length() > 10) {
                        result = result.substring(9);
                        result = result.substring(0, result.length() - 1);
                        LogEx.d(QQMusicWebService.class, "onSuccess: " + result);
                        if (commonCallback != null) {
                            commonCallback.callback(GsonUtil.getGson().fromJson(result, SearchRes.class));
                            return;
                        }
                    }
                }
                if (commonCallback != null) {
                    commonCallback.callback(null);
                }
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
