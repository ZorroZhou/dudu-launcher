package com.wow.frame.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wow.frame.SFrame;

import java.util.ArrayList;
import java.util.List;

public class JsonConvertUtil {
    public static <T> T toBean(Class<T> type, Object map) {
        Gson gson = SFrame.getGson();
        try {
            String json = gson.toJson(map);
            return gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//
//    public static <T> T toBean(TypeToken<T> type, Object map) {
//        Gson gson = SFrame.getGson();
//        try {
//            String json = gson.toJson(map);
//            return gson.fromJson(json, type.getType());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static <T> List<T> toList(TypeToken<List<T>> type, Object list) {
        Gson gson = SFrame.getGson();
        try {
            String json = gson.toJson(list);
            return gson.fromJson(json, type.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>(0);
    }
}
