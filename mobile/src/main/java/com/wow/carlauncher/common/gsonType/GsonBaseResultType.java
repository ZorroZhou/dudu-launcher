package com.wow.carlauncher.common.gsonType;

import android.support.annotation.NonNull;

import com.wow.carlauncher.repertory.server.response.BaseResult;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GsonBaseResultType implements ParameterizedType {
    Type clazz;

    public GsonBaseResultType(Type clz) {
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
        return BaseResult.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
