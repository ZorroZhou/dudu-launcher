package com.wow.frame.repertory.remote.callback;

/**
 * Created by Che on 2016/11/22 0022.
 */
public interface SCallBack<T> {
    void callback(boolean isok, String msg, T res);
}
