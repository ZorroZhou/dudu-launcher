package com.wow.carlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.service.MainService;

/**
 * Created by 10124 on 2017/10/31.
 */

public class BootReceiver extends BroadcastReceiver {
    private static boolean bootSuccess = false;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (!bootSuccess) {
            bootSuccess = true;
            Intent startIntent = new Intent(context, MainService.class);
            context.startService(startIntent);

            TaskExecutor.self().run(() -> {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.addCategory(Intent.CATEGORY_HOME);
                home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(home);
            }, 1000);
        }
    }
}
