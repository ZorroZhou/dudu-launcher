package com.wow.carlauncher.repertory.web.mobile;

/**
 * Created by LC on 2016/4/15.
 */
public interface JsonConverter {
    <T> T fromJson(String json, Class<T> classOfT);
    String toJson(Object src);
}
