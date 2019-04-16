package com.wow.musicapi.provider.kugou;

import com.alibaba.fastjson.JSONObject;
import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.config.Constants;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("SpellCheckingInspection")
class KugouGetSongDetailRequest extends BaseRequest<KugouSong> {
    private final String mMusicHash;

    public KugouGetSongDetailRequest(String musicHash) {
        mMusicHash = musicHash;
    }

    @Override
    protected Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://m.kugou.com/app/i/getSongInfo.php").newBuilder();
        urlBuilder.addQueryParameter("cmd", "playInfo");
        urlBuilder.addQueryParameter("hash", mMusicHash);
        requestBuilder.url(urlBuilder.build());
        requestBuilder.addHeader(Constants.REFERER, "http://m.kugou.com/play/info/" + mMusicHash);
        requestBuilder.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) " +
                "AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        requestBuilder.get();
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected KugouSong parseResult(Response response) throws IOException {
        String data = responseBodyToString(response);
        JSONObject responseJson = JSONObject.parseObject(data);
        KugouSong detail = responseJson.toJavaObject(KugouSong.class);
        return detail;
    }
}
