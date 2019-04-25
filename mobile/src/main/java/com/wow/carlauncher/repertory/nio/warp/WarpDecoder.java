package com.wow.carlauncher.repertory.nio.warp;

import io.netty.handler.codec.string.StringEncoder;

public class WarpDecoder extends StringEncoder {
    public static final String TAG = "WarpDecoder";

    protected boolean debug = false;

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public WarpDecoder() {
        this.debug = debug;
    }


}
