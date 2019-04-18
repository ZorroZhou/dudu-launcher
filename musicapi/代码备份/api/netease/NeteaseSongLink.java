package com.wow.musicapi.provider.netease;

import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.MusicLink;

@SuppressWarnings("SpellCheckingInspection")
class NeteaseSongLink extends BaseBean implements MusicLink {

    @SerializedName("url")
    public String url;

    @SerializedName("id")
    public long songId;

    @SerializedName("size")
    public long size;

    @SerializedName("br")
    public long bitrate;

    @SerializedName("md5")
    public String md5;
    @SerializedName("type")

    public String type;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public String getSongId() {
        return String.valueOf(songId);
    }

    @Override
    public long getBitRate() {
        return bitrate;
    }

    @Override
    public String getMd5() {
        return md5;
    }
}
