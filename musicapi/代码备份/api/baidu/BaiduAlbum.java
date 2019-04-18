package com.wow.musicapi.provider.baidu;

import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.Song;
import com.wow.musicapi.util.SongUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haohua on 2018/2/11.
 */
@SuppressWarnings("SpellCheckingInspection")
class BaiduAlbum extends BaseBean implements Album {

    @SerializedName("title")
    public String name;

    @SerializedName("album_id")
    public String id;

    @SerializedName("author")
    public String author;

    @SerializedName("artist_id")
    public String artistId;

    public List<BaiduSong> songs;

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

    public void setSongs(List<BaiduSong> songs) {
        this.songs = songs;
    }

    @Override
    public List<? extends Artist> getArtists() {
        ArrayList<BaiduArtist> artists = new ArrayList<>();
        BaiduArtist artist = new BaiduArtist();
        artist.id = this.artistId;
        artist.name = this.author;
        artists.add(artist);
        return artists;
    }

    @Override
    public String getFormattedArtistsString() {
        return SongUtils.getArtistsString(getArtists());
    }

    @Override
    public MusicProvider getMusicProvider() {
        return MusicProvider.Baidu;
    }
}
