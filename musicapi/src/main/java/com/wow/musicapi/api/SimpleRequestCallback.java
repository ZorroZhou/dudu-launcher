package com.wow.musicapi.api;

import java.io.IOException;

/**
 * RequestCallback简化的基类
 *
 * @param <TResult>
 */
public abstract class SimpleRequestCallback<TResult> implements RequestCallback<TResult> {
    /**
     * onFailure简化为默认打印错误
     *
     * @param e
     */
    @Override
    public void onFailure(IOException e) {
        e.printStackTrace();
    }
}
