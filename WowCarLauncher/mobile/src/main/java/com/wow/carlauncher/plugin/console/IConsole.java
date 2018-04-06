package com.wow.carlauncher.plugin.console;

import android.content.Context;

/**
 * Created by 10124 on 2017/10/26.
 */

public abstract class IConsole {
    protected Context context;

    public IConsole() {

    }

    public IConsole(Context context) {
        this.context = context;
    }

    public abstract void decVolume();

    public abstract void incVolume();

    public abstract void mute();

    public abstract void clearTask();

    public abstract void callAnswer();

    public abstract void callHangup();
}
