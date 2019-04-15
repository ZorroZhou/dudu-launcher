package com.wow.musicapi.util;

import com.wow.musicapi.model.Album;
import com.wow.musicapi.model.Artist;
import com.wow.musicapi.model.MusicLink;
import com.wow.musicapi.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongUtils {
    public static String getArtistsString(Song song) {
        return getArtistsString(song.getArtists());
    }

    public static String getArtistsString(List<? extends Artist> artists) {
        ArrayList<String> names = new ArrayList<>();
        if (artists != null) {
            for (Artist artist : artists) {
                names.add(artist.getName());
            }
        }
        return TextUtils.join(", ", names);
    }

    public static String generateSongPath(Album album, Song song) {
        // 专辑文件夹名称是专辑名+专辑信息中的歌手名(不要用歌曲中的歌手名)
        final String albumPathName = album.getName() + " - " + album.getFormattedArtistsString();
        String path = FileUtils.combine(song.getMusicProvider().toString(),
                albumPathName, generateSongFilename(song));
        return path;
    }

    private static String generateSongFilename(Song song) {
        return song.getName() + " - " + getArtistsString(song) + ".mp3";
    }

    public static String[] getSongIdsFromSongList(List<? extends Song> songs) {
        ArrayList<String> musicIds = new ArrayList<>();
        for (Song song : songs) {
            musicIds.add(song.getSongId());
        }
        return musicIds.toArray(new String[]{});
    }

    /**
     * 使用api做一次请求，批量填入歌曲列表每一首歌曲的url
     *
     * @param songs
     * @throws IOException
     */
    public static void fillSongLinks(List<? extends Song> songs, Function<String[], List<? extends MusicLink>>
            function) {
        if (songs != null && !songs.isEmpty()) {
            // 1. 先获得所有songId组成的列表
            String[] songIds = getSongIdsFromSongList(songs);
            // 2. 使用api批量获取歌曲url
            List<? extends MusicLink> links = function.apply(songIds);
            // 3. 把songId/url分别作为key/value，存入字典
            HashMap<String, MusicLink> map = new HashMap<>();
            for (MusicLink link : links) {
                map.put(link.getSongId(), link);
            }
            // 4. 循环一次，把上面索引表中的url数据填入Song对象
            for (Song song : songs) {
                song.setMusicLink(map.get(song.getSongId()));
            }
        }
    }

    @SuppressWarnings("unused")
    public static String getReadableBitRate(long bitRate) {
        //noinspection SpellCheckingInspection
        if (bitRate <= 0) {
            return "";
        } else {
            return (bitRate / 1000) + "kbps";
        }
    }
}
