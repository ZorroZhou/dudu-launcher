package com.wow.carlauncher.plugin.music.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.wow.carlauncher.plugin.music.MusicController;
import com.wow.carlauncher.plugin.music.controllers.kuwo.KuwoMusicLauncherView;
import com.wow.carlauncher.plugin.music.controllers.kuwo.KuwoMusicPopupView;
import com.wow.carlauncher.plugin.music.event.PEventMusicInfoChange;
import com.wow.carlauncher.plugin.music.event.PEventMusicStateChange;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import java.util.List;
import java.util.Map;

import cn.kuwo.autosdk.api.KWAPI;

/**
 * Created by 10124 on 2017/10/26.
 */

public class KuwoMusicPlugin extends MusicController {
    private static final String PACKAGE_NAME = "com.tencent.qqmusiccar";
    private static final String CLASS_NAME = "com.tencent.qqmusiccar.app.reciver.BroadcastReceiverCenterForThird";

    public static final int WE_DRIVE_NEXT = 3;
    public static final int WE_DRIVE_PAUSE = 1;
    public static final int WE_DRIVE_PRE = 2;
    public static final int WE_DRIVE_RESUME = 0;

    private Gson gson;

    private KuwoMusicLauncherView launcherView;

    private KuwoMusicPopupView popupView;
    private KWAPI mKwApi;

    public KuwoMusicPlugin(Context context) {
        super(context);
        mKwApi = KWAPI.createKWAPI(context, "wow");
    }

    @Override
    public View getLauncherView() {
        if (launcherView == null) {
            launcherView = new KuwoMusicLauncherView(context, this);
        }
        return launcherView;
    }

    @Override
    public View getPopupView() {
        if (popupView == null) {
            popupView = new KuwoMusicPopupView(context, this);
        }
        return popupView;
    }

    public void play() {
        mKwApi.randomPlayMusic();
    }

    public void pause() {
        sendEvent(WE_DRIVE_PAUSE);
    }

    public void next() {
        sendEvent(WE_DRIVE_NEXT);
    }

    public void pre() {
        sendEvent(WE_DRIVE_PRE);
    }

    private void sendEvent(int event) {
        Intent intent = new Intent("com.tencent.qqmusiccar.action");
        intent.setClassName(PACKAGE_NAME, CLASS_NAME);
        intent.setData(Uri.parse("qqmusiccar://asdasd?action=20&m0=" + event));
        context.sendBroadcast(intent);
        refreshInfo();
    }

    private void refreshInfo() {
        Intent intent2 = new Intent("com.tencent.qqmusiccar.action");
        intent2.setClassName(PACKAGE_NAME, CLASS_NAME);
        intent2.setData(Uri.parse("qqmusiccar://asdasd?action=100"));
        context.sendBroadcast(intent2);
    }

    @Override
    public void destroy() {
        super.destroy();
        context.unregisterReceiver(mReceiver);
        if (launcherView.getParent() != null) {
            ((ViewGroup) launcherView.getParent()).removeView(launcherView);
        }
        if (popupView.getParent() != null) {
            ((ViewGroup) popupView.getParent()).removeView(popupView);
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent intent) {
            if (intent.getStringExtra("com.tencent.qqmusiccar.EXTRA_COMMAND_DATA") != null) {
                String value = intent.getStringExtra("com.tencent.qqmusiccar.EXTRA_COMMAND_DATA");
                try {
                    Map m = gson.fromJson(value, Map.class);
                    Map c = (Map) m.get("command");
                    if ("update_state".equals(c.get("method"))) {
                        Map d = (Map) c.get("data");
                        String title = d.get("key_title") + "";
                        List<Map> as = (List<Map>) d.get("key_artist");
                        String artist = "";
                        if (as != null && as.size() > 0) {
                            Map a = as.get(0);
                            artist = a.get("singer") + "";
                        }
                        int curr_time = ((Double) d.get("curr_time")).intValue();
                        int total_time = ((Double) d.get("total_time")).intValue();
                        EventBus.getDefault().post(new PEventMusicInfoChange(title, artist, curr_time, total_time));
                        Log.e("!!!!!!!!!!!!!!", "onReceive: " + d);
                        if (d.get("state") != null && (double) d.get("state") == 2) {
                            EventBus.getDefault().post(new PEventMusicStateChange(true));
                            x.task().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    refreshInfo();
                                }
                            }, 500);
                        } else {
                            EventBus.getDefault().post(new PEventMusicStateChange(false));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    };
}
