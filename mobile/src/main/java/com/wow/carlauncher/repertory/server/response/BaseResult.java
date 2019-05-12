package com.wow.carlauncher.repertory.server.response;

public class BaseResult<T> {
    private Integer code;
    private String msg;
    private T data;

    public BaseResult(String msg) {
        this.msg = msg;
    }

    public BaseResult() {
    }


    public Integer getCode() {
        return code;
    }

    public BaseResult<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public BaseResult<T> setData(T data) {
        this.data = data;
        return this;
    }
}
