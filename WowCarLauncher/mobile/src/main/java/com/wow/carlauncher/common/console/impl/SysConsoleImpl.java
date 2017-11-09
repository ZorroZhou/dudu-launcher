package com.wow.carlauncher.common.console.impl;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.console.IConsole;
import com.wow.carlauncher.common.util.AppUtil;

/**
 * Created by 10124 on 2017/11/9.
 */

public class SysConsoleImpl extends IConsole {
    public static final String TAG = "SysConsoleImpl";
    public static final int MARK = 0;

    public SysConsoleImpl(Context context) {
        super(context);
    }

    @Override
    public void decVolume() {
        Log.e(TAG, "decVolume");
        AppUtil.sendKeyCode(KeyEvent.KEYCODE_VOLUME_DOWN);
    }

    @Override
    public void incVolume() {
        Log.e(TAG, "incVolume");
        AppUtil.sendKeyCode(KeyEvent.KEYCODE_VOLUME_UP);
    }

    @Override
    public void mute() {
        Log.e(TAG, "mute");
        AppUtil.sendKeyCode(KeyEvent.KEYCODE_VOLUME_MUTE);
    }
}
