package com.wow.musicapi.provider.kugou;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.config.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("SpellCheckingInspection")
class KugouSearchMusicRequest extends BaseRequest<List<KugouSong>> {
    private final String mKeyword;
    private static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private final int mPage;

    public KugouSearchMusicRequest(String keyword, int page) {
        mKeyword = keyword;
        mPage = page;
    }

    @Override
    protected Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://mobilecdn.kugou.com/api/v3/search/song").newBuilder();
        urlBuilder.addQueryParameter("keyword", mKeyword);
        urlBuilder.addQueryParameter("format", "json");
        urlBuilder.addQueryParameter("page", String.valueOf(mPage));
        urlBuilder.addQueryParameter("pagesize", String.valueOf(PAGE_SIZE));
        requestBuilder.url(urlBuilder.build());
        requestBuilder.addHeader(Constants.REFERER, "http://m.kugou.com/v2/static/html/search.html");
        requestBuilder.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) " +
                "AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        requestBuilder.get();
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected List<KugouSong> parseResult(Response response) throws IOException {
        String data = responseBodyToString(response);
        JSONObject responseJson = JSONObject.parseObject(data);
        JSONArray songArray = responseJson.getJSONObject("data").getJSONArray("info");
        List<KugouSong> songs = new ArrayList<>();
        if (songArray != null) {
            songs = songArray.toJavaList(KugouSong.class);
        }
        return songs;
    }
}
