package com.wow.carlauncher.common;

import android.support.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class GsonListType implements ParameterizedType {
    Class clazz;

    public GsonListType(Class clz) {
        clazz = clz;
    }

    @NonNull
    @Override
    public Type[] getActualTypeArguments() {
        return new Type[]{clazz};
    }

    @NonNull
    @Override
    public Type getRawType() {
        return List.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
