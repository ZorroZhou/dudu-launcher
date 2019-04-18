package com.wow.musicapi.provider.baidu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.config.Constants;
import com.wow.musicapi.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("SpellCheckingInspection")
class BaiduSearchMusicRequest extends BaseRequest<List<? extends Song>> {
    private final String mKeyword;
    private static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private final int mPage;

    public BaiduSearchMusicRequest(String keyword, int page) {
        mKeyword = keyword;
        mPage = page;
    }

    @Override
    protected Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://musicapi.qianqian.com/v1/restserver/ting").newBuilder();
        urlBuilder.addQueryParameter("method", "baidu.ting.search.merge");
        urlBuilder.addQueryParameter("query", mKeyword);
        urlBuilder.addQueryParameter("format", "json");
        urlBuilder.addQueryParameter("page_no", String.valueOf(mPage));
        urlBuilder.addQueryParameter("page_size", String.valueOf(PAGE_SIZE));
        requestBuilder.url(urlBuilder.build());
        requestBuilder.addHeader("User-Agent", "android_6.1.0.3;baiduyinyue");
        requestBuilder.get();
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected List<BaiduSong> parseResult(Response response) throws IOException {
        String body = responseBodyToString(response);
        JSONObject responseJson = JSONObject.parseObject(body);
        JSONObject result = responseJson.getJSONObject("result");
        JSONObject songInfo = result.getJSONObject("song_info");
        JSONArray songArray = songInfo.getJSONArray("song_list");
        List<BaiduSong> songs = new ArrayList<>();
        if (songArray != null) {
            songs = songArray.toJavaList(BaiduSong.class);
        }
        return songs;
    }
}
