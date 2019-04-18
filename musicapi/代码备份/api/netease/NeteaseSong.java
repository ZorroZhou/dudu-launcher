package com.wow.musicapi.provider.netease;

import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.MusicLink;
import com.wow.musicapi.model.Song;
import com.wow.musicapi.util.SongUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haohua on 2018/2/9.
 */
@SuppressWarnings("SpellCheckingInspection")
class NeteaseSong extends BaseBean implements Song {

    @SerializedName("name")
    public String name;

    @SerializedName("id")
    public long songId;

    @SerializedName("ar", alternateNames = {"artists",})
    public ArrayList<NeteaseArtist> artists;

    @SerializedName("al", alternateNames = {"album",})
    public NeteaseAlbum album;

    private MusicLink musicLink;

    private NeteaseLyric lyric;

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
        return artists;
    }

    @Override
    public String getFormattedArtistsString() {
        return SongUtils.getArtistsString(this);
    }

    @Override
    public Album getAlbum() {
        return album;
    }

    @Override
    public MusicProvider getMusicProvider() {
        return MusicProvider.Netease;
    }

    @Override
    public MusicLink getMusicLink() {
        return musicLink;
    }

    @Override
    public String getPicUrl() {
        if (album != null) {
            return album.picUrl;
        }
        return null;
    }

    public void setMusicLink(MusicLink musicLink) {
        this.musicLink = musicLink;
    }

    public void setLyric(NeteaseLyric lyric) {
        this.lyric = lyric;
    }

    public NeteaseLyric getLyric() {
        return lyric;
    }
}
