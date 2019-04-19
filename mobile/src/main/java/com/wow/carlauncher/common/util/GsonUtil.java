package com.wow.carlauncher.common.util;

import com.google.gson.Gson;

public class GsonUtil {
    private static Gson gson;

    public static Gson getGson() {
        if (GsonUtil.gson == null) {
            GsonUtil.gson = new Gson();
        }
        return GsonUtil.gson;
    }
}
