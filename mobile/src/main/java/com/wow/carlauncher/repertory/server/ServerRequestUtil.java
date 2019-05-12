package com.wow.carlauncher.repertory.server;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.ex.manage.OkHttpManage;
import com.wow.carlauncher.repertory.server.response.BaseResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ServerRequestUtil {
    public static <T> void get(String url, final CommonCallback<T> commonCallback) {
        OkHttpManage.self().get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogEx.e(ServerRequestUtil.class, "onError: ");
                e.printStackTrace();
                if (commonCallback != null) {
                    commonCallback.callback(false, "网络请求失败", null);
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    String result = response.body().string();
                    LogEx.d(ServerRequestUtil.class, "onSuccess: " + result);
                    if (result.length() > 2) {
                        if (commonCallback != null) {
                            BaseResult<T> res = GsonUtil.getGson().fromJson(result, new TypeToken<BaseResult<T>>() {
                            }.getType());
                            if (CommonUtil.equals(res.getCode(), 0)) {
                                commonCallback.callback(true, "", res.getData());
                            } else {
                                commonCallback.callback(false, res.getMsg(), null);
                            }
                            return;
                        }
                    }
                }
                if (commonCallback != null) {
                    commonCallback.callback(false, "网络请求失败", null);
                }
            }
        });
    }
}
