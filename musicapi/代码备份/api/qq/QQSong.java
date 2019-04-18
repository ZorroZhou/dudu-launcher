package com.wow.musicapi.provider.qq;

import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.Lyric;
import com.wow.musicapi.model.MusicLink;
import com.wow.musicapi.model.Song;
import com.wow.musicapi.util.SongUtils;

import java.util.ArrayList;

/**
 * Created by haohua on 2018/2/9.
 */
class QQSong extends BaseBean implements Song {
    @SerializedName("songname")
    public String name;

    @SerializedName("songid")
    public String songId;

    @SerializedName("songmid")
    public String songMid;

    @SerializedName("singer")
    public ArrayList<QQSinger> singers;

    @SerializedName("albummid")
    public String albumMid;

    @SerializedName("albumname")
    public String albumName;

    @SerializedName("sizeogg")
    public long sizeogg;

    @SerializedName("sizeflac")
    public long sizeflac;

    @SerializedName("sizeape")
    public long sizeape;

    @SerializedName("size320")
    public long size320;

    @SerializedName("size128")
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
