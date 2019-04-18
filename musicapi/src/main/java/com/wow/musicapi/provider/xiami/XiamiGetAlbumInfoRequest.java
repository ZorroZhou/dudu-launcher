package com.wow.musicapi.provider.xiami;

import com.wow.frame.SFrame;
import com.wow.musicapi.api.BaseRequest;

import org.xutils.http.RequestParams;

import java.util.Map;

@SuppressWarnings("SpellCheckingInspection")
public class XiamiGetAlbumInfoRequest extends BaseRequest<XiamiAlbum> {
    private final String mAlbumId;

    public XiamiGetAlbumInfoRequest(String albumId) {
        mAlbumId = albumId;
    }

    @Override
    protected RequestParams buildRequest() {
        RequestParams requestParams = new RequestParams("http://api.xiami.com/web");
        requestParams.addBodyParameter("id", mAlbumId);
        requestParams.addBodyParameter("v", "2.0");
        requestParams.addBodyParameter("app_key", "1");
        requestParams.addBodyParameter("r", "album/detail");

        requestParams.addHeader("User-Agent", XiamiMusicApi.USER_AGENT);
        requestParams.addHeader("Referer", "http://m.xiami.com");

//        params.setRequestBody(new FileBody(file, "application/octet-stream"));
//        x.http().getSync()
//        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.xiami.com/web").newBuilder();
//        urlBuilder.addQueryParameter("id", mAlbumId);
//        urlBuilder.addQueryParameter("v", "2.0");
//        urlBuilder.addQueryParameter("app_key", "1");
//        urlBuilder.addQueryParameter("r", "album/detail");
//        Request.Builder requestBuilder = new Request.Builder();
//        requestBuilder.url(urlBuilder.build());
//        requestBuilder.addHeader("User-Agent", XiamiMusicApi.USER_AGENT);
//        requestBuilder.addHeader("Referer", "http://m.xiami.com");
//        requestBuilder.get();
//        final Request request = requestBuilder.build();
        return requestParams;
    }

    protected XiamiAlbum parseResult(String data) {
        Map json = SFrame.getGson().fromJson(data, Map.class);
        XiamiAlbum album = SFrame.getGson().fromJson(SFrame.getGson().toJson(json.get("data")), XiamiAlbum.class);
        return album;
    }
}
