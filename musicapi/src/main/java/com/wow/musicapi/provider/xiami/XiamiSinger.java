package com.wow.musicapi.provider.xiami;

import com.google.gson.annotations.SerializedName;
import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;

@SuppressWarnings("SpellCheckingInspection")
class XiamiSinger extends BaseBean implements Artist {
    @SerializedName("artist_name")
    public String artistName;

    @SerializedName("artist_id")
    public String artistId;

    @Override
    public String getName() {
        return artistName;
    }

    @Override
    public String getId() {
        return artistId;
    }
}
