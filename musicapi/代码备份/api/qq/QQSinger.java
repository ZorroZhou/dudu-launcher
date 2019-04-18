package com.wow.musicapi.provider.qq;

import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;

/**
 * Created by haohua on 2018/2/9.
 */
class QQSinger extends BaseBean implements Artist {
    @SerializedName("name")
    public String name;

    @SerializedName("mid")
    public String mid;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return mid;
    }
}
