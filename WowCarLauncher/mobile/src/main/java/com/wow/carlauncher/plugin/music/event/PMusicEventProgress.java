package com.wow.carlauncher.plugin.music.event;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PMusicEventProgress {
    private int currTime;
    private int totalTime;

    public int getCurrTime() {
        return currTime;
    }

    public PMusicEventProgress setCurrTime(int currTime) {
        this.currTime = currTime;
        return this;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public PMusicEventProgress setTotalTime(int totalTime) {
        this.totalTime = totalTime;
        return this;
    }

    public PMusicEventProgress() {
    }

    public PMusicEventProgress(int currTime, int totalTime) {
        this.currTime = currTime;
        this.totalTime = totalTime;
    }
}
