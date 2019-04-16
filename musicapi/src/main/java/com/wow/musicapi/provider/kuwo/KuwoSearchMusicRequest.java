package com.wow.musicapi.provider.kuwo;

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

/**
 * Created by haohua on 2018/2/23.
 */
@SuppressWarnings("SpellCheckingInspection")
class KuwoSearchMusicRequest extends BaseRequest<List<KuwoSong>> {
    private final String mKeyword;
    private static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private final int mPage;

    public KuwoSearchMusicRequest(String keyword, int page) {
        mKeyword = keyword;
        mPage = page;
    }

    @Override
    protected Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://search.kuwo.cn/r.s").newBuilder();
        urlBuilder.addQueryParameter("all", mKeyword);
        urlBuilder.addQueryParameter("ft", "music");
        urlBuilder.addQueryParameter("itemset", "web_2013");
        urlBuilder.addQueryParameter("pn", String.valueOf(mPage));
        urlBuilder.addQueryParameter("rn", String.valueOf(PAGE_SIZE));
        urlBuilder.addQueryParameter("rformat", "json");
        urlBuilder.addQueryParameter("encoding", "utf8");
        requestBuilder.url(urlBuilder.build());
        requestBuilder.addHeader(Constants.REFERER, "http://player.kuwo.cn/webmusic/play");
        requestBuilder.get();
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected List<KuwoSong> parseResult(Response response) throws IOException {
        String body = response.body().string();
        JSONObject responseJson = JSONObject.parseObject(body);
        JSONArray songArray = responseJson.getJSONArray("abslist");
        List<KuwoSong> songs = new ArrayList<>();
        if (songArray != null) {
            songs = songArray.toJavaList(KuwoSong.class);
        }
        return songs;
    }
}
