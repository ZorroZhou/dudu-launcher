package com.wow.carlauncher.plugin.music.event;

/**
 * Created by 10124 on 2017/10/28.
 */

public class PEventMusicInfoChange {
    public String cover;
    public String title;
    public String artist;

    public PEventMusicInfoChange() {
    }

    public PEventMusicInfoChange(String cover, String title, String artist) {
        this.cover = cover;
        this.title = title;
        this.artist = artist;
    }
}
