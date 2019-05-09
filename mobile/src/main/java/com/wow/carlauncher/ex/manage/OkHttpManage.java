package com.wow.carlauncher.ex.manage;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wow.carlauncher.common.LogEx;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

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
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }
}
