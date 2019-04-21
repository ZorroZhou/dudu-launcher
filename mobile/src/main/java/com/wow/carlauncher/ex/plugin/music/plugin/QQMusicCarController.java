package com.wow.carlauncher.ex.plugin.music.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import com.google.gson.Gson;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.plugin.music.MusicController;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.common.util.GsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

/**
 * Created by 10124 on 2017/10/26.
 */

public class QQMusicCarController extends MusicController {
    private static final String PACKAGE_NAME = "com.tencent.qqmusiccar";
    private static final String CLASS_NAME = "com.tencent.qqmusiccar.app.reciver.BroadcastReceiverCenterForThird";

    private static final int WE_DRIVE_NEXT = 3;
    private static final int WE_DRIVE_PAUSE = 1;
    private static final int WE_DRIVE_PRE = 2;
    private static final int WE_DRIVE_RESUME = 0;

    private Gson gson;

    public void init(Context context, MusicPlugin musicView) {
        super.init(context, musicView);
        gson = GsonUtil.getGson();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tencent.qqmusiccar.action.PLAY_COMMAND_SEND_FOR_THIRD");
        intentFilter.addAction("com.android.music.playstatechanged");
        this.context.registerReceiver(mReceiver, intentFilter);

        EventBus.getDefault().register(this);
    }


    public void play() {
        sendEvent(WE_DRIVE_RESUME);
    }

    public void pause() {
        System.out.println("!!!!!!!!!");
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
    }

    private void refreshInfo() {
        if (waitMsg) {
            return;
        }
        waitMsg = true;
        Intent intent2 = new Intent("com.tencent.qqmusiccar.action");
        intent2.setClassName(PACKAGE_NAME, CLASS_NAME);
        intent2.setData(Uri.parse("qqmusiccar://asdasd?action=100"));
        context.sendBroadcast(intent2);
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
        context.unregisterReceiver(mReceiver);
    }

    private int index = 2;

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final MTimeSecondEvent event) {
        index++;
        if (index > 2) {
            index = 1;
            refreshInfo();
        }
    }

    private boolean waitMsg = false;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent intent) {
            try {
                if ("com.tencent.qqmusiccar.action.PLAY_COMMAND_SEND_FOR_THIRD".equals(intent.getAction()) && intent.getStringExtra("com.tencent.qqmusiccar.EXTRA_COMMAND_DATA") != null && waitMsg) {
                    waitMsg = false;
                    String value = intent.getStringExtra("com.tencent.qqmusiccar.EXTRA_COMMAND_DATA");
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
                        musicView.refreshInfo(title, artist);
                        musicView.refreshProgress(curr_time, total_time);
                        if (d.get("state") != null && (double) d.get("state") == 2) {
                            musicView.refreshState(true);
                        } else {
                            musicView.refreshState(false);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
