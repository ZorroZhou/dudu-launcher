package com.wow.musicapi.provider.migu;

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

@SuppressWarnings("SpellCheckingInspection")
class MiguSong extends BaseBean implements Song {

    @SerializedName("songName")
    public String name;

    @SerializedName("id")
    public String songId;

    @SerializedName("artist")
    public String artist;

    @SerializedName("cover")
    public String cover;

    @SerializedName("albumId")
    public String albumId;

    @SerializedName("albumName")
    public String albumName;

    @SerializedName("mp3")
    public String songUrl;

    @SerializedName("lyrics")
    public String lyricsUrl;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSongId() {
        return songId;
    }

    @Override
    public List<? extends Artist> getArtists() {
        ArrayList<MiguArtist> result = new ArrayList<>();
        String[] artistArray = artist.split(",");
        for (String name : artistArray) {
            MiguArtist artist = new MiguArtist();
            artist.name = name.trim();
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
        MiguAlbum album = new MiguAlbum();
        album.id = albumId;
        album.name = albumName;
        return album;
    }

    @Override
    public MusicProvider getMusicProvider() {
        return MusicProvider.Migu;
    }

    @Override
    public MusicLink getMusicLink() {
        MiguSongLink link = new MiguSongLink();
        link.setUrl(songUrl);
        return link;
    }

    @Override
    public String getPicUrl() {
        return cover;
    }

    @Override
    public Lyric getLyric() {
        MiguLyric lyric = new MiguLyric();
        lyric.lyricUrl = lyricsUrl;
        return lyric;
    }

    public void setMusicLink(MusicLink musicLink) {
        throw new UnsupportedOperationException();
    }
}
