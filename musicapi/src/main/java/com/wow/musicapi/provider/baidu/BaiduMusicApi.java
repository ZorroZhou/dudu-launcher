package com.wow.musicapi.provider.baidu;

import com.wow.musicapi.api.MusicApi;
import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.MusicLink;
import com.wow.musicapi.model.Song;
import com.wow.musicapi.util.SongUtils;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class BaiduMusicApi implements MusicApi {
    @Override
    public List<? extends Song> searchMusicSync(String keyword, int page, boolean needLink) throws Throwable {
        List<? extends Song> result = new BaiduSearchMusicRequest(keyword, page).requestSync();
        if (needLink) {
            String[] songIds = SongUtils.getSongIdsFromSongList(result);
            List<BaiduSong> details = new BaiduGetSongDetailsRequest(songIds).requestSync();
            result = details;
        }
        return result;
    }

    @Override
    public MusicLink getMusicLinkByIdSync(String musicId) throws Throwable {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Song> getSongDetailInfoByIdsSync(boolean needLyric, String... musicIds) throws Throwable {
        throw new UnsupportedOperationException();
    }

    @Override
    public Album getAlbumInfoByIdSync(String albumId, boolean needLink) throws Throwable {
        BaiduAlbum album = new BaiduGetAlbumInfoRequest(albumId).requestSync();
        return album;
    }
}
