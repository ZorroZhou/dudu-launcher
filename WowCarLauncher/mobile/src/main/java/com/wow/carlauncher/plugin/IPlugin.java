package com.wow.carlauncher.plugin;

import android.view.View;

/**
 * Created by 10124 on 2017/10/26.
 */

public interface IPlugin {
    View getLauncherView();

    View getPopupView();

    PopupViewProportion getPopupViewProportion();

    void destroy();
}
