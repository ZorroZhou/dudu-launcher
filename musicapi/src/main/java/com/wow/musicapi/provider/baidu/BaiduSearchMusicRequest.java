package com.wow.musicapi.provider.baidu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.config.Constants;
import com.wow.musicapi.model.Song;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

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
    protected RequestParams buildRequest() {
        RequestParams requestParams = new RequestParams("http://musicapi.qianqian.com/v1/restserver/ting");
        requestParams.addQueryStringParameter("method", "baidu.ting.search.merge");
        requestParams.addQueryStringParameter("query", mKeyword);
        requestParams.addQueryStringParameter("format", "json");
        requestParams.addQueryStringParameter("page_no", String.valueOf(mPage));
        requestParams.addQueryStringParameter("page_size", String.valueOf(PAGE_SIZE));


//        urlBuilder.addQueryParameter("method", "baidu.ting.search.merge");
//        urlBuilder.addQueryParameter("query", mKeyword);
//        urlBuilder.addQueryParameter("format", "json");
//        urlBuilder.addQueryParameter("page_no", String.valueOf(mPage));
//        urlBuilder.addQueryParameter("page_size", String.valueOf(PAGE_SIZE));

        requestParams.addHeader("User-Agent", "android_6.1.0.3;baiduyinyue");
        return requestParams;
    }

    @Override
    protected List<BaiduSong> parseResult(String body) {
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
