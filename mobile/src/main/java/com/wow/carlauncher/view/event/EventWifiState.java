package com.wow.carlauncher.view.event;

/**
 * Created by 10124 on 2018/4/23.
 */

public class EventWifiState {
    private boolean usable;

    public boolean isUsable() {
        return usable;
    }

    public EventWifiState setUsable(boolean usable) {
        this.usable = usable;
        return this;
    }
}
