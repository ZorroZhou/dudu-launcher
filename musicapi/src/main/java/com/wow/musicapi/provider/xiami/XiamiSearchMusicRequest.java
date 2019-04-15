package com.wow.musicapi.provider.xiami;

import com.alibaba.fastjson.JSONObject;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.config.Constants;
import com.wow.musicapi.model.Song;

import java.io.IOException;
import java.util.List;

/**
 * Create by huchunyue on 2018/2/24
 */

@SuppressWarnings("SpellCheckingInspection")
public class XiamiSearchMusicRequest extends BaseRequest<List<? extends Song>> {
    private final String mKeyword;
    private static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private final int mPage;

    public XiamiSearchMusicRequest(String keyword, int page) {
        mKeyword = keyword;
        mPage = page;
    }

    @Override
    protected Request buildRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.xiami.com/web").newBuilder();
        urlBuilder.addQueryParameter("key", mKeyword);
        urlBuilder.addQueryParameter("v", "2.0");
        urlBuilder.addQueryParameter("app_key", "1");
        urlBuilder.addQueryParameter("r", "search/songs");
        urlBuilder.addQueryParameter("page", String.valueOf(mPage));
        urlBuilder.addQueryParameter("limit", String.valueOf(PAGE_SIZE));
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(urlBuilder.build());
        requestBuilder.addHeader("User-Agent", XiamiMusicApi.USER_AGENT);
        requestBuilder.addHeader("Referer", "http://m.xiami.com");
        requestBuilder.get();
        final Request request = requestBuilder.build();
        return request;
    }

    protected List<XiamiSong> parseResult(Response response) throws IOException {
        String data = response.body().string();
        JSONObject json = JSONObject.parseObject(data);
        List<XiamiSong> list = json.getJSONObject("data").getJSONArray("songs")
                .toJavaList(XiamiSong.class);
        return list;
    }
}
