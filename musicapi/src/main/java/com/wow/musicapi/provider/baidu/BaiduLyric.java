package com.wow.musicapi.provider.baidu;

import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.Lyric;

@SuppressWarnings("SpellCheckingInspection")
class BaiduLyric extends BaseBean implements Lyric {
    public String lyricUrl;

    @Override
    public String getLyric() {
        return null;
    }

    @Override
    public String getLyricUrl() {
        return lyricUrl;
    }
}
