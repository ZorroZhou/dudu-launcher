package com.wow.carlauncher.ex.plugin;

import android.content.Context;

import com.wow.carlauncher.ex.ContextEx;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by 10124 on 2017/10/26.
 */

public class BasePlugin extends ContextEx {
    public BasePlugin() {
    }

    public void init(Context context) {
        setContext(context);
    }

    public void postEvent(Object e) {
        EventBus.getDefault().post(e);
    }
}
