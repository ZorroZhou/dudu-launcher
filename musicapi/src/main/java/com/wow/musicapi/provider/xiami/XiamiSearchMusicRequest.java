package com.wow.musicapi.provider.xiami;

import com.google.gson.reflect.TypeToken;
import com.wow.frame.SFrame;
import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.config.Constants;
import com.wow.musicapi.model.Song;

import org.xutils.http.RequestParams;

import java.util.List;
import java.util.Map;

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
    protected RequestParams buildRequest() {
        RequestParams requestParams = new RequestParams("http://api.xiami.com/web");
        requestParams.addBodyParameter("key", mKeyword);
        requestParams.addBodyParameter("v", "2.0");
        requestParams.addBodyParameter("app_key", "1");
        requestParams.addBodyParameter("r", "search/songs");
        requestParams.addBodyParameter("page", String.valueOf(mPage));
        requestParams.addBodyParameter("limit", String.valueOf(PAGE_SIZE));

        requestParams.addHeader("User-Agent", XiamiMusicApi.USER_AGENT);
        requestParams.addHeader("Referer", "http://m.xiami.com");
        return requestParams;
    }

    protected List<XiamiSong> parseResult(String data) {
        System.out.println(data);
        Map json = SFrame.getGson().fromJson(data, Map.class);

        List<XiamiSong> list = SFrame.getGson().fromJson(SFrame.getGson().toJson(((Map) json.get("data")).get("songs")), new TypeToken<List<XiamiSong>>() {
        }.getType());
        return list;
    }
}
