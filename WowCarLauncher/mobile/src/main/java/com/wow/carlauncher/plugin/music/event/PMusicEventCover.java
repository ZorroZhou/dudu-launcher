package com.wow.carlauncher.plugin.music.event;

import android.graphics.Bitmap;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PMusicEventCover {
    private Bitmap cover;

    public Bitmap getCover() {
        return cover;
    }

    public PMusicEventCover setCover(Bitmap cover) {
        this.cover = cover;
        return this;
    }

    public PMusicEventCover() {
    }

    public PMusicEventCover(Bitmap cover) {
        this.cover = cover;
    }
}
