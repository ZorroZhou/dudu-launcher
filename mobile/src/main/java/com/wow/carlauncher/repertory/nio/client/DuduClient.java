package com.wow.carlauncher.repertory.nio.client;

import com.wow.carlauncher.repertory.nio.base.NioClient;
import com.wow.carlauncher.repertory.nio.warp.WarpConverter;

import io.netty.channel.ChannelInboundHandlerAdapter;

public class DuduClient extends NioClient {
    private WarpConverter warpConverter;

    public DuduClient() {
        this.warpConverter = new WarpConverter();
    }

    @Override
    public WarpConverter getWarpConverter() {
        return warpConverter;
    }

    @Override
    public ChannelInboundHandlerAdapter getMessageHandler() {
        return null;
    }
}
