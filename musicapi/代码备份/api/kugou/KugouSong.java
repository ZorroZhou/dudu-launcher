package com.wow.musicapi.provider.kugou;

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
class KugouSong extends BaseBean implements Song {

    @SerializedName("songname", alternateNames = "songName")
    public String name;

    @SerializedName("singername", alternateNames = "singerName")
    public String artist;

    @SerializedName("album_id", alternateNames = "albumid")
    public String albumId;

    @SerializedName("album_name")
    public String albumName;

    @SerializedName("hash")
    public String songHash;

    @SerializedName("imgUrl")
    public String picUrlFormat;

    @SerializedName("url")
    public String songUrl;

    @SerializedName("bitRate")
    public long bitRate;

    @SerializedName("fileSize")
    public long fileSize;

    @SerializedName("extName")
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
