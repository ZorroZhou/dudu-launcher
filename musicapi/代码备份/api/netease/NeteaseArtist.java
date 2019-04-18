package com.wow.musicapi.provider.netease;

import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;

/**
 * Created by haohua on 2018/2/12.
 */

@SuppressWarnings("SpellCheckingInspection")
class NeteaseArtist extends BaseBean implements Artist {
    @SerializedName("name")
    public String name;

    @SerializedName("id")
    public long id;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }
}
