package com.wow.musicapi.provider.migu;

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

@SuppressWarnings("SpellCheckingInspection")
class MiguSearchMusicRequest extends BaseRequest<List<? extends Song>> {
    private final String mKeyword;
    private static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private final int mPage;

    public MiguSearchMusicRequest(String keyword, int page) {
        mKeyword = keyword;
        mPage = page;
    }

    @Override
    protected Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://m.10086.cn/migu/remoting/scr_search_tag").newBuilder();
        urlBuilder.addQueryParameter("keyword", mKeyword);
        urlBuilder.addQueryParameter("type", "2");
        urlBuilder.addQueryParameter("pgc", String.valueOf(mPage));
        urlBuilder.addQueryParameter("rows", String.valueOf(PAGE_SIZE));
        requestBuilder.url(urlBuilder.build());
        requestBuilder.addHeader("Referrer", "http://m.10086.cn");
        requestBuilder.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) " +
                "AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        requestBuilder.get();
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected List<MiguSong> parseResult(Response response) throws IOException {
        String data = responseBodyToString(response);
        JSONObject responseJson = JSONObject.parseObject(data);
        JSONArray songArray = responseJson.getJSONArray("musics");
        List<MiguSong> songs = new ArrayList<>();
        if (songArray != null) {
            songs = songArray.toJavaList(MiguSong.class);
        }
        return songs;
    }
}
