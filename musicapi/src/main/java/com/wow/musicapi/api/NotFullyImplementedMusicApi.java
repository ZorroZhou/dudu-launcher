package com.wow.musicapi.api;

import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.Song;
import com.wow.musicapi.model.MusicLink;

import java.io.IOException;
import java.util.List;

/**
 * Created by haohua on 2018/2/11.
 */
public class NotFullyImplementedMusicApi implements MusicApi {

    @Override
    public List<? extends Song> searchMusicSync(String keyword, int page, boolean needLink) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MusicLink getMusicLinkByIdSync(String musicId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Song> getSongDetailInfoByIdsSync(boolean needLyric, String... musicIds) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Album getAlbumInfoByIdSync(String albumId, boolean needLink) {
        throw new UnsupportedOperationException();
    }
}
