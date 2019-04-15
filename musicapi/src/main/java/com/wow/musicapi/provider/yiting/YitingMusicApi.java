package com.wow.musicapi.provider.yiting;

import com.wow.musicapi.api.MusicApi;
import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.MusicLink;
import com.wow.musicapi.model.Song;
import com.wow.musicapi.util.SongUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by haohua on 2018/2/23.
 */
@SuppressWarnings("SpellCheckingInspection")
public class YitingMusicApi implements MusicApi {
    @Override
    public List<? extends Song> searchMusicSync(String keyword, int page, boolean needLink) throws IOException {
        List<? extends Song> result = new YitingSearchMusicRequest(keyword, page).requestSync();
        if (needLink) {
            String[] songIds = SongUtils.getSongIdsFromSongList(result);
            List<YitingSong> detailSongs = new YitingGetMusicDetailsRequest(songIds).requestSync();
            return detailSongs;
        } else {
            return result;
        }
    }

    @Override
    public MusicLink getMusicLinkByIdSync(String musicId) throws IOException {
        return null;
    }

    @Override
    public List<? extends Song> getSongDetailInfoByIdsSync(boolean needLyric, String... musicIds) throws IOException {
        List<YitingSong> details = new YitingGetMusicDetailsRequest(musicIds).requestSync();
        return details;
    }

    @Override
    public Album getAlbumInfoByIdSync(String albumId, boolean needLink) throws IOException {
        return null;
    }
}
