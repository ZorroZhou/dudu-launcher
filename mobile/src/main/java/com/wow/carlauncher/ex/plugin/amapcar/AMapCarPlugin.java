package com.wow.carlauncher.ex.plugin.amapcar;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventState;
import com.wow.frame.util.AppUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.*;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCartReceiver.GETHC_NEXT_TO_NAVI;

/**
 * Created by 10124 on 2017/11/4.
 */

public class AMapCarPlugin extends ContextEx {
    public static final String TAG = "AMapCarPlugin";
    private static AMapCarPlugin self;

    public static AMapCarPlugin self() {
        if (self == null) {
            self = new AMapCarPlugin();
        }
        return self;
    }

    private long lastHeartbeatTime = 0;

    private AMapCarPlugin() {

    }

    public void init(Context context) {
        setContext(context);
        amapReceiver = new AMapCartReceiver(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_ACTION);
        context.registerReceiver(amapReceiver, intentFilter);

        EventBus.getDefault().register(this);
    }

    private AMapCartReceiver amapReceiver;

    public void naviToHome() {
        if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
            Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put(REQUEST_GO_HC_DEST, REQUEST_GO_HC_DEST_HOME);
        param.put("IS_START_NAVI", 0);
        sendReceiver(REQUEST_GO_HC, param);
    }

    public void exitNav() {
        if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
            Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
            return;
        }
        sendReceiver(REQUEST_EXIT_NAV, null);
    }

    public void mute(boolean mute) {
        if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
            Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> param = new HashMap<>();
        if (mute) {
            param.put(REQUEST_SET_NAVI_MUTE_MARK, 1);
        } else {
            param.put(REQUEST_SET_NAVI_MUTE_MARK, 0);
            param.put(REQUEST_SET_NAVI_CASUAL_MUTE_MARK, 0);
        }
        sendReceiver(REQUEST_SET_NAVI_MUTE, param);
    }

    public void getMute() {
        if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
            Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
            return;
        }
        sendReceiver(REQUEST_GET_NAVI_MUTE, null);
    }


    public void getHome() {
        if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
            Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
            return;
        }

        amapReceiver.setGetHcNext(GETHC_NEXT_TO_NAVI);

        Map<String, Object> param = new HashMap<>();
        param.put(EXTRA_TYPE, REQUEST_GETHC_EXTRA_TYPE_HOME);
        sendReceiver(REQUEST_GETHC, param);
    }

    public void naviToComp() {
        if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
            Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> param = new HashMap<>();
        param.put(REQUEST_GO_HC_DEST, REQUEST_GO_HC_DEST_COMP);
        param.put("IS_START_NAVI", 0);
        sendReceiver(REQUEST_GO_HC, param);
    }

    public void getComp() {
        if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
            Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
            return;
        }

        amapReceiver.setGetHcNext(GETHC_NEXT_TO_NAVI);

        Map<String, Object> param = new HashMap<>();
        param.put(EXTRA_TYPE, REQUEST_GETHC_EXTRA_TYPE_COMP);
        sendReceiver(REQUEST_GETHC, param);
    }

    void toBack() {
        sendReceiver(REQUEST_GO_BACK, null);
    }

    private void sendReceiver(int key, Map<String, Object> param) {
        Log.e(TAG, "sendReceiver: " + key + " " + param);
        Intent intent = new Intent();
        intent.setAction(SEND_ACTION);
        intent.putExtra(KEY_TYPE, key);
        intent.putExtra("SOURCE_APP", "com.wow.carlauncher");
        if (param != null) {
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
        }
        getContext().sendBroadcast(intent);
    }

    public void noticeHeartbeatTime() {
        lastHeartbeatTime = System.currentTimeMillis();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final MTimeSecondEvent event) {
        //3分钟没有收到心跳，则结束导航
        if (System.currentTimeMillis() - lastHeartbeatTime > 1000 * 60 * 3) {
            EventBus.getDefault().post(new PAmapEventState().setRunning(false));
        }
    }
}
