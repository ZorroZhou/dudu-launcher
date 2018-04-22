package com.wow.carlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wow.carlauncher.activity.launcher.LauncherActivity;
import com.wow.carlauncher.service.MainService;

/**
 * Created by 10124 on 2017/10/31.
 */

public class BootReceiver extends BroadcastReceiver {
    private static boolean bootSuccess=false;
    @Override
    public void onReceive(final Context context, Intent intent) {
        if(!bootSuccess){
            bootSuccess=true;
            Intent startIntent = new Intent(context, MainService.class);
            context.startService(startIntent);

            Intent intent2 = new Intent(context, LauncherActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
        }
    }
}
