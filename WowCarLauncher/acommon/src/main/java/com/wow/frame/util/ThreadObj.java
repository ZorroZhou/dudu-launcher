package com.wow.frame.util;

/**
 * Created by 10124 on 2017/10/30.
 */

public class ThreadObj<T> {
    public ThreadObj() {
    }

    public ThreadObj(T obj) {
        this.obj = obj;
    }

    private T obj;

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
