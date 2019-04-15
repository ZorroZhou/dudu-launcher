package com.wow.musicapi.provider.netease;

import com.alibaba.fastjson.JSONObject;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import org.apache.commons.codec.binary.Base64;

import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.config.Constants;
import com.wow.musicapi.model.Album;
import com.wow.musicapi.util.AES;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 参考: https://github.com/metowolf/NeteaseCloudMusicApi/blob/master/weapi/NeteaseMusicAPI_mini.php
 */
class NeteaseGetAlbumInfoRequest extends BaseRequest<Album> {
    private final String mAlbumId;
    @SuppressWarnings("SpellCheckingInspection")
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like " +
            "Gecko) Chrome/35.0.1916.157 Safari/537.36";
    private static final String COOKIE = "os=pc; osver=Microsoft-Windows-10-Professional-build-10586-64bit; " +
            "appver=2.0.3.131777; channel=netease; __remember_me=true";

    private static final String encSecKey =
            "84ca47bca10bad09a6b04c5c927ef077d9b9f1e37098aa3eac6ea70eb59df0aa28b691b7e75e4f1f9831754919ea784c8f74fbfadf2898b0be17849fd656060162857830e241aba44991601f137624094c114ea8d17bce815b0cd4e5b8e2fbaba978c6d1d14dc3d1faf852bdd28818031ccdaaa13a6018e1024e2aae98844210";
    private static final String secretKey = "TA3YiYCfY2dDJQgg";
    private static final String NONCE = "0CoJUm6Qyw8W8jud";

    private static final byte[] iv = "0102030405060708".getBytes();

    public NeteaseGetAlbumInfoRequest(String albumId) {
        mAlbumId = albumId;
    }

    @Override
    protected Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(HttpUrl.parse("http://music.163.com/weapi/v1/album/" + mAlbumId + "?csrf_token="));
        requestBuilder.addHeader(Constants.REFERER, "http://music.163.com/");
        requestBuilder.addHeader("User-Agent", USER_AGENT);
        requestBuilder.addHeader("Cookie", COOKIE);
        JSONObject json = new JSONObject();
        //noinspection SpellCheckingInspection
        json.put("csrf_token", "");
        HashMap<String, String> encrypted = encrypt(json);
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> item : encrypted.entrySet()) {
            formBuilder.add(item.getKey(), item.getValue());
        }
        FormBody body = formBuilder.build();
        requestBuilder.post(body);
        final Request request = requestBuilder.build();
        return request;
    }

    private HashMap<String, String> encrypt(JSONObject json) {
        final String jsonStr = json.toJSONString();
        try {
            byte[] params1 = AES.encrypt(jsonStr, NONCE.getBytes(), iv);
            byte[] params2 = AES.encrypt(new String(Base64.encodeBase64(params1)), secretKey.getBytes(), iv);
            final String param2Base64 = new String(Base64.encodeBase64(params2));
            HashMap<String, String> result = new HashMap<>();
            result.put("params", param2Base64);
            result.put("encSecKey", encSecKey);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Album parseResult(Response response) throws IOException {
        JSONObject json = JSONObject.parseObject(response.body().string());
        //noinspection SpellCheckingInspection
        NeteaseAlbum album = json.getJSONObject("album").toJavaObject(NeteaseAlbum.class);
        List<NeteaseSong> songs = json.getJSONArray("songs").toJavaList(NeteaseSong.class);
        album.songs = songs;
        return album;
    }
}
