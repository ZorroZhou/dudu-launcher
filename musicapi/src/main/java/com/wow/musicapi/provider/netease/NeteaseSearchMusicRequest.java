package com.wow.musicapi.provider.netease;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.config.Constants;
import com.wow.musicapi.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haohua on 2018/2/11.
 */
@SuppressWarnings("SpellCheckingInspection")
class NeteaseSearchMusicRequest extends BaseRequest<List<? extends Song>> {
    private final String mKeyword;
    private static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private final int mPage;

    public NeteaseSearchMusicRequest(String keyword, int page) {
        mKeyword = keyword;
        mPage = page;
    }

    @Override
    protected Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(HttpUrl.parse("http://music.163.com/api/linux/forward"));
        requestBuilder.addHeader(Constants.REFERER, "http://music.163.com/");
        JSONObject json = new JSONObject();
        json.put("method", "POST");
        json.put("url", "http://music.163.com/api/cloudsearch/pc");
        JSONObject params = new JSONObject();
        params.put("s", mKeyword);
        params.put("type", 1);
        params.put("offset", mPage * PAGE_SIZE);
        params.put("limit", PAGE_SIZE);
        json.put("params", params);
        String encrypted = NeteaseMusicApi.encrypt(json);
        FormBody body = new FormBody.Builder().add("eparams", encrypted).build();
        requestBuilder.post(body);
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected List<NeteaseSong> parseResult(Response response) throws IOException {
        String body = responseBodyToString(response);
        JSONObject responseJson = JSONObject.parseObject(body);
        JSONObject result = responseJson.getJSONObject("result");
        List<NeteaseSong> songs = new ArrayList<>();
        if (result != null) {
            long songCount = result.getLongValue("songCount");
            JSONArray songArray = result.getJSONArray("songs");
            if (songCount != 0 && songArray != null) {
                songs = songArray.toJavaList(NeteaseSong.class);
            }
        }
        return songs;
    }
}
