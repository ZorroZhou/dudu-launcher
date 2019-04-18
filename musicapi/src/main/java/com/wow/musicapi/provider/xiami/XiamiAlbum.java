package com.wow.musicapi.provider.xiami;

import com.google.gson.annotations.SerializedName;
import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.Song;
import com.wow.musicapi.util.SongUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class XiamiAlbum extends BaseBean implements Album {
    @SerializedName("artist_name")
    public String artistName;

    @SerializedName("artist_id")
    public long artistId;

    @SerializedName("album_id")
    public String albumId;

    @SerializedName("album_name")
    public String albumName;

    @SerializedName("songs")
    public List<XiamiSong> songs;

    @Override
    public String getName() {
        return albumName;
    }

    public void setName(String name) {
        this.albumName = name;
    }

    @Override
    public String getAlbumId() {
        return albumId;
    }

    @Override
    public List<? extends Artist> getArtists() {
        XiamiSinger artist = new XiamiSinger();
        artist.artistName = this.artistName;
        artist.artistId = String.valueOf(this.artistId);
        ArrayList<XiamiSinger> result = new ArrayList<>();
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
        return MusicProvider.Xiami;
    }
}
