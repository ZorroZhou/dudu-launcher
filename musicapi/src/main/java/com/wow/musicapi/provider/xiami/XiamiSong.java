package com.wow.musicapi.provider.xiami;

import com.google.gson.annotations.SerializedName;
import com.wow.musicapi.api.MusicProvider;
import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.Lyric;
import com.wow.musicapi.model.MusicLink;
import com.wow.musicapi.model.Song;
import com.wow.musicapi.util.SongUtils;

import java.util.ArrayList;

@SuppressWarnings("SpellCheckingInspection")
public class XiamiSong extends BaseBean implements Song {
    @SerializedName("song_name")
    public String name;

    @SerializedName("song_id")
    public String songId;

    @SerializedName("album_id")
    public long albumId;

    @SerializedName("album_name")
    public String albumName;

    @SerializedName("album_logo")
    public String albumPicUrl;

    @SerializedName("artist_id")
    public long artistId;

    @SerializedName("artist_name")
    public String artistName;

    @SerializedName("artist_logo")
    public String artistPicUrl;

    @SerializedName("listen_file")
    public String listenUrl;

    @SerializedName("lyric")
    public String lyricUrl;

    @SerializedName("singers")
    public String singers;

    @Override
    public MusicLink getMusicLink() {
        XiamiSongLink link = new XiamiSongLink();
        link.url = this.listenUrl;
        return link;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSongId() {
        return songId;
    }

    @Override
    public ArrayList<? extends Artist> getArtists() {
        ArrayList<XiamiSinger> artists = new ArrayList<>();
        if (singers != null) {
            String[] singerArray = singers.split(";");
            for (String singerName : singerArray) {
                XiamiSinger singer = new XiamiSinger();
                singer.artistName = singerName;
                artists.add(singer);
            }
        } else {
            XiamiSinger singer = new XiamiSinger();
            singer.artistId = String.valueOf(artistId);
            singer.artistName = artistName;
            artists.add(singer);
        }
        return artists;
    }

    @Override
    public String getFormattedArtistsString() {
        return SongUtils.getArtistsString(this);
    }

    @Override
    public Album getAlbum() {
        XiamiAlbum album = new XiamiAlbum();
        album.setName(this.albumName);
        album.setAlbumId(String.valueOf(this.albumId));
        return album;
    }

    @Override
    public MusicProvider getMusicProvider() {
        return MusicProvider.Xiami;
    }

    @Override
    public void setMusicLink(MusicLink musicLink) {
    }

    @Override
    public String getPicUrl() {
        return albumPicUrl;
    }

    @Override
    public Lyric getLyric() {
        XiamiLyric lyric = new XiamiLyric();
        lyric.setLyricUrl(this.lyricUrl);
        return lyric;
    }
}
