package com.wow.carlauncher.ex.manage.okHttp;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.common.LogEx;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
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

        long t1 = System.currentTimeMillis();
        okHttpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
        LogEx.d(this, "init time:" + (System.currentTimeMillis() - t1));
    }

    public Call get(String url, Callback callback) {
        LogEx.d(this, "get:" + url);
        Request.Builder builder = new Request.Builder().get().url(url);
        if (AppContext.self().getLocalUser() != null) {
            builder.addHeader("token", AppContext.self().getLocalUser().getToken());
        }
        Request request = builder.build();
        OkHttpClient clientTemp;
        if (callback instanceof ProgressResponseListener) {
            clientTemp = getProgressClient((ProgressResponseListener) callback);
        } else {
            clientTemp = okHttpClient;
        }
        Call call = clientTemp.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public Call post(String url, Map<String, Object> param, Callback callback) {
        LogEx.d(this, "get:" + url);
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (String key : param.keySet()) {
            formBuilder.add(key, String.valueOf(param.get(key)));
        }
        Request.Builder builder = new Request.Builder().post(formBuilder.build()).url(url);
        if (AppContext.self().getLocalUser() != null) {
            builder.addHeader("token", AppContext.self().getLocalUser().getToken());
        }
        Request request = builder.build();

        OkHttpClient clientTemp;
        if (callback instanceof ProgressResponseListener) {
            clientTemp = getProgressClient((ProgressResponseListener) callback);
        } else {
            clientTemp = okHttpClient;
        }
        Call call = clientTemp.newCall(request);
        call.enqueue(callback);
        return call;
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