package com.wow.musicapi.provider.kuwo;

import com.alibaba.fastjson.annotation.JSONField;
import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.*;
import com.wow.musicapi.util.CommonUtils;
import com.wow.musicapi.util.SongUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haohua on 2018/2/23.
 */
@SuppressWarnings("SpellCheckingInspection")
class KuwoSong extends BaseBean implements Song {

    @JSONField(name = "SONGNAME")
    public String nameRaw;

    @JSONField(name = "MUSICRID")
    public String songId;

    @JSONField(name = "ARTIST")
    public String artistRaw;

    @JSONField(name = "ALBUMID")
    public String albumId;

    @JSONField(name = "ALBUM")
    public String albumNameRaw;

    private MusicLink musicLink;

    @Override
    public String getName() {
        String result = CommonUtils.unescapeHtmlAndXml(nameRaw);
        return result;
    }

    @Override
    public String getSongId() {
        return String.valueOf(songId);
    }

    @Override
    public List<? extends Artist> getArtists() {
        ArrayList<KuwoArtist> result = new ArrayList<>();
        String[] artistArray = getArtist().split(",");
        for (String name : artistArray) {
            KuwoArtist artist = new KuwoArtist();
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
        KuwoAlbum album = new KuwoAlbum();
        album.id = albumId;
        album.name = getAlbumName();
        return album;
    }

    @Override
    public MusicProvider getMusicProvider() {
        return MusicProvider.Kuwo;
    }

    @Override
    public MusicLink getMusicLink() {
        return musicLink;
    }

    @Override
    public String getPicUrl() {
        return null;
    }

    @Override
    public Lyric getLyric() {
        return null;
    }

    public void setMusicLink(MusicLink musicLink) {
        this.musicLink = musicLink;
    }

    public String getArtist() {
        String artist = CommonUtils.unescapeHtmlAndXml(artistRaw);
        return artist;
    }

    public String getAlbumName() {
        String albumName = CommonUtils.unescapeHtmlAndXml(albumNameRaw);
        return albumName;
    }
}
