package com.wow.carlauncher.repertory.nio.warp;

import java.nio.charset.Charset;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LineBasedFrameDecoder;
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
