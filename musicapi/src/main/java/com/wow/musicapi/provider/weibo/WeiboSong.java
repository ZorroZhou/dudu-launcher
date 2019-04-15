package com.wow.musicapi.provider.weibo;

import com.alibaba.fastjson.annotation.JSONField;
import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.*;
import com.wow.musicapi.util.SongUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
class WeiboSong extends BaseBean implements Song {

    @JSONField(name = "song_name")
    public String name;

    @JSONField(name = "song_id")
    public long songId;

    @JSONField(name = "artist_name")
    public String[] artistNames;

    @JSONField(name = "photo")
    public String cover;

    @JSONField(name = "play_stream")
    public String songUrl;

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
        ArrayList<WeiboArtist> artists = new ArrayList<>();
        for (String name : artistNames) {
            WeiboArtist artist = new WeiboArtist();
            artist.name = name;
            artists.add(artist);
        }
        return artists;
    }

    @Override
    public String getFormattedArtistsString() {
        return SongUtils.getArtistsString(this);
    }

    @Override
    public Album getAlbum() {
        return null;
    }

    @Override
    public MusicProvider getMusicProvider() {
        return MusicProvider.Weibo;
    }

    @Override
    public MusicLink getMusicLink() {
        WeiboSongLink link = new WeiboSongLink();
        link.songId = this.songId;
        link.url = this.songUrl;
        return link;
    }

    @Override
    public String getPicUrl() {
        return cover;
    }

    @Override
    public Lyric getLyric() {
        return null;
    }

    public void setMusicLink(MusicLink musicLink) {
        throw new UnsupportedOperationException();
    }
}
