package com.wow.carlauncher.plugin.amapcar;

import android.content.Context;
import android.media.AudioManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.plugin.IPlugin;

/**
 * Created by 10124 on 2017/11/4.
 */

public class AMapCarPlugin implements IPlugin {
    protected Context context;
    private LinearLayout launcherView;

    public AMapCarPlugin(Context context) {
        this.context = context;
    }

    @Override
    public View getLauncherView() {
        return null;
    }

    @Override
    public View getPopupView() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
