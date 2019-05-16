package com.wow.carlauncher.repertory.server;

public interface CommonCallback<T> {
    void callback(int code, String msg, T t);
}
