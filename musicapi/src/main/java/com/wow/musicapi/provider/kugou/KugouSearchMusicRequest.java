package com.wow.musicapi.provider.kugou;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.config.Constants;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

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
    protected RequestParams buildRequest() {
        RequestParams requestParams = new RequestParams("http://mobilecdn.kugou.com/api/v3/search/song");
        requestParams.addQueryStringParameter("keyword", mKeyword);
        requestParams.addQueryStringParameter("format", "json");
        requestParams.addQueryStringParameter("page", String.valueOf(mPage));
        requestParams.addQueryStringParameter("pagesize", String.valueOf(PAGE_SIZE));
        requestParams.addHeader(Constants.REFERER, "http://m.kugou.com/v2/static/html/search.html");
        requestParams.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) " +
                "AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        return requestParams;
    }

    @Override
    protected List<KugouSong> parseResult(String data) {
        JSONObject responseJson = JSONObject.parseObject(data);
        JSONArray songArray = responseJson.getJSONObject("data").getJSONArray("info");
        List<KugouSong> songs = new ArrayList<>();
        if (songArray != null) {
            songs = songArray.toJavaList(KugouSong.class);
        }
        return songs;
    }
}
