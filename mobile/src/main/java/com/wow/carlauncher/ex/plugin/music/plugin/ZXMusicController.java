package com.wow.carlauncher.ex.plugin.music.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.plugin.music.MusciCoverUtil;
import com.wow.carlauncher.ex.plugin.music.MusicController;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;

/**
 * Created by 10124 on 2017/10/26.
 */

public class ZXMusicController extends MusicController {
    private static final String CMD_NEXT = "xy.android.nextmedia";
    private static final String CMD_PAUSE_OR_PLAY = "xy.android.playpause";
    private static final String CMD_PRE = "xy.android.previousmedia";


    public void init(Context context, MusicPlugin musicView) {
        super.init(context, musicView);

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("update.widget.playbtnstate");
        intentFilter2.addAction("update.widget.update_proBar");
        this.context.registerReceiver(mReceiver, intentFilter2);

        if (SharedPreUtil.getBoolean(CommonData.SDATA_MUSIC_AUTO_RUN, false)) {
            TaskExecutor.self().run(this::play, 1000);
        }
    }

    @Override
    public String name() {
        return "掌讯音乐";
    }

    @Override
    public String clazz() {
        return "com.acloud.stub.localmusic";
    }

    public void play() {
        sendEvent(CMD_PAUSE_OR_PLAY);
    }

    public void pause() {
        sendEvent(CMD_PAUSE_OR_PLAY);
    }

    public void next() {
        sendEvent(CMD_NEXT);
    }

    public void pre() {
        sendEvent(CMD_PRE);
    }

    private void sendEvent(String event) {
        Intent localIntent = new Intent(event);
        context.sendBroadcast(localIntent);
    }

    @Override
    public void destroy() {
        context.unregisterReceiver(mReceiver);
    }

    private String lastTitle = "";
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent intent) {
            if ("update.widget.playbtnstate".equals(intent.getAction())) {
                try {
                    boolean playing = intent.getBooleanExtra("PlayState", false);
                    musicPlugin.refreshState(playing, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("update.widget.update_proBar".equals(intent.getAction())) {
                try {
                    int proBarmax = intent.getIntExtra("proBarmax", 0);
                    int proBarvalue = intent.getIntExtra("proBarvalue", 0);
                    String title = intent.getStringExtra("curplaysong");
                    musicPlugin.refreshState(true, true);
                    musicPlugin.refreshProgress(proBarvalue, proBarmax);
                    if (CommonUtil.isNotNull(title) && !title.equals(lastTitle)) {
                        lastTitle = title;
                        musicPlugin.refreshInfo(lastTitle, "", false);
                        String xx = intent.getStringExtra("artistPicPath");
                        if (CommonUtil.isNotNull(xx)) {
                            musicPlugin.refreshCover("file:/" + xx);
                        } else {
                            MusciCoverUtil.loadCover(title, null, musicPlugin);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
