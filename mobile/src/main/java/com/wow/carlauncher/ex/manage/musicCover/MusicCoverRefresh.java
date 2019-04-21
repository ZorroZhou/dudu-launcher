package com.wow.carlauncher.ex.manage.musicCover;

import android.graphics.Bitmap;

/**
 * Created by 10124 on 2018/4/22.
 */

public class MusicCoverRefresh {
    private Bitmap cover;

    private String title;
    private String artist;

    public String getTitle() {
        return title;
    }

    public MusicCoverRefresh setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public MusicCoverRefresh setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public Bitmap getCover() {
        return cover;
    }

    public MusicCoverRefresh setCover(Bitmap cover) {
        this.cover = cover;
        return this;
    }
}
