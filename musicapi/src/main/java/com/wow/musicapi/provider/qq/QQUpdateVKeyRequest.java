package com.wow.musicapi.provider.qq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.config.Constants;

import java.io.IOException;
import java.util.List;

/**
 * Created by haohua on 2018/2/11.
 */
class QQUpdateVKeyRequest extends BaseRequest<QQUpdateVKeyRequest.VKey> {

    public final static class VKey {
        public String key;
        public List<String> sips;
    }

    @Override
    protected Request buildRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://base.music.qq.com/fcgi-bin/fcg_musicexpress.fcg")
                .newBuilder();
        urlBuilder.addQueryParameter("json", "3");
        urlBuilder.addQueryParameter("guid", QQMusicApi.GUID);
        urlBuilder.addQueryParameter("format", "json");
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(urlBuilder.build());
        requestBuilder.addHeader("User-Agent", QQMusicApi.USER_AGENT);
        requestBuilder.addHeader(Constants.REFERER, "http://y.qq.com");
        requestBuilder.get();
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected VKey parseResult(Response response) throws IOException {
        JSONObject json = JSONObject.parseObject(response.body().string());
        JSONArray sip = json.getJSONArray("sip");
        String key = json.getString("key");
        VKey result = new VKey();
        result.key = key;
        result.sips = sip.toJavaList(String.class);
        return result;
    }
}
