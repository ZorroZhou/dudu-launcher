package com.wow.carlauncher.ex.plugin.amapcar;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.wow.carlauncher.common.util.AppUtil;
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.AMAP_PACKAGE;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.EXTRA_TYPE;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.KEY_TYPE;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.RECEIVE_ACTION;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.REQUEST_EXIT_NAV;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.REQUEST_GETHC;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.REQUEST_GETHC_EXTRA_TYPE_COMP;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.REQUEST_GETHC_EXTRA_TYPE_HOME;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.REQUEST_GET_NAVI_MUTE;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.REQUEST_GO_BACK;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.REQUEST_GO_HC;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.REQUEST_GO_HC_DEST;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.REQUEST_GO_HC_DEST_COMP;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.REQUEST_GO_HC_DEST_HOME;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.REQUEST_SET_NAVI_CASUAL_MUTE_MARK;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.REQUEST_SET_NAVI_MUTE;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.REQUEST_SET_NAVI_MUTE_MARK;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.SEND_ACTION;
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


        // final View launcherWidgetView = AppWidgetManage.self().getWidgetById(launcher);
    }

    private AMapCartReceiver amapReceiver;

    public void testNavi() {
        Intent intent = new Intent();
        intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
        intent.putExtra("KEY_TYPE", 10076);
        intent.putExtra("EXTRA_SLAT", 24.496706);
        intent.putExtra("EXTRA_SLON", 118.182682);
        intent.putExtra("EXTRA_SNAME", "佰翔软件园酒店");
        intent.putExtra("EXTRA_FMIDLAT", 24.492793);
        intent.putExtra("EXTRA_FMIDLON", 118.162947);
        intent.putExtra("EXTRA_FMIDNAME", "蔡塘");
        intent.putExtra("EXTRA_SMIDLAT", 24.483256);
        intent.putExtra("EXTRA_SMIDLON", 118.148825);
        intent.putExtra("EXTRA_SMIDNAME", "太川大楼");
        intent.putExtra("EXTRA_TMIDLAT", 24.47658);
        intent.putExtra("EXTRA_TMIDLON", 118.163917);
        intent.putExtra("EXTRA_TMIDNAME", "世界山庄");
        intent.putExtra("EXTRA_DLAT", 24.453688);
        intent.putExtra("EXTRA_DLON", 118.17581);
        intent.putExtra("EXTRA_DNAME", "椰风寨");
        intent.putExtra("EXTRA_DEV", 0);
        intent.putExtra("EXTRA_M", 0);
        intent.putExtra("KEY_RECYLE_SIMUNAVI", true);
        getContext().sendBroadcast(intent);

        Intent intent2 = new Intent();
        intent2.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
        intent2.putExtra("KEY_TYPE", 10031);
        getContext().sendBroadcast(intent2);
    }

    public void naviToHome() {
        if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
            Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put(REQUEST_GO_HC_DEST, REQUEST_GO_HC_DEST_HOME);
        param.put("IS_START_NAVI", 0);
        sendReceiver(REQUEST_GO_HC, param);
        sendReceiver(REQUEST_GO_BACK, null);
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
        getMute();
    }

    public void getMute() {
        if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
            Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
            return;
        }
        sendReceiver(REQUEST_GET_NAVI_MUTE, null);
    }


//    public void getHome() {
//        if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
//            ToastManage.self().show("没有安装高德地图");
//            return;
//        }
//
//        amapReceiver.setGetHcNext(GETHC_NEXT_TO_NAVI);
//
//        Map<String, Object> param = new HashMap<>();
//        param.put(EXTRA_TYPE, REQUEST_GETHC_EXTRA_TYPE_HOME);
//        sendReceiver(REQUEST_GETHC, param);
//    }

    public void naviToComp() {
        if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
            Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> param = new HashMap<>();
        param.put(REQUEST_GO_HC_DEST, REQUEST_GO_HC_DEST_COMP);
        param.put("IS_START_NAVI", 0);
        sendReceiver(REQUEST_GO_HC, param);

        sendReceiver(REQUEST_GO_BACK, null);
    }

//    public void getComp() {
//        if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
//            Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        amapReceiver.setGetHcNext(GETHC_NEXT_TO_NAVI);
//
//        Map<String, Object> param = new HashMap<>();
//        param.put(EXTRA_TYPE, REQUEST_GETHC_EXTRA_TYPE_COMP);
//        sendReceiver(REQUEST_GETHC, param);
//    }

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

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final MTimeSecondEvent event) {
        //3分钟没有收到心跳，则结束导航
        if (System.currentTimeMillis() - lastHeartbeatTime > 1000 * 60 * 3) {
            EventBus.getDefault().post(new PAmapEventState().setRunning(false));
        }
    }
}
