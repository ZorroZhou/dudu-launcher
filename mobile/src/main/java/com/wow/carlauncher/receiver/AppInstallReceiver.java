package com.wow.carlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;

/**
 * Created by 10124 on 2017/11/13.
 */

public class AppInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED) ||
                intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED) ||
                intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED))) {
            Log.e(CommonData.TAG, "onReceive:???????? ");
            AppInfoManage.self().refreshAppInfo();
        }
    }
}
