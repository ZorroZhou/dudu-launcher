package com.wow.carlauncher.ex.plugin.music.event;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PMusicEventState {
    private boolean run;
    private boolean showProgress;

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

    public boolean isShowProgress() {
        return showProgress;
    }

    public PMusicEventState setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
        return this;
    }
}
