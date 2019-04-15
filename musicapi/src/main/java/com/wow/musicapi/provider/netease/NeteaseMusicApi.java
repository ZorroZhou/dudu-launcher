package com.wow.musicapi.provider.netease;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.codec.binary.Hex;

import com.wow.musicapi.api.MusicApi;
import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.Song;
import com.wow.musicapi.model.MusicLink;
import com.wow.musicapi.util.AES;
import com.wow.musicapi.util.Function;
import com.wow.musicapi.util.SongUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by haohua on 2018/2/10.
 */
@SuppressWarnings("SpellCheckingInspection")
public class NeteaseMusicApi implements MusicApi {
    private static final String SECRET = "7246674226682325323F5E6544673A51";

    private Function<String[], List<? extends MusicLink>> mGetSongLinksRequestFunction = new Function<String[],
            List<? extends MusicLink>>() {
        @Override
        public List<? extends MusicLink> apply(String[] musicIds) {
            try {
                return new NeteaseGetMusicLinksRequest(musicIds).requestSync();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    @Override
    public List<? extends Song> searchMusicSync(String keyword, int page, boolean needLink) throws IOException {
        List<? extends Song> result = new NeteaseSearchMusicRequest(keyword, page).requestSync();
        if (needLink) {
            SongUtils.fillSongLinks(result, mGetSongLinksRequestFunction);
        }
        return result;
    }

    @Override
    public MusicLink getMusicLinkByIdSync(String musicId) throws IOException {
        return new NeteaseGetMusicLinksRequest(musicId).requestSync().get(0);
    }

    @Override
    public Album getAlbumInfoByIdSync(String albumId, boolean needLink) throws IOException {
        Album result = new NeteaseGetAlbumInfoRequest(albumId).requestSync();
        if (needLink) {
            List<? extends Song> songs = result.getSongs();
            SongUtils.fillSongLinks(songs, mGetSongLinksRequestFunction);
        }
        return result;
    }

    public List<? extends Song> getSongDetailInfoByIdsSync(boolean needLyric, String... musicIds) throws IOException {
        List<NeteaseSong> songs = new NeteaseGetMusicDetailsRequest(musicIds).requestSync();
        if (needLyric) {
            for (NeteaseSong song : songs) {
                NeteaseLyric lrc = new NeteaseGetMusicLyricRequest(song.getSongId()).requestSync();
                song.setLyric(lrc);
            }
        }
        return songs;
    }

    static String encrypt(JSONObject json) {
        return encrypt(json.toString());
    }

    static String encrypt(String raw) {
        byte[] encrypted;
        try {
            encrypted = AES.encrypt(raw, Hex.decodeHex(SECRET.toCharArray()));
            return new String(Hex.encodeHex(encrypted)).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

}
