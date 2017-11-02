package com.wow.carlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.wow.carlauncher.service.MainService;

import org.xutils.x;

/**
 * Created by 10124 on 2017/10/31.
 */

public class BootReceiver extends BroadcastReceiver {
    private static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (BOOT_COMPLETED.equals(intent.getAction())) {
            Intent startIntent = new Intent(context, MainService.class);
            context.startService(startIntent);
        }
    }
}
