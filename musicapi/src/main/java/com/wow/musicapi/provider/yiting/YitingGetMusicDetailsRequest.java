package com.wow.musicapi.provider.yiting;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.model.MusicLink;
import com.wow.musicapi.util.TextUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by haohua on 2018/2/23.
 */
@SuppressWarnings("SpellCheckingInspection")
class YitingGetMusicDetailsRequest extends BaseRequest<List<YitingSong>> {
    private final String[] mMusicIds;

    public YitingGetMusicDetailsRequest(String... musicIds) {
        mMusicIds = musicIds;
    }

    @Override
    protected Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        String songIds = TextUtils.join(",", Arrays.asList(mMusicIds));
        requestBuilder.url(HttpUrl.parse("http://h5.1ting.com/touch/api/song").newBuilder()
                .addQueryParameter("ids", songIds).build());
        requestBuilder.addHeader("Referrer", "http://h5.1ting.com/#/song" + songIds);
        requestBuilder.get();
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected List<YitingSong> parseResult(Response response) throws IOException {
        String body = response.body().string();
        JSONArray jsonData = JSONObject.parseArray(body);
        List<YitingSong> songLinks = jsonData.toJavaList(YitingSong.class);
        return songLinks;
    }
}
