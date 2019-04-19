package com.wow.musicapi.provider.kugou;

import com.alibaba.fastjson.JSONObject;

import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.config.Constants;

import org.xutils.http.RequestParams;

@SuppressWarnings("SpellCheckingInspection")
class KugouGetSongDetailRequest extends BaseRequest<KugouSong> {
    private final String mMusicHash;

    public KugouGetSongDetailRequest(String musicHash) {
        mMusicHash = musicHash;
    }

    @Override
    protected RequestParams buildRequest() {
        RequestParams requestParams = new RequestParams("http://m.kugou.com/app/i/getSongInfo.php");
        requestParams.addQueryStringParameter("cmd", "playInfo");
        requestParams.addQueryStringParameter("hash", mMusicHash);
        requestParams.addHeader(Constants.REFERER, "http://m.kugou.com/play/info/" + mMusicHash);
        requestParams.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) " +
                "AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        return requestParams;
    }

    @Override
    protected KugouSong parseResult(String data) {
        JSONObject responseJson = JSONObject.parseObject(data);
        KugouSong detail = responseJson.toJavaObject(KugouSong.class);
        return detail;
    }
}
