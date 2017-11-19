package com.wow.carlauncher.plugin.qqcarmusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.plugin.pevent.PEventMusicInfoChange;
import com.wow.carlauncher.plugin.pevent.PEventMusicStateChange;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2017/10/26.
 */

public class QQMusicCarPlugin extends BasePlugin {
    private static final String PACKAGE_NAME = "com.tencent.qqmusiccar";
    private static final String CLASS_NAME = "com.tencent.qqmusiccar.app.reciver.BroadcastReceiverCenterForThird";

    private static final int WE_DRIVE_NEXT = 3;
    private static final int WE_DRIVE_PAUSE = 1;
    private static final int WE_DRIVE_PRE = 2;
    private static final int WE_DRIVE_RESUME = 0;

    private Gson gson;

    public QQMusicCarPlugin(Context context, PluginManage pluginManage) {
        super(context, pluginManage);
        gson = new Gson();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tencent.qqmusiccar.action.PLAY_COMMAND_SEND_FOR_THIRD");
        this.context.registerReceiver(mReceiver, intentFilter);

        startUpdate();
    }

    @Override
    public ViewGroup initLauncherView() {
        LauncherView launcherView = new LauncherView(context, this);
        return launcherView;
    }

    @Override
    public ViewGroup initPopupView() {
        PopupView view = new PopupView(context, this);
        return view;
    }

    void play() {
        sendEvent(WE_DRIVE_RESUME);
    }

    void pause() {
        sendEvent(WE_DRIVE_PAUSE);
    }

    void next() {
        sendEvent(WE_DRIVE_NEXT);
    }

    void pre() {
        sendEvent(WE_DRIVE_PRE);
    }

    private void sendEvent(int event) {
        Intent intent = new Intent("com.tencent.qqmusiccar.action");
        intent.setClassName(PACKAGE_NAME, CLASS_NAME);
        intent.setData(Uri.parse("qqmusiccar://asdasd?action=20&m0=" + event));
        context.sendBroadcast(intent);
    }

    private void refreshInfo() {
        waitMsg = true;
        Intent intent2 = new Intent("com.tencent.qqmusiccar.action");
        intent2.setClassName(PACKAGE_NAME, CLASS_NAME);
        intent2.setData(Uri.parse("qqmusiccar://asdasd?action=100"));
        context.sendBroadcast(intent2);
    }

    @Override
    public void destroy() {
        super.destroy();
        stopUpdate();
        context.unregisterReceiver(mReceiver);
    }

    private Timer timer;

    private void startUpdate() {
        stopUpdate();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshInfo();
            }
        }, 0, 1000);
    }

    private void stopUpdate() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private boolean waitMsg = false;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent intent) {
            if (intent.getStringExtra("com.tencent.qqmusiccar.EXTRA_COMMAND_DATA") != null && waitMsg) {
                waitMsg = false;
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
                        if (d.get("state") != null && (double) d.get("state") == 2) {
                            EventBus.getDefault().post(new PEventMusicStateChange(true));
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
