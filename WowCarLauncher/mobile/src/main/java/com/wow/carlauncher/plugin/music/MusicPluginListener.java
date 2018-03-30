package com.wow.carlauncher.plugin.music;

import android.graphics.Bitmap;

/**
 * Created by 10124 on 2017/11/16.
 */

public interface MusicPluginListener {
    void refreshInfo(final String title, final String artist);

    void refreshProgress(final int curr_time, final int total_time);

    void refreshCover(final Bitmap cover);

    void refreshState(final boolean run);
}
