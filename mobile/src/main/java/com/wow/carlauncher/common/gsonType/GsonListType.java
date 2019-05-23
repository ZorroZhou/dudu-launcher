package com.wow.carlauncher.common.gsonType;

import android.support.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class GsonListType implements ParameterizedType {
    Type clazz;

    public GsonListType(Type clz) {
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
