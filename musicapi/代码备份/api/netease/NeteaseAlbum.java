package com.wow.musicapi.provider.netease;

import com.alibaba.fastjson.annotation.JSONField;
import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.Song;
import com.wow.musicapi.util.SongUtils;

import java.util.List;

/**
 * Created by haohua on 2018/2/11.
 */
@SuppressWarnings("SpellCheckingInspection")
class NeteaseAlbum extends BaseBean implements Album {

    @SerializedName("name")
    public String name;

    @SerializedName("id")
    public long id;

    @SerializedName("artists")
    public List<NeteaseArtist> artists;

    public List<NeteaseSong> songs;

    @SerializedName("picUrl")
    public String picUrl;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAlbumId() {
        return String.valueOf(id);
    }

    @Override
    public List<? extends Song> getSongs() {
        return songs;
    }

    public void setSongs(List<NeteaseSong> songs) {
        this.songs = songs;
    }

    @Override
    public List<? extends Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<NeteaseArtist> artists) {
        this.artists = artists;
    }

    @Override
    public String getFormattedArtistsString() {
        return SongUtils.getArtistsString(getArtists());
    }

    @Override
    public MusicProvider getMusicProvider() {
        return MusicProvider.Netease;
    }
}
