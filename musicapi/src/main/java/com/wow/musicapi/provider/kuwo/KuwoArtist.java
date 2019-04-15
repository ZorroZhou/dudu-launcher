package com.wow.musicapi.provider.kuwo;

import com.alibaba.fastjson.annotation.JSONField;
import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;

/**
 * Created by haohua on 2018/2/23.
 */

@SuppressWarnings("SpellCheckingInspection")
class KuwoArtist extends BaseBean implements Artist {
    @JSONField(name = "name")
    public String name;

    @JSONField(name = "id")
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
