package com.wow.carlauncher.ex.manage.okHttp;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wow.carlauncher.common.LogEx;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpManage {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static OkHttpManage instance = new OkHttpManage();
    }

    public static OkHttpManage self() {
        return OkHttpManage.SingletonHolder.instance;
    }

    private OkHttpManage() {
        super();
    }

    private Context context;
    private OkHttpClient okHttpClient;

    public void init(Context context) {
        this.context = context;
        okHttpClient = new OkHttpClient();
    }

    public void get(String url, Callback callback) {
        LogEx.d(this, "get:" + url);
        Request request = new Request.Builder().get().url(url).build();
        OkHttpClient clientTemp;
        if (callback instanceof ProgressResponseListener) {
            clientTemp = getProgressClient((ProgressResponseListener) callback);
        } else {
            clientTemp = okHttpClient;
        }
        Call call = clientTemp.newCall(request);
        call.enqueue(callback);
    }


    private OkHttpClient getProgressClient(final ProgressResponseListener progressListener) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        //增加拦截器
        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //拦截
                Response originalResponse = chain.proceed(chain.request());
                //包装响应体并返回
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        });
        return client.build();

    }
}