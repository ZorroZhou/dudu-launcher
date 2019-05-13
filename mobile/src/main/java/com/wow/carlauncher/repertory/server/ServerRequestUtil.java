package com.wow.carlauncher.repertory.server;

import android.support.annotation.NonNull;

import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.ex.manage.okHttp.OkHttpManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.server.response.BaseResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ServerRequestUtil {
    public static <D, T extends BaseResult<D>> Call get(String url, Class<T> clazz, final CommonCallback<D> commonCallback) {
        return OkHttpManage.self().get(url, new Callback() {
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
                            BaseResult<D> res = GsonUtil.getGson().fromJson(result, clazz);
                            if (CommonUtil.equals(res.getCode(), 0)) {
                                commonCallback.callback(true, "", res.getData());
                            } else {
                                ToastManage.self().show(res.getMsg());
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
