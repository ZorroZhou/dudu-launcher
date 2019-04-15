package com.wow.musicapi.provider.qq;

import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.MusicLink;

/**
 * Created by haohua on 2018/2/9.
 */
class QQSongLink extends BaseBean implements MusicLink {
    public String url;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public String getSongId() {
        return null;
    }

    @Override
    public long getBitRate() {
        return 0;
    }

    @Override
    public String getMd5() {
        return null;
    }
}
