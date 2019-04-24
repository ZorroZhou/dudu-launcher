package com.wow.carlauncher.repertory.nio.warp;

import android.util.Log;

/**
 * 客户端包的转换器,用来将byte数组转换成包
 */
public class WarpConverter {
    private final static String TAG = "WarpConverter";

    public BaseWarp bytes2Warp(String msg) {
        if (msg == null || msg.length() < 2) {
            return null;
        }
        BaseWarp warp = null;
        String cmd = msg.substring(0, 2);
        switch (cmd) {
            default:
                break;
        }
        Log.d(TAG, "解码出的包为:" + warp);
        return warp;
    }
}
