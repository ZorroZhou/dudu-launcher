package com.wow.musicapi.provider.netease;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import com.wow.musicapi.api.BaseRequest;
import com.wow.musicapi.config.Constants;
import com.wow.musicapi.model.MusicLink;
import com.wow.musicapi.util.CommonUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by haohua on 2018/2/11.
 */
@SuppressWarnings("SpellCheckingInspection")
class NeteaseGetMusicDetailsRequest extends BaseRequest<List<NeteaseSong>> {
    private final String[] mMusicIds;

    public NeteaseGetMusicDetailsRequest(String... musicIds) {
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
        json.put("url", "http://music.163.com/api/song/detail");
        JSONObject params = new JSONObject();
        JSONArray musicIdArray = new JSONArray();
        for (String musicId : mMusicIds) {
            musicIdArray.add(Long.parseLong(musicId));
        }
        params.put("ids", musicIdArray);
        json.put("params", params);
        String encrypted = NeteaseMusicApi.encrypt(json);
        FormBody body = new FormBody.Builder().add("eparams", encrypted).build();
        requestBuilder.post(body);
        final Request request = requestBuilder.build();
        return request;
    }

    @Override
    protected List<NeteaseSong> parseResult(Response response) throws IOException {
        final String json = responseBodyToString(response);
        JSONObject responseJson = JSONObject.parseObject(json);
        JSONArray jsonData = responseJson.getJSONArray("songs");
        List<NeteaseSong> songs = jsonData.toJavaList(NeteaseSong.class);
        return songs;
    }
}
