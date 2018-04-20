package com.wow.carlauncher.plugin.console.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.wow.carlauncher.plugin.console.ConsoleListener;
import com.wow.carlauncher.plugin.console.ConsoleProtocl;

/**
 * Created by 10124 on 2017/11/9.
 */

public class NwdConsoleImpl extends ConsoleProtocl {
    public static final String ACTION_BT_BEGIN_CALL_ONLINE = "com.bt.ACTION_BT_BEGIN_CALL_ONLINE";
    public static final String ACTION_BT_END_CALL = "com.bt.ACTION_BT_END_CALL";
    public static final String ACTION_BT_INCOMING_CALL = "com.bt.ACTION_BT_INCOMING_CALL";
    public static final String ACTION_BT_OUTGOING_CALL = "com.bt.ACTION_BT_OUTGOING_CALL";

    public static final int MARK = 1;

    private Intent mSetVolumeIntent = new Intent("com.nwd.action.ACTION_KEY_VALUE");
    private Intent mMuteIntent = new Intent("com.nwd.action.ACTION_SET_MUTE");
    private boolean mute = false;

    public NwdConsoleImpl(Context context, final ConsoleListener listener) {
        super(context, listener);

        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction(ACTION_BT_END_CALL);
        localIntentFilter.addAction(ACTION_BT_BEGIN_CALL_ONLINE);
        localIntentFilter.addAction(ACTION_BT_INCOMING_CALL);
        localIntentFilter.addAction(ACTION_BT_OUTGOING_CALL);
        context.registerReceiver(callBroadcastReceiver, localIntentFilter);
    }

    private BroadcastReceiver callBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_BT_BEGIN_CALL_ONLINE.equals(intent.getAction())) {
                listener.callState(true);
            } else if (ACTION_BT_INCOMING_CALL.equals(intent.getAction())) {
                listener.callState(true);
            } else if (ACTION_BT_OUTGOING_CALL.equals(intent.getAction())) {
                listener.callState(true);
            } else if (ACTION_BT_END_CALL.equals(intent.getAction())) {
                listener.callState(false);
            }
        }
    };

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
            this.mMuteIntent.putExtra("extra_mute", false);
            this.context.sendBroadcast(this.mMuteIntent);
            mute = false;
        } else {
            this.mMuteIntent.putExtra("extra_mute", true);
            this.context.sendBroadcast(this.mMuteIntent);
            mute = true;
        }
    }

    @Override
    public void clearTask() {

    }

    @Override
    public void callAnswer() {
        this.mSetVolumeIntent.putExtra("extra_key_value", (byte) 21);
        this.context.sendBroadcast(this.mSetVolumeIntent);
    }

    @Override
    public void callHangup() {
        this.mSetVolumeIntent.putExtra("extra_key_value", (byte) 22);
        this.context.sendBroadcast(this.mSetVolumeIntent);
    }

    @Override
    public void destroy() {
        context.unregisterReceiver(callBroadcastReceiver);
    }
}
