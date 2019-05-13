package com.wow.carlauncher.view.event;

/**
 * Created by 10124 on 2018/4/23.
 */

public class EventUsbMount {
    private boolean mount;

    public boolean isMount() {
        return mount;
    }

    public EventUsbMount setMount(boolean mount) {
        this.mount = mount;
        return this;
    }
}
