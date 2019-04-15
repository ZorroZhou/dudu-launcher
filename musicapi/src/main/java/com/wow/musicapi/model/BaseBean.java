package com.wow.musicapi.model;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by haohua on 2018/2/9.
 */
public abstract class BaseBean implements Cloneable, Serializable {
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
