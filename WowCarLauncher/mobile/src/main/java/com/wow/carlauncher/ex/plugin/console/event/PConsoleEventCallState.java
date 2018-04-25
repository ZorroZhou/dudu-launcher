package com.wow.carlauncher.ex.plugin.console.event;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PConsoleEventCallState {
    private boolean calling;

    public boolean isCalling() {
        return calling;
    }

    public PConsoleEventCallState setCalling(boolean calling) {
        this.calling = calling;
        return this;
    }

    public PConsoleEventCallState() {
    }

    public PConsoleEventCallState(boolean calling) {
        this.calling = calling;
    }
}
