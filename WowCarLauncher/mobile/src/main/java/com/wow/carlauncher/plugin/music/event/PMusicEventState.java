package com.wow.carlauncher.plugin.music.event;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PMusicEventState {
    private boolean run;

    public boolean isRun() {
        return run;
    }

    public PMusicEventState setRun(boolean run) {
        this.run = run;
        return this;
    }

    public PMusicEventState() {
    }

    public PMusicEventState(boolean run) {
        this.run = run;
    }
}
