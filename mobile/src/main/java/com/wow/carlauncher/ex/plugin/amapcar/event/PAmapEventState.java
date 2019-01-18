package com.wow.carlauncher.ex.plugin.amapcar.event;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PAmapEventState {
    private boolean running;

    public boolean isRunning() {
        return running;
    }

    public PAmapEventState setRunning(boolean running) {
        this.running = running;
        return this;
    }
}
