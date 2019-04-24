package com.wow.carlauncher.repertory.web.qqmusic.res;

import java.util.List;

public class SearchRes extends BaseRes<SearchRes.Data> {
    public static class Data {
        private Song song;

        public Song getSong() {
            return song;
        }

        public Data setSong(Song song) {
            this.song = song;
            return this;
        }
    }

    public static class Song {
        private List<SongItem> list;

        public List<SongItem> getList() {
            return list;
        }

        public Song setList(List<SongItem> list) {
            this.list = list;
            return this;
        }
    }

    public static class SongItem {
        private int albumid;
        private String albummid;
        private String albumname;
        private String albumname_hilight;

        public int getAlbumid() {
            return albumid;
        }

        public SongItem setAlbumid(int albumid) {
            this.albumid = albumid;
            return this;
        }

        public String getAlbummid() {
            return albummid;
        }

        public SongItem setAlbummid(String albummid) {
            this.albummid = albummid;
            return this;
        }

        public String getAlbumname() {
            return albumname;
        }

        public SongItem setAlbumname(String albumname) {
            this.albumname = albumname;
            return this;
        }

        public String getAlbumname_hilight() {
            return albumname_hilight;
        }

        public SongItem setAlbumname_hilight(String albumname_hilight) {
            this.albumname_hilight = albumname_hilight;
            return this;
        }
    }
}
