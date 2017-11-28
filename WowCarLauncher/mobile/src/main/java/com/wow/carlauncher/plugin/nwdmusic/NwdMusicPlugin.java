package com.wow.carlauncher.plugin.nwdmusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.plugin.common.music.MusicComPlugin;
import com.wow.carlauncher.plugin.common.music.MusicController;

/**
 * Created by 10124 on 2017/10/26.
 */

public class NwdMusicPlugin extends MusicComPlugin implements MusicController {
    private static final int CMD_NEXT = 3;
    private static final int CMD_PAUSE = 5;
    private static final int CMD_PRE = 2;
    private static final int CMD_RESUME = 1;

    public NwdMusicPlugin(Context context, PluginManage pluginManage) {
        super(context, pluginManage);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.music.queuechanged");
        intentFilter.addAction("com.android.music.metachanged");
        intentFilter.addAction("com.android.music.playstatechanged");
        this.context.registerReceiver(mReceiver, intentFilter);

    }

    public void play() {
        sendEvent(CMD_RESUME);
    }

    public void pause() {
        sendEvent(CMD_PAUSE);
    }

    public void next() {
        sendEvent(CMD_NEXT);
    }

    public void pre() {
        sendEvent(CMD_PRE);
    }

    private void sendEvent(int event) {
        Intent localIntent = new Intent("com.nwd.ACTION_PLAY_COMMAND");
        localIntent.putExtra("extra_command", event);
        context.sendBroadcast(localIntent);
    }

    @Override
    public String getName() {
        return "NWD音乐";
    }

    @Override
    public void destroy() {
        super.destroy();
        context.unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent intent) {
            if ("com.android.music.queuechanged".equals(intent.getAction())
                    || "com.android.music.metachanged".equals(intent.getAction())
                    || "com.android.music.playstatechanged".equals(intent.getAction())) {
                String title = intent.getStringExtra("track");
                String artist = intent.getStringExtra("artist");
                refreshInfo(title, artist);
                boolean playing = intent.getBooleanExtra("playing", false);
                refreshState(playing);
            }
        }
    };
}
