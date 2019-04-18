package com.wow.musicapi.provider.migu;

import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;

@SuppressWarnings("SpellCheckingInspection")
class MiguArtist extends BaseBean implements Artist {
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
