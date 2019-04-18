package com.wow.musicapi.provider.weibo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.annimon.stream.Stream;
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
class WeiboSearchMusicRequest extends BaseRequest<List<? extends Song>> {
    private final String mKeyword;
    private static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private final int mPage;

    public WeiboSearchMusicRequest(String keyword, int page) {
        mKeyword = keyword;
        mPage = page;
    }

    @Override
    protected Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://ting.weibo.com/open/search/searchmusicbykey?").newBuilder();
        urlBuilder.addQueryParameter("key", mKeyword);
        urlBuilder.addQueryParameter("source", "weibo_video_new");
        requestBuilder.url(urlBuilder.build());
        requestBuilder.get();
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected List<WeiboSong> parseResult(Response response) throws IOException {
        JSONObject responseJson = JSONObject.parseObject(response.body().string());
        JSONArray songArray = responseJson.getJSONArray("data");
        List<WeiboSong> songs = new ArrayList<>();
        if (songArray != null) {
            songs = songArray.toJavaList(WeiboSong.class);
            songs = Stream.of(songs).skip(mPage * PAGE_SIZE).limit(PAGE_SIZE).toList();
        }
        return songs;
    }
}
