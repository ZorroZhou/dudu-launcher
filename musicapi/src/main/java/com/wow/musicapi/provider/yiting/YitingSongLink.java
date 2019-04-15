package com.wow.musicapi.provider.yiting;

import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.MusicLink;

/**
 * Created by haohua on 2018/2/23.
 */
@SuppressWarnings("SpellCheckingInspection")
class YitingSongLink extends BaseBean implements MusicLink {

    public String song_filepath;

    public String songId;

    @Override
    public String getUrl() {
        return "http://h5.1ting.com/file?url=" + song_filepath.replaceAll(".wma", ".mp3");
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
        return songId;
    }

    @Override
    public long getBitRate() {
        return 0;
    }

    @Override
    public String getMd5() {
        // TODO
        return null;
    }
}
