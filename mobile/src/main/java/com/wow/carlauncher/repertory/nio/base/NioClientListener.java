package com.wow.carlauncher.repertory.nio.base;

public interface NioClientListener {
    void stateChange(NioConnectState clientState);
}
