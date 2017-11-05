package com.wow.carlauncher.plugin.amapcar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.plugin.IPlugin;

import java.util.Map;

/**
 * Created by 10124 on 2017/11/4.
 */

public class AMapCarPlugin implements IPlugin {
    protected Context context;
    private RelativeLayout launcherView;

    public AMapCarPlugin(Context context) {
        this.context = context;
    }

    @Override
    public View getLauncherView() {
        if (launcherView == null) {
            launcherView = (RelativeLayout) View.inflate(context, R.layout.plugin_amap_lanncher, null);
        }
        return launcherView;
    }

    @Override
    public View getPopupView() {
        return null;
    }

    @Override
    public void destroy() {

    }

    private void sendReceiver(int key, Map<String, Object> param) {
        Intent intent = new Intent();
        intent.setAction(AMapCarConstant.SEND_ACTION);
        intent.putExtra(AMapCarConstant.KEY_TYPE, key);
        intent.putExtra("SOURCE_APP", "车机优化");
        for (String k : param.keySet()) {
            Object value = param.get(k);
            if (value instanceof Integer) {
                intent.putExtra(k, (Integer) value);
            } else if (value instanceof Double) {
                intent.putExtra(k, (Double) value);
            } else if (value instanceof String) {
                intent.putExtra(k, (String) value);
            }
        }
        context.sendBroadcast(intent);
    }

    private BroadcastReceiver amapReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(AMapCarConstant.RECEIVE_ACTION)) {
                int key = intent.getIntExtra(AMapCarConstant.KEY_TYPE, -1);
                switch (key) {
                    case AMapCarConstant.RESPONSE_DISTRICT: {
                        intent.getStringExtra(AMapCarConstant.RESPONSE_DISTRICT_PRVINCE_NAME);
                        intent.getStringExtra(AMapCarConstant.RESPONSE_DISTRICT_CITY_NAME);
                        break;
                    }
                }
            }
        }
    };
}
