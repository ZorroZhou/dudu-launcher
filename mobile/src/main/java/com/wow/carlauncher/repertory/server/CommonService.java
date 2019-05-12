package com.wow.carlauncher.repertory.server;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.ex.manage.OkHttpManage;
import com.wow.carlauncher.repertory.server.response.AppUpdate;
import com.wow.carlauncher.repertory.server.response.BaseResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CommonService {

    private final static String GET_UPDATE = "api/app/common/getUpdate/[TYPE]";
    private final static String GET_UPDATE_TYPE = "[TYPE]";

    public static void getUpdate(int type, final CommonCallback<AppUpdate> commonCallback) {
        OkHttpManage.self().get(ServerConstant.SERVER_URL + GET_UPDATE.replace(GET_UPDATE_TYPE, type + ""), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogEx.e(CommonService.class, "onError: ");
                e.printStackTrace();
                if (commonCallback != null) {
                    commonCallback.callback(null);
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    String result = response.body().string();
                    LogEx.d(CommonService.class, "onSuccess: " + result);
                    if (result.length() > 10) {
                        result = result.substring(9);
                        result = result.substring(0, result.length() - 1);
                        LogEx.d(CommonService.class, "onSuccess: " + result);
                        if (commonCallback != null) {
                            BaseResult<AppUpdate> res = GsonUtil.getGson().fromJson(result, new TypeToken<BaseResult<AppUpdate>>() {
                            }.getType());
                            commonCallback.callback(res);
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
}
