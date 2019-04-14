package com.wow.carlauncher.ex.plugin.amapcar.event;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PAmapMuteStateInfo {
    private boolean mute = false;

    public boolean isMute() {
        return mute;
    }

    public PAmapMuteStateInfo setMute(boolean mute) {
        this.mute = mute;
        return this;
    }
}
