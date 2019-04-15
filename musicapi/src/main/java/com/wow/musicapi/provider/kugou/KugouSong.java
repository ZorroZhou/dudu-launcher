package com.wow.musicapi.provider.kugou;

import com.alibaba.fastjson.annotation.JSONField;
import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.*;
import com.wow.musicapi.util.SongUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
class KugouSong extends BaseBean implements Song {

    @JSONField(name = "songname", alternateNames = "songName")
    public String name;

    @JSONField(name = "singername", alternateNames = "singerName")
    public String artist;

    @JSONField(name = "album_id", alternateNames = "albumid")
    public String albumId;

    @JSONField(name = "album_name")
    public String albumName;

    @JSONField(name = "hash")
    public String songHash;

    @JSONField(name = "imgUrl")
    public String picUrlFormat;

    @JSONField(name = "url")
    public String songUrl;

    @JSONField(name = "bitRate")
    public long bitRate;

    @JSONField(name = "fileSize")
    public long fileSize;

    @JSONField(name = "extName")
    public String extName;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSongId() {
        return songHash;
    }

    @Override
    public List<? extends Artist> getArtists() {
        ArrayList<KugouArtist> result = new ArrayList<>();
        KugouArtist artist = new KugouArtist();
        artist.name = this.artist;
        result.add(artist);
        return result;
    }

    @Override
    public String getFormattedArtistsString() {
        return SongUtils.getArtistsString(this);
    }

    @Override
    public Album getAlbum() {
        KugouAlbum album = new KugouAlbum();
        album.name = this.albumName;
        album.id = this.albumId;
        return album;
    }

    @Override
    public MusicProvider getMusicProvider() {
        return MusicProvider.Kugou;
    }

    @Override
    public MusicLink getMusicLink() {
        KugouSongLink link = new KugouSongLink();
        link.url = songUrl;
        link.bitRate = bitRate * 1000;
        link.size = fileSize;
        link.extName = extName;
        return link;
    }

    @Override
    public String getPicUrl() {
        return picUrlFormat.replace("{size}", "150");
    }

    @Override
    public Lyric getLyric() {
        return null;
    }

    public void setMusicLink(MusicLink musicLink) {
    }
}
