package com.wow.musicapi.provider.qq;

import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.Song;
import com.wow.musicapi.util.SongUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haohua on 2018/2/17.
 */
class QQAlbum extends BaseBean implements Album {
    @SerializedName("name")
    private String name;

    @SerializedName("mid")
    private String albumId;

    @SerializedName("singermid")
    private String singerMid;

    @SerializedName("singername")
    private String singerName;

    private List<? extends Song> songs;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAlbumId() {
        return albumId;
    }

    @Override
    public List<? extends Artist> getArtists() {
        QQSinger artist = new QQSinger();
        artist.name = this.singerName;
        artist.mid = this.singerMid;
        ArrayList<QQSinger> result = new ArrayList<>();
        result.add(artist);
        return result;
    }

    @Override
    public String getFormattedArtistsString() {
        return SongUtils.getArtistsString(getArtists());
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    @Override
    public List<? extends Song> getSongs() {
        return songs;
    }

    @Override
    public MusicProvider getMusicProvider() {
        return MusicProvider.QQ;
    }

    public void setSongs(List<? extends Song> songs) {
        this.songs = songs;
    }

    public String getSingerMid() {
        return singerMid;
    }

    public void setSingerMid(String singerMid) {
        this.singerMid = singerMid;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }
}
