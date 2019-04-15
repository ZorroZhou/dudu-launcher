package com.wow.musicapi.provider.yiting;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
 * Created by haohua on 2018/2/23.
 */
@SuppressWarnings("SpellCheckingInspection")
class YitingSearchMusicRequest extends BaseRequest<List<? extends Song>> {
    private final String mKeyword;
    private static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private final int mPage;

    public YitingSearchMusicRequest(String keyword, int page) {
        mKeyword = keyword;
        mPage = page;
    }

    @Override
    protected Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://so.1ting.com/song/json").newBuilder();
        urlBuilder.addQueryParameter("q", mKeyword);
        urlBuilder.addQueryParameter("page", String.valueOf(mPage));
        urlBuilder.addQueryParameter("size", String.valueOf(PAGE_SIZE));
        requestBuilder.url(urlBuilder.build());
        requestBuilder.addHeader(Constants.REFERER, "http://h5.1ting.com/");
        requestBuilder.get();
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected List<YitingSong> parseResult(Response response) throws IOException {
        String body = response.body().string();
        JSONObject responseJson = JSONObject.parseObject(body);
        JSONArray songArray = responseJson.getJSONArray("results");
        List<YitingSong> songs = new ArrayList<>();
        if (songArray != null) {
            songs = songArray.toJavaList(YitingSong.class);
        }
        return songs;
    }
}
