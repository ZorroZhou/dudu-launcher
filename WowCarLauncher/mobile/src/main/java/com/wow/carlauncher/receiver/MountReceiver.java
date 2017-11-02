package com.wow.carlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wow.carlauncher.service.MainService;

/**
 * Created by 10124 on 2017/11/2.
 */

public class MountReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())) {
            Intent startIntent = new Intent(context, MainService.class);
            context.startService(startIntent);
        } else if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
            Intent startIntent = new Intent(context, MainService.class);
            context.startService(startIntent);
        }
    }
}
