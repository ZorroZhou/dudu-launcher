package com.wow.musicapi.provider.kuwo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.config.Constants;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by haohua on 2018/2/23.
 */
@SuppressWarnings("SpellCheckingInspection")
class KuwoGetMusicLinkRequest extends BaseRequest<KuwoSongLink> {
    private final String mMusicRid;

    public KuwoGetMusicLinkRequest(String musicRid) {
        mMusicRid = musicRid;
    }

    @Override
    protected Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://player.kuwo.cn/webmusic/st/getNewMuiseByRid").newBuilder();
        urlBuilder.addQueryParameter("rid", mMusicRid);
        requestBuilder.url(urlBuilder.build());
        requestBuilder.addHeader(Constants.REFERER, "http://player.kuwo.cn/webmusic/play");
        requestBuilder.get();
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected KuwoSongLink parseResult(Response response) throws IOException {
        String body = response.body().string();
        body = body.replaceAll("&", "&amp;");
        XmlMapper mapper = new XmlMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KuwoSongLink link = mapper.readValue(body, KuwoSongLink.class);
        return link;
    }
}
