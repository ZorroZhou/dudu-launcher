package com.wow.carlauncher.repertory.qqmusicService.res;

public class BaseRes<T> {
    private Integer code;
    private T data;

    public Integer getCode() {
        return code;
    }

    public BaseRes<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public T getData() {
        return data;
    }

    public BaseRes<T> setData(T data) {
        this.data = data;
        return this;
    }
}
