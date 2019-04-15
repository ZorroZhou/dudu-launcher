package com.wow.musicapi.provider.xiami;

import com.wow.musicapi.api.MusicApi;
import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.MusicLink;
import com.wow.musicapi.model.Song;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class XiamiMusicApi implements MusicApi {

    static final String USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) " +
            "AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1";

    @Override
    public List<? extends Song> searchMusicSync(String keyword, int page, boolean needLink) throws IOException {
        List<? extends Song> result = new XiamiSearchMusicRequest(keyword, page).requestSync();
        return result;
    }

    public MusicLink getMusicLinkByIdSync(String musicId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Song> getSongDetailInfoByIdsSync(boolean needLyric, String... musicIds) throws IOException {
        return null;
    }

    public Album getAlbumInfoByIdSync(String albumId, boolean needLink) throws IOException {
        XiamiAlbum album = new XiamiGetAlbumInfoRequest(albumId).requestSync();
        return album;
    }
}
