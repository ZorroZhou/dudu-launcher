package com.wow.carlauncher.repertory.server;

public interface CommonCallback<T> {
    void callback(boolean success, String msg, T t);
}
