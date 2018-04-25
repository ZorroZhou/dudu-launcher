package com.wow.carlauncher.ex.plugin.console.event;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PConsoleEventLightState {
    private boolean open;

    public boolean isOpen() {
        return open;
    }

    public PConsoleEventLightState setOpen(boolean open) {
        this.open = open;
        return this;
    }

    public PConsoleEventLightState() {
    }

    public PConsoleEventLightState(boolean open) {
        this.open = open;
    }
}
