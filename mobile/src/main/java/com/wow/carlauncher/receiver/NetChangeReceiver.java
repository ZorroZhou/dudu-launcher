package com.wow.carlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.wow.carlauncher.view.event.EventNetStateChange;
import com.wow.carlauncher.view.event.EventWifiState;
import com.wow.carlauncher.common.util.NetWorkUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 10124 on 2017/10/31.
 */

public class NetChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            // 接口回调传过去状态的类型
            if (NetWorkUtil.isWifiConnected(context)) {
                EventBus.getDefault().post(new EventWifiState().setUsable(true));
            } else {
                EventBus.getDefault().post(new EventWifiState().setUsable(false));
            }
            EventBus.getDefault().post(new EventNetStateChange());
        }
    }
}
