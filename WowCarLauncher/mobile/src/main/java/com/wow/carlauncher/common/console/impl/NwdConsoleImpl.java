package com.wow.carlauncher.common.console.impl;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import com.bt.BTController;
import com.bt.BTFeature;
import com.wow.carlauncher.common.console.IConsole;

/**
 * Created by 10124 on 2017/11/9.
 */

public class NwdConsoleImpl extends IConsole {
    public static final int MARK = 1;

    private Intent mSetVolumeIntent = new Intent("com.nwd.action.ACTION_KEY_VALUE");
    private Intent mMuteIntent = new Intent("com.nwd.action.ACTION_SET_MUTE");
    private boolean mute = false;

    private BTController controller;

    public NwdConsoleImpl(Context context) {
        super(context);
        controller = BTController.getInstance(context);
    }

    @Override
    public void decVolume() {
        this.mSetVolumeIntent.putExtra("extra_key_value", (byte) 15);
        this.context.sendBroadcast(this.mSetVolumeIntent);
    }

    @Override
    public void incVolume() {
        this.mSetVolumeIntent.putExtra("extra_key_value", (byte) 14);
        this.context.sendBroadcast(this.mSetVolumeIntent);
    }

    @Override
    public synchronized void mute() {
        if (mute) {
            this.mMuteIntent.putExtra("extra_mute", true);
            this.context.sendBroadcast(this.mMuteIntent);
            mute = false;
        } else {
            this.mMuteIntent.putExtra("extra_mute", false);
            this.context.sendBroadcast(this.mMuteIntent);
            mute = true;
        }
    }

    @Override
    public void clearTask() {

    }

    @Override
    public void callAnswer() {
        try {
            controller.getFeature().answerCall();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callHangup() {
        try {
            controller.getFeature().cancelCall();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
