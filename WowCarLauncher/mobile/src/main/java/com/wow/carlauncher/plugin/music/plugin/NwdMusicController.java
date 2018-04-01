package com.wow.carlauncher.plugin.music.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.wow.carlauncher.plugin.music.MusicController;
import com.wow.carlauncher.plugin.music.MusicPlugin;
import com.wow.carlauncher.plugin.music.MusicPluginListener;

/**
 * Created by 10124 on 2017/10/26.
 */

public class NwdMusicController extends MusicController {
    private static final int CMD_NEXT = 3;
    private static final int CMD_PAUSE = 5;
    private static final int CMD_PRE = 2;
    private static final int CMD_RESUME = 1;

    public void init(Context context, MusicPlugin musicView) {
        super.init(context, musicView);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.music.queuechanged");
        intentFilter.addAction("com.android.music.metachanged");
        intentFilter.addAction("com.android.music.playstatechanged");
        this.context.registerReceiver(mReceiver, intentFilter);

    }

    public void play() {
        sendEvent(CMD_RESUME);
        musicView.refreshState(true);
    }

    public void pause() {
        sendEvent(CMD_PAUSE);
        musicView.refreshState(false);
    }

    public void next() {
        sendEvent(CMD_NEXT);
        sendEvent(CMD_RESUME);
        musicView.refreshState(true);
    }

    public void pre() {
        sendEvent(CMD_PRE);
        sendEvent(CMD_RESUME);
        musicView.refreshState(true);
    }

    private void sendEvent(int event) {
        Intent localIntent = new Intent("com.nwd.ACTION_PLAY_COMMAND");
        localIntent.putExtra("extra_command", event);
        context.sendBroadcast(localIntent);
    }

    @Override
    public void destroy() {
        context.unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent intent) {
            if ("com.android.music.queuechanged".equals(intent.getAction())
                    || "com.android.music.metachanged".equals(intent.getAction())
                    || "com.android.music.playstatechanged".equals(intent.getAction())) {
                try {
                    String title = intent.getStringExtra("track");
                    String artist = intent.getStringExtra("artist");
                    musicView.refreshInfo(title, artist);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    boolean playing = intent.getBooleanExtra("playing", false);
                    musicView.refreshState(playing);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
