package com.wow.musicapi.provider.kuwo;

import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.Song;
import com.wow.musicapi.util.SongUtils;

import java.util.List;

/**
 * Created by haohua on 2018/2/23.
 */
@SuppressWarnings("SpellCheckingInspection")
class KuwoAlbum extends BaseBean implements Album {

    public String name;

    public String id;

    @SerializedName("artists")
    public List<KuwoArtist> artists;

    public List<KuwoSong> songs;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAlbumId() {
        return id;
    }

    @Override
    public List<? extends Song> getSongs() {
        return songs;
    }

    public void setSongs(List<KuwoSong> songs) {
        this.songs = songs;
    }

    @Override
    public List<? extends Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<KuwoArtist> artists) {
        this.artists = artists;
    }

    @Override
    public String getFormattedArtistsString() {
        return SongUtils.getArtistsString(getArtists());
    }

    @Override
    public MusicProvider getMusicProvider() {
        return MusicProvider.Kuwo;
    }
}
