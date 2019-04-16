package com.wow.musicapi.provider.netease;

import com.alibaba.fastjson.JSONObject;
import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.config.Constants;
import com.wow.musicapi.util.CommonUtils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("SpellCheckingInspection")
class NeteaseGetMusicLyricRequest extends BaseRequest<NeteaseLyric> {
    private final String songId;

    public NeteaseGetMusicLyricRequest(String songId) {
        this.songId = songId;
    }

    @Override
    protected Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(HttpUrl.parse("http://music.163.com/api/linux/forward"));
        requestBuilder.addHeader(Constants.REFERER, "http://music.163.com/");
        requestBuilder.addHeader("X-REAL-IP", CommonUtils.generateChinaRandomIP());
        JSONObject json = new JSONObject();
        json.put("method", "GET");
        json.put("url", "http://music.163.com/api/song/lyric");
        JSONObject params = new JSONObject();
        params.put("id", this.songId);
        params.put("lv", 1);
        json.put("params", params);
        String encrypted = NeteaseMusicApi.encrypt(json);
        FormBody body = new FormBody.Builder().add("eparams", encrypted).build();
        requestBuilder.post(body);
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected NeteaseLyric parseResult(Response response) throws IOException {
        final String json = responseBodyToString(response);
        JSONObject responseJson = JSONObject.parseObject(json);
        JSONObject lrcJson = responseJson.getJSONObject("lrc");
        NeteaseLyric lrc = lrcJson.toJavaObject(NeteaseLyric.class);
        return lrc;
    }
}
