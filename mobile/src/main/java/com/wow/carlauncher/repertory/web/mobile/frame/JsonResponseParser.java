package com.wow.carlauncher.repertory.web.mobile.frame;

import android.util.Log;

import com.google.gson.Gson;
import com.wow.carlauncher.common.util.GsonUtil;

import java.lang.reflect.Type;

/**
 * Created by Che on 16/11/22.
 */
public class JsonResponseParser implements JsonConverter {
    private static final String TAG = "JsonResponseParser";
    private Gson gson;

    public JsonResponseParser() {
        gson = GsonUtil.getGson();
    }

    @Override
    public <T> T fromJson(String json, Class<T> classOfT) {
        Log.w(TAG, "fromJson: " + json);
        return gson.fromJson(json, classOfT);
    }

    public <T> T fromJson(String json, Type typeOfT) {
        Log.w(TAG, "fromJson: " + json);
        return gson.fromJson(json, typeOfT);
    }

    @Override
    public String toJson(Object src) {
        String r = gson.toJson(src);
        Log.w(TAG, "toJson: " + r);
        return r;
    }
}
