package com.wow.musicapi.provider.xiami;

import com.alibaba.fastjson.JSONObject;
import com.wow.musicapi.api.BaseRequest;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("SpellCheckingInspection")
public class XiamiGetAlbumInfoRequest extends BaseRequest<XiamiAlbum> {
    private final String mAlbumId;

    public XiamiGetAlbumInfoRequest(String albumId) {
        mAlbumId = albumId;
    }

    @Override
    protected Request buildRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.xiami.com/web").newBuilder();
        urlBuilder.addQueryParameter("id", mAlbumId);
        urlBuilder.addQueryParameter("v", "2.0");
        urlBuilder.addQueryParameter("app_key", "1");
        urlBuilder.addQueryParameter("r", "album/detail");
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(urlBuilder.build());
        requestBuilder.addHeader("User-Agent", XiamiMusicApi.USER_AGENT);
        requestBuilder.addHeader("Referer", "http://m.xiami.com");
        requestBuilder.get();
        final Request request = requestBuilder.build();
        return request;
    }

    protected XiamiAlbum parseResult(Response response) throws IOException {
        String data = response.body().string();
        JSONObject json = JSONObject.parseObject(data);
        XiamiAlbum album = json.getJSONObject("data").toJavaObject(XiamiAlbum.class);
        return album;
    }
}
