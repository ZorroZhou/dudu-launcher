package com.wow.musicapi.provider.weibo;

import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;

@SuppressWarnings("SpellCheckingInspection")
class WeiboArtist extends BaseBean implements Artist {
    public String name;

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
