package com.wow.musicapi.provider.xiami;

import com.alibaba.fastjson.JSONObject;
import com.wow.frame.SFrame;
import com.wow.frame.repertory.dbTool.beanTool.BeanUtil;
import com.wow.frame.util.JsonConvertUtil;
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
        return requestParams;
    }

    protected XiamiAlbum parseResult(String data) {
        JSONObject json = JSONObject.parseObject(data);
        XiamiAlbum album = json.getJSONObject("data").toJavaObject(XiamiAlbum.class);
        return album;
    }
}
