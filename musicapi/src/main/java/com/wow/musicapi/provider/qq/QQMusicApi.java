package com.wow.musicapi.provider.qq;

import com.wow.musicapi.api.MusicApi;
import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.Song;
import com.wow.musicapi.model.MusicLink;

import java.io.IOException;
import java.util.List;

/**
 * Created by haohua on 2018/2/8.
 */
public class QQMusicApi implements MusicApi {

    static final String GUID = "5150825362";

    private QQUpdateVKeyRequest.VKey mKeyCache;

    static final String USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) " +
            "AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1";

    @Override
    public List<? extends Song> searchMusicSync(String keyword, int page, boolean needLink) throws IOException {
        List<? extends Song> result = new QQSearchMusicRequest(keyword, page).requestSync();
        if (needLink) {
            fillSongLinks(result);
        }
        return result;
    }

    private void fillSongLinks(List<? extends Song> result) throws IOException {
        for (Song song : result) {
            song.setMusicLink(getMusicLinkByIdSync(song));
        }
    }

    private MusicLink getMusicLinkByIdSync(Song song) throws IOException {
        if (mKeyCache == null) {
            QQUpdateVKeyRequest.VKey vKey = new QQUpdateVKeyRequest().requestSync();
            mKeyCache = vKey;
        }
        QQSongLink result = getSongLink(mKeyCache, song);
        return result;
    }

    @Override
    public MusicLink getMusicLinkByIdSync(String musicId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Song> getSongDetailInfoByIdsSync(boolean needLyric, String... musicIds) throws IOException {
        return null;
    }

    private static QQSongLink getSongLink(QQUpdateVKeyRequest.VKey vKeyObj, Song song)  {
        String host = vKeyObj.sips.get(0);
        String vKey = vKeyObj.key;
        QQSong  qqSong = (QQSong) song;
        QQSongQuality quality = qqSong.guessQuality();
        final String link = buildQQMusicLink(host, quality,  song.getSongId(), vKey, GUID);
        QQSongLink result = new QQSongLink();
        result.url = link;
        return result;
    }

    @Override
    public Album getAlbumInfoByIdSync(String albumId, boolean needLink) throws IOException {
        Album album = new QQGetAlbumInfoRequest(albumId).requestSync();
        fillSongLinks(album.getSongs());
        return album;
    }

    private static String buildQQMusicLink(String host, QQSongQuality quality, String mid, String key, String guid) {
        String link = host + quality.getPrefix() + mid + "." + quality.getSuffix() + "?vkey=" + key + "&guid=" +
                guid + "&fromtag=1";
        return link;
    }
}
