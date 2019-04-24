package com.wow.carlauncher.ex.plugin.music;

import android.content.Context;

/**
 * Created by 10124 on 2017/11/16.
 */

public abstract class MusicController {
    protected Context context;
    protected MusicPlugin musicPlugin;

    public void init(Context context, MusicPlugin musicPlugin) {
        this.context = context;
        this.musicPlugin = musicPlugin;
    }

    public abstract void destroy();

    public abstract void play();

    public abstract void pause();

    public abstract void next();

    public abstract void pre();

    public abstract String clazz();
}
