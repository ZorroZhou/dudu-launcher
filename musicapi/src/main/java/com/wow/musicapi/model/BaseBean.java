package com.wow.musicapi.model;

import com.wow.frame.SFrame;

import java.io.Serializable;

/**
 * Created by haohua on 2018/2/9.
 */
public abstract class BaseBean implements Cloneable, Serializable {
    @Override
    public String toString() {
        return SFrame.getGson().toJson(this);
    }
}
