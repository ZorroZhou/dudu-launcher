package com.wow.musicapi.provider.netease;

import com.alibaba.fastjson.annotation.JSONField;
import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.Lyric;

@SuppressWarnings("SpellCheckingInspection")
class NeteaseLyric extends BaseBean implements Lyric {
    @JSONField(name = "lyric")
    public String lyric;

    @Override
    public String getLyric() {
        return lyric;
    }

    @Override
    public String getLyricUrl() {
        return null;
    }
}
