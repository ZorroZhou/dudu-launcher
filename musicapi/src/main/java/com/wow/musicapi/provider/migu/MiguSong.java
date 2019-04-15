package com.wow.musicapi.provider.migu;

import com.alibaba.fastjson.annotation.JSONField;
import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.*;
import com.wow.musicapi.util.SongUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
class MiguSong extends BaseBean implements Song {

    @JSONField(name = "songName")
    public String name;

    @JSONField(name = "id")
    public String songId;

    @JSONField(name = "artist")
    public String artist;

    @JSONField(name = "cover")
    public String cover;

    @JSONField(name = "albumId")
    public String albumId;

    @JSONField(name = "albumName")
    public String albumName;

    @JSONField(name = "mp3")
    public String songUrl;

    @JSONField(name = "lyrics")
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
