package com.wow.musicapi.provider.baidu;

import com.alibaba.fastjson.JSONObject;
import com.wow.musicapi.api.BaseRequest;

import org.xutils.http.RequestParams;

import java.util.List;


@SuppressWarnings("SpellCheckingInspection")
class BaiduGetAlbumInfoRequest extends BaseRequest<BaiduAlbum> {
    private final String mAlbumId;

    public BaiduGetAlbumInfoRequest(String albumId) {
        mAlbumId = albumId;
    }

    @Override
    protected RequestParams buildRequest() {
        RequestParams requestParams = new RequestParams("http://musicapi.qianqian.com/v1/restserver/ting");
        requestParams.addQueryStringParameter("method", "baidu.ting.album.getAlbumInfo");
        requestParams.addQueryStringParameter("album_id", mAlbumId);
        requestParams.addQueryStringParameter("format", "json");
        requestParams.addHeader("User-Agent", "android_6.1.0.3;baiduyinyue");
        return requestParams;
    }

    @Override
    protected BaiduAlbum parseResult(String body) {
        JSONObject responseJson = JSONObject.parseObject(body);
        BaiduAlbum album = responseJson.getJSONObject("albumInfo").toJavaObject(BaiduAlbum.class);
        List<BaiduSong> songs = responseJson.getJSONArray("songlist").toJavaList(BaiduSong.class);
        album.setSongs(songs);
        return album;
    }
}
