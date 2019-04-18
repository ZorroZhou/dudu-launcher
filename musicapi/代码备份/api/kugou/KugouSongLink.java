package com.wow.musicapi.provider.kugou;

import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.MusicLink;

@SuppressWarnings("SpellCheckingInspection")
class KugouSongLink extends BaseBean implements MusicLink {
    public String url;

    public long bitRate;

    public String extName;

    public long size;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getType() {
        return extName;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public String getSongId() {
        return null;
    }

    @Override
    public long getBitRate() {
        return bitRate;
    }

    @Override
    public String getMd5() {
        return null;
    }
}
