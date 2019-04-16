package com.wow.musicapi.provider.netease;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.config.Constants;
import com.wow.musicapi.model.MusicLink;
import com.wow.musicapi.util.CommonUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by haohua on 2018/2/11.
 */
@SuppressWarnings("SpellCheckingInspection")
class NeteaseGetMusicLinksRequest extends BaseRequest<List<? extends MusicLink>> {
    private final String[] mMusicIds;

    public NeteaseGetMusicLinksRequest(String... musicIds) {
        mMusicIds = musicIds;
    }

    @Override
    protected Request buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(HttpUrl.parse("http://music.163.com/api/linux/forward"));
        requestBuilder.addHeader(Constants.REFERER, "http://music.163.com/");
        requestBuilder.addHeader("X-REAL-IP", CommonUtils.generateChinaRandomIP());
        JSONObject json = new JSONObject();
        json.put("method", "POST");
        json.put("url", "http://music.163.com/api/song/enhance/player/url");
        JSONObject params = new JSONObject();
        JSONArray musicIdArray = new JSONArray();
        for (String musicId : mMusicIds) {
            musicIdArray.add(Long.parseLong(musicId));
        }
        params.put("br", 320000);
        params.put("ids", musicIdArray);
        json.put("params", params);
        String encrypted = NeteaseMusicApi.encrypt(json);
        FormBody body = new FormBody.Builder().add("eparams", encrypted).build();
        requestBuilder.post(body);
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected List<NeteaseSongLink> parseResult(Response response) throws IOException {
        JSONObject responseJson = JSONObject.parseObject(response.body().string());
        JSONArray jsonData = responseJson.getJSONArray("data");
        List<NeteaseSongLink> songLinks = jsonData.toJavaList(NeteaseSongLink.class);
        return songLinks;
    }
}
