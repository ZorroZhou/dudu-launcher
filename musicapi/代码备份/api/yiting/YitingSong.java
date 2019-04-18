package com.wow.musicapi.provider.yiting;

import com.alibaba.fastjson.annotation.JSONField;
import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.Lyric;
import com.wow.musicapi.model.MusicLink;
import com.wow.musicapi.model.Song;
import com.wow.musicapi.util.SongUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haohua on 2018/2/23.
 */
@SuppressWarnings("SpellCheckingInspection")
class YitingSong extends BaseBean implements Song {

    @SerializedName("song_name")
    public String name;

    @SerializedName("song_id")
    public String songId;

    @SerializedName("song_filepath")
    public String song_filepath;

    @SerializedName("singer_name")
    public String artist;

    @SerializedName("album_id")
    public String albumId;

    @SerializedName("album_name")
    public String albumTitle;

    @SerializedName("album_cover")
    public String albumCover;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSongId() {
        return String.valueOf(songId);
    }

    @Override
    public List<? extends Artist> getArtists() {
        ArrayList<YitingArtist> result = new ArrayList<>();
        String[] artistNames = getArtist().split(",");
        for (String name : artistNames) {
            YitingArtist artist = new YitingArtist();
            artist.name = name;
            result.add(artist);
        }
        return result;
    }

    @Override
    public String getFormattedArtistsString() {
        return SongUtils.getArtistsString(this);
    }

    @Override
    public Album getAlbum() {
        YitingAlbum album = new YitingAlbum();
        album.id = this.albumId;
        album.name = getAlbumTitle();
        return album;
    }

    @Override
    public MusicProvider getMusicProvider() {
        return MusicProvider.Yiting;
    }

    @Override
    public MusicLink getMusicLink() {
        YitingSongLink link = new YitingSongLink();
        link.song_filepath = this.song_filepath;
        link.songId = this.songId;
        return link;
    }

    @Override
    public String getPicUrl() {
        return albumCover;
    }

    @Override
    public Lyric getLyric() {
        return null;
    }

    public void setMusicLink(MusicLink musicLink) {
        // nop
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }
}
