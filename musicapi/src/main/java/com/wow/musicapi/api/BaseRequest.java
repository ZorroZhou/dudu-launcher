package com.wow.musicapi.api;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;

/**
 * 为统一http请求和解析返回数据而封装的基类
 *
 * @param <TResult>
 */
public abstract class BaseRequest<TResult> {
    /**
     * 同步请求方式
     *
     * @return
     * @throws IOException
     */
    public final TResult requestSync() throws Throwable {
        RequestParams request = buildRequest();
        String response = x.http().getSync(request, String.class);
        TResult parsed = parseResult(response);
        return parsed;
    }

    /**
     * 需要构造的请求
     *
     * @return
     */
    protected abstract RequestParams buildRequest();

    /**
     * 解析response
     *
     * @param response
     * @return
     * @throws IOException
     */
    protected abstract TResult parseResult(String response) throws IOException;

    /**
     * 是否使用代理
     *
     * @return
     */
    protected boolean useProxy() {
        return false;
    }
}
