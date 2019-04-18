package com.wow.musicapi.provider.baidu;

import com.alibaba.fastjson.annotation.JSONField;
import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;

/**
 * Created by haohua on 2018/2/23.
 */

@SuppressWarnings("SpellCheckingInspection")
class BaiduArtist extends BaseBean implements Artist {
    @SerializedName("name")
    public String name;

    @SerializedName("id")
    public String id;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }
}
