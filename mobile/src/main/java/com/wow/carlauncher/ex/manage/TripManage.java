package com.wow.carlauncher.ex.manage;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wow.carlauncher.ex.ContextEx;

public class TripManage extends ContextEx {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static TripManage instance = new TripManage();
    }

    public static TripManage self() {
        return TripManage.SingletonHolder.instance;
    }

    private TripManage() {
    }

    public void init(Context context) {
        setContext(context);
    }
}
