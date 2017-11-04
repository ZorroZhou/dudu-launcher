package com.wow.carlauncher.plugin.music;

import android.content.Context;
import android.view.View;

import com.wow.carlauncher.plugin.PopupViewProportion;

/**
 * Created by 10124 on 2017/10/26.
 */

public abstract class MusicController {
    protected Context context;

    public MusicController(Context context) {
        this.context = context;
    }

    public void destroy() {
    }

    public abstract View getLauncherView();

    public abstract View getPopupView();

}
