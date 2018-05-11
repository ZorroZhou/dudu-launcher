package com.wow.carlauncher.ex.manage.trip;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wow.carlauncher.ex.ContextEx;

/**
 * Created by 10124 on 2018/5/11.
 */

public class TripManage extends ContextEx {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static TripManage instance = new TripManage();
    }

    public static TripManage self() {
        return TripManage.SingletonHolder.instance;
    }

    private TripManage() {
        super();
    }

    public void init(Context context) {
        setContext(context);
    }
}
