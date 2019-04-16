package com.wow.musicapi.provider.baidu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.util.TextUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("SpellCheckingInspection")
class BaiduGetSongDetailsRequest extends BaseRequest<List<BaiduSong>> {
    private final String[] mMusicIds;

    public BaiduGetSongDetailsRequest(String... musicIds) {
        mMusicIds = musicIds;
    }

    @Override
    protected Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        String songIds = TextUtils.join(",", Arrays.asList(mMusicIds));
        requestBuilder.url(HttpUrl.parse("http://music.baidu.com/data/music/links").newBuilder()
                .addQueryParameter("songIds", songIds).build());
        requestBuilder.addHeader("Referrer", "music.baidu.com/song/" + songIds);
        requestBuilder.get();
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected List<BaiduSong> parseResult(Response response) throws IOException {
        String data = responseBodyToString(response);
        JSONObject responseJson = JSONObject.parseObject(data);
        JSONArray jsonData = responseJson.getJSONObject("data").getJSONArray("songList");
        List<BaiduSong> details = jsonData.toJavaList(BaiduSong.class);
        return details;
    }
}
