package com.wow.musicapi.provider.baidu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.util.TextUtils;

import org.xutils.http.RequestParams;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
class BaiduGetSongDetailsRequest extends BaseRequest<List<BaiduSong>> {
    private final String[] mMusicIds;

    public BaiduGetSongDetailsRequest(String... musicIds) {
        mMusicIds = musicIds;
    }

    @Override
    protected RequestParams buildRequest() {
        RequestParams requestParams = new RequestParams("http://music.baidu.com/data/music/links");
        String songIds = TextUtils.join(",", Arrays.asList(mMusicIds));
        requestParams.addQueryStringParameter("songIds", songIds);
        requestParams.addHeader("Referrer", "music.baidu.com/song/" + songIds);
        return requestParams;
    }

    @Override
    protected List<BaiduSong> parseResult(String response) {
        JSONObject responseJson = JSONObject.parseObject(response);
        JSONArray jsonData = responseJson.getJSONObject("data").getJSONArray("songList");
        List<BaiduSong> details = jsonData.toJavaList(BaiduSong.class);
        return details;
    }
}
