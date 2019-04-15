package com.wow.musicapi.api;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 为统一http请求和解析返回数据而封装的基类
 *
 * @param <TResult>
 */
public abstract class BaseRequest<TResult> {
    /**
     * 异步请求方式（推荐使用此种方式）
     *
     * @param callback
     */
    public final void requestAsync(final RequestCallback<TResult> callback) {
        Request request = buildRequest();
        HttpEngine.requestAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final TResult parsedResult = parseResult(response);
                callback.onSuccess(parsedResult);
            }
        }, useProxy());
    }

    /**
     * 同步请求方式
     *
     * @return
     * @throws IOException
     */
    public final TResult requestSync() throws IOException {
        Request request = buildRequest();
        Response response = HttpEngine.requestSync(request, useProxy());
        TResult parsed = parseResult(response);
        return parsed;
    }

    protected static String responseBodyToString(Response response) throws IOException {
        final String body = response.body().string();
        return body;
    }

    /**
     * 需要构造的请求
     *
     * @return
     */
    protected abstract Request buildRequest();

    /**
     * 解析response
     *
     * @param response
     * @return
     * @throws IOException
     */
    protected abstract TResult parseResult(Response response) throws IOException;

    /**
     * 是否使用代理
     *
     * @return
     */
    protected boolean useProxy() {
        return false;
    }
}
