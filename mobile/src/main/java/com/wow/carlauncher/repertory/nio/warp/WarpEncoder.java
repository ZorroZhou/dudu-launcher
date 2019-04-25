package com.wow.carlauncher.repertory.nio.warp;

import android.util.Log;

import com.wow.carlauncher.common.util.GsonUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class WarpEncoder extends MessageToByteEncoder<BaseWarp> {
    public static final String TAG = "WarpEncoder";

    protected boolean debug = false;

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, BaseWarp message, ByteBuf byteBuf) {
        if (debug) {
            Log.d(TAG, "start Encoder:" + message);
        }
        String msg = message.getCmd() + GsonUtil.getGson().toJson(message);
        channelHandlerContext.write(msg);
        channelHandlerContext.flush();
    }
}
