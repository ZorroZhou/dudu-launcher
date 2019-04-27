package com.wow.carlauncher.repertory.web.mobile.packet;

public class Response<T> {
    private Integer code;
    private String msg;
    private T data;

    public Response(String msg) {
        this.msg = msg;
    }

    public Response() {
    }

    public Response<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Response<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Response<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return this.data;
    }

    public String toString() {
        return "BaseResult(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }
}
