package com.wow.musicapi.api;

import java.io.IOException;

/**
 * Created by haohua on 2018/2/11.
 */
public interface RequestCallback<TResult> {
    void onFailure(IOException e);

    void onSuccess(TResult result);
}
