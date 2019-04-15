package com.wow.musicapi.provider.baidu;

import com.alibaba.fastjson.annotation.JSONField;
import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.*;
import com.wow.musicapi.util.SongUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haohua on 2018/2/23.
 */
@SuppressWarnings("SpellCheckingInspection")
class BaiduSong extends BaseBean implements Song {

    @JSONField(name = "title", alternateNames = "songName")
    public String name;

    @JSONField(name = "song_id", alternateNames = "songId")
    public long songId;

    @JSONField(name = "author", alternateNames = "artistName")
    public String artist;

    @JSONField(name = "all_artist_id")
    public String artistIds;

    @JSONField(name = "album_id", alternateNames = "albumId")
    public String albumId;

    @JSONField(name = "album_title", alternateNames = "albumName")
    public String albumTitle;

    @JSONField(name = "lrclink", alternateNames = "lrcLink")
    public String lrcLink;

    @JSONField(name = "songPicBig")
    public String songPicBig;

    @JSONField(name = "songLink")
    public String songUrl;

    @JSONField(name = "size")
    public long size;

    @JSONField(name = "rate")
    public long bitrate;

    @JSONField(name = "format")
    public String format;


    @Override
    public String getName() {
        return removeEm(name);
    }

    @Override
    public String getSongId() {
        return String.valueOf(songId);
    }

    @Override
    public List<? extends Artist> getArtists() {
        ArrayList<BaiduArtist> result = new ArrayList<>();
        String[] artistNames = getArtist().split(",");
        for (String name : artistNames) {
            BaiduArtist artist = new BaiduArtist();
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
        BaiduAlbum album = new BaiduAlbum();
        if ("0".equals(this.albumId)) {
            album.id = null;
        } else {
            album.id = this.albumId;
        }
        album.name = getAlbumTitle();
        return album;
    }

    @Override
    public MusicProvider getMusicProvider() {
        return MusicProvider.Baidu;
    }

    @Override
    public MusicLink getMusicLink() {
        BaiduSongLink link = new BaiduSongLink();
        link.url = songUrl;
        link.format = this.format;
        link.size = this.size;
        link.songId = String.valueOf(this.songId);
        link.bitRate = this.bitrate * 1000;
        return link;
    }

    @Override
    public String getPicUrl() {
        return songPicBig;
    }

    @Override
    public Lyric getLyric() {
        BaiduLyric lyric = new BaiduLyric();
        lyric.lyricUrl = this.lrcLink;
        return lyric;
    }

    public void setMusicLink(MusicLink musicLink) {
    }

    public String getArtist() {
        return removeEm(artist);
    }

    public String getAlbumTitle() {
        return removeEm(albumTitle);
    }

    private static String removeEm(String text) {
        return text.replaceAll("<em>", "").replaceAll("</em>", "");
    }
}
