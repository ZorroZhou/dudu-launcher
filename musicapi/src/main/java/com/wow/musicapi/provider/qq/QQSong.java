package com.wow.musicapi.provider.qq;

import com.alibaba.fastjson.annotation.JSONField;

import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.*;
import com.wow.musicapi.util.SongUtils;

import java.util.ArrayList;

/**
 * Created by haohua on 2018/2/9.
 */
class QQSong extends BaseBean implements Song {
    @JSONField(name = "songname")
    public String name;

    @JSONField(name = "songid")
    public String songId;

    @JSONField(name = "songmid")
    public String songMid;

    @JSONField(name = "singer")
    public ArrayList<QQSinger> singers;

    @JSONField(name = "albummid")
    public String albumMid;

    @JSONField(name = "albumname")
    public String albumName;

    @JSONField(name = "sizeogg")
    public long sizeogg;

    @JSONField(name = "sizeflac")
    public long sizeflac;

    @JSONField(name = "sizeape")
    public long sizeape;

    @JSONField(name = "size320")
    public long size320;

    @JSONField(name = "size128")
    public long size128;

    private MusicLink musicLink;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSongId() {
        return songMid;
    }

    @Override
    public ArrayList<? extends Artist> getArtists() {
        return singers;
    }

    @Override
    public String getFormattedArtistsString() {
        return SongUtils.getArtistsString(this);
    }

    @Override
    public Album getAlbum() {
        QQAlbum album = new QQAlbum();
        album.setName(this.albumName);
        album.setAlbumId(this.albumMid);
        return album;
    }

    @Override
    public MusicProvider getMusicProvider() {
        return MusicProvider.QQ;
    }

    @Override
    public void setMusicLink(MusicLink musicLink) {
        this.musicLink = musicLink;
    }

    @Override
    public MusicLink getMusicLink() {
        return musicLink;
    }

    @Override
    public String getPicUrl() {
        return "http://y.gtimg.cn/music/photo_new/T002R300x300M000" + albumMid + ".jpg";
    }

    @Override
    public Lyric getLyric() {
        return null;
    }

    /**
     * 根据size字段猜测url中的quality音质字段
     *
     * @return
     */
    public QQSongQuality guessQuality() {
        if (size320 != 0) {
            return QQSongQuality.High;
        } else if (size128 != 0) {
            return QQSongQuality.Medium;
        } else {
            return QQSongQuality.Low;
        }
    }
}
