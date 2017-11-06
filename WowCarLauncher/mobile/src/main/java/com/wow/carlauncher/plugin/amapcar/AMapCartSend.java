package com.wow.carlauncher.plugin.amapcar;

import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import static com.wow.carlauncher.plugin.amapcar.AMapCarConstant.*;

/**
 * Created by 10124 on 2017/11/6.
 */

class AMapCartSend {
    private static final String TAG = "AMapCartReceiver";

    private AMapCarPlugin aMapCarPlugin;

    AMapCartSend(AMapCarPlugin aMapCarPlugin) {
        this.aMapCarPlugin = aMapCarPlugin;
    }

    void search(String text) {
        Map<String, Object> param = new HashMap<>();
        param.put(REQUEST_SEARCH_KEYWORDS, text);
        sendReceiver(REQUEST_SEARCH, param);
    }

    void naviToHome() {
        Map<String, Object> param = new HashMap<>();
        param.put(REQUEST_GO_HC_DEST, REQUEST_GO_HC_DEST_HOME);
        param.put("IS_START_NAVI", 0);
        sendReceiver(REQUEST_GO_HC, param);
    }

    void getHome() {
        Map<String, Object> param = new HashMap<>();
        param.put(EXTRA_TYPE, REQUEST_GETHC_EXTRA_TYPE_HOME);
        sendReceiver(REQUEST_GETHC, param);
    }

    void toHomeSet() {
        Map<String, Object> param = new HashMap<>();
        param.put(EXTRA_TYPE, REQUEST_SET_HC_HOME);
        sendReceiver(REQUEST_SET_HC, param);
    }

    void toCompSet() {
        Map<String, Object> param = new HashMap<>();
        param.put(EXTRA_TYPE, REQUEST_SET_HC_COMP);
        sendReceiver(REQUEST_SET_HC, param);
    }

    void naviToComp() {
        Map<String, Object> param = new HashMap<>();
        param.put(REQUEST_GO_HC_DEST, REQUEST_GO_HC_DEST_COMP);
        param.put("IS_START_NAVI", 0);
        sendReceiver(REQUEST_GO_HC, param);
    }

    void getComp() {
        Map<String, Object> param = new HashMap<>();
        param.put(EXTRA_TYPE, REQUEST_GETHC_EXTRA_TYPE_COMP);
        sendReceiver(REQUEST_GETHC, param);
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
        aMapCarPlugin.getContext().sendBroadcast(intent);
    }
}
