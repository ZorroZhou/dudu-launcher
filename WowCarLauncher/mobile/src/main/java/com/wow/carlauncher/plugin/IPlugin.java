package com.wow.carlauncher.plugin;

import android.view.View;

/**
 * Created by 10124 on 2017/10/26.
 */

public interface IPlugin {
    void setPluginManage(PluginManage pluginManage);

    View getLauncherView();

    View getPopupView();

    void destroy();
}
