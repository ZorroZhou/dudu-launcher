package com.wow.carlauncher.plugin.pevent;

/**
 * Created by 10124 on 2017/10/28.
 */

public class PEventMusicInfoChange {
    public String title;
    public String artist;
    public int curr_time;
    public int total_time;

    public PEventMusicInfoChange() {
    }

    public PEventMusicInfoChange(String title, String artist, int curr_time, int total_time) {
        this.title = title;
        this.artist = artist;
        this.curr_time = curr_time;
        this.total_time = total_time;
    }
}
