package com.wow.carlauncher.ex.manage.okHttp;

import okhttp3.Callback;

public interface ProgressResponseListener extends Callback {
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}