package com.wow.musicapi.provider.baidu;

import com.alibaba.fastjson.JSONObject;
import com.wow.musicapi.api.BaseRequest;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("SpellCheckingInspection")
class BaiduGetAlbumInfoRequest extends BaseRequest<BaiduAlbum> {
    private final String mAlbumId;

    public BaiduGetAlbumInfoRequest(String albumId) {
        mAlbumId = albumId;
    }

    @Override
    protected Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://musicapi.qianqian.com/v1/restserver/ting").newBuilder();
        urlBuilder.addQueryParameter("method", "baidu.ting.album.getAlbumInfo");
        urlBuilder.addQueryParameter("album_id", mAlbumId);
        urlBuilder.addQueryParameter("format", "json");
        requestBuilder.url(urlBuilder.build());
        requestBuilder.addHeader("User-Agent", "android_6.1.0.3;baiduyinyue");
        requestBuilder.get();
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected BaiduAlbum parseResult(Response response) throws IOException {
        String body = response.body().string();
        JSONObject responseJson = JSONObject.parseObject(body);
        BaiduAlbum album = responseJson.getJSONObject("albumInfo").toJavaObject(BaiduAlbum.class);
        List<BaiduSong> songs = responseJson.getJSONArray("songlist").toJavaList(BaiduSong.class);
        album.setSongs(songs);
        return album;
    }
}
