package com.wow.carlauncher.repertory.nio.base;


import android.util.Log;

import com.google.common.base.Strings;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.repertory.nio.warp.BaseWarp;
import com.wow.carlauncher.repertory.nio.warp.WarpConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import static com.wow.carlauncher.repertory.nio.base.NioConnectState.CONNECTED;
import static com.wow.carlauncher.repertory.nio.base.NioConnectState.CONNECTING;
import static com.wow.carlauncher.repertory.nio.base.NioConnectState.LOGIN_SUCCESS;
import static com.wow.carlauncher.repertory.nio.base.NioConnectState.NOT_CONNECT;

/**
 * 基础的客户端，负责处理nio连接，等相关的功能，可自定义解析器
 */
public abstract class NioClient {
    private static final String TAG = "NIO客户端";
    private Channel channel;
    private NioConnectState connectState = NOT_CONNECT;

    protected void setConnectState(NioConnectState connectState) {
        this.connectState = connectState;
        listener.stateChange(connectState);
    }

    public NioConnectState getConnectState() {
        return connectState;
    }

    private ExecutorService singleThreadPool;
    protected NioClientListener listener;

    public void setListener(NioClientListener listener) {
        this.listener = listener;
    }

    public NioClient() {
        listener = new NioClientListenerImpl();
        singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1), new ThreadFactoryBuilder()
                .setNameFormat("client-thread-%d").build(), new ThreadPoolExecutor.AbortPolicy());
    }

    public void disConnect() {
        Log.d(TAG, "断开服务器连接");
        if (!(connectState != CONNECTED && connectState != LOGIN_SUCCESS)) {
            setConnectState(NOT_CONNECT);
        }
        if (channel != null) {
            channel.close();
            channel = null;
        }
    }

    public void connect() {
        if (connectState != NOT_CONNECT) {
            return;
        }
        setConnectState(CONNECTING);
        Log.d(TAG, "开始连接服务器");

        singleThreadPool.execute(() -> {
            final EventLoopGroup group = new NioEventLoopGroup();
            final Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    // 字符串解码 和 编码
                    pipeline.addLast(new LineBasedFrameDecoder(1024));
                    pipeline.addLast(new StringDecoder());
                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) {
                            if (msg != null) {
                                String body = msg.toString();
                                BaseWarp warp = getWarpConverter().bytes2Warp(body);
                                if (warp != null) {
                                    ctx.fireChannelRead(warp);
                                }
                            }
                        }
                    });
                    pipeline.addLast(new IdleStateHandler(30, 30, 30));
                    pipeline.addLast("messageHandler", getMessageHandler());
                }
            });
            try {
                String serverUrl = SharedPreUtil.getString(CommonData.SDATA_SERVER);
                if (Strings.isNullOrEmpty(serverUrl)) {
                    Log.e(TAG, "服务器地址未设置");
                    setConnectState(NOT_CONNECT);
                    return;
                }
                String[] ips = serverUrl.split(":");
                channel = b.connect(ips[0], Integer.parseInt(ips[1])).sync().channel();
                setConnectState(CONNECTED);
                Log.d(TAG, "服务器连接成功");
                channel.closeFuture().sync(); // 异步等待关闭连接channel
            } catch (Exception e) {
                Log.e(TAG, "连接服务器异常，可能是地址设置错误！" + e.getMessage());
                e.printStackTrace();
                channel = null;
                setConnectState(NOT_CONNECT);
                disConnect();
            } finally {
                group.shutdownGracefully();
            }
        });
    }

    public abstract WarpConverter getWarpConverter();

    public abstract ChannelInboundHandlerAdapter getMessageHandler();
}
