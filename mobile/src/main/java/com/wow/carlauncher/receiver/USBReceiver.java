package com.wow.carlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wow.carlauncher.view.event.EventUsbMount;

import org.greenrobot.eventbus.EventBus;

public class USBReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.MEDIA_MOUNTED".equals(intent.getAction())) {
            EventBus.getDefault().post(new EventUsbMount().setMount(true));
        } else if ("android.intent.action.MEDIA_UNMOUNTED".equals(intent.getAction())) {
            EventBus.getDefault().post(new EventUsbMount().setMount(false));
        }
    }
}
