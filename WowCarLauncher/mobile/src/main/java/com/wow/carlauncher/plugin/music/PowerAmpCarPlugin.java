package com.wow.carlauncher.plugin.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;

import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.plugin.music.common.MusicComPlugin;
import com.wow.carlauncher.plugin.music.common.MusicController;

public class PowerAmpCarPlugin extends MusicComPlugin implements MusicController {

    private static final int NEXT = 4;
    private static final int TOGGLE_PLAY_PAUSE = 1;
    private static final int PREVIOUS = 5;

    public PowerAmpCarPlugin(Context context, PluginManage pluginManage) {
        super(context, pluginManage);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.maxmpz.audioplayer.STATUS_CHANGED");
        intentFilter.addAction("com.maxmpz.audioplayer.PLAYING_MODE_CHANGED");
        intentFilter.addAction("com.maxmpz.audioplayer.TRACK_CHANGED");
        intentFilter.addAction("com.maxmpz.audioplayer.AA_CHANGED");
        intentFilter.addAction("com.android.music.playstatechanged");
        this.context.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public String getName() {
        return "PowerAmp";
    }

    public void play() {
        context.startService(new Intent("com.maxmpz.audioplayer.API_COMMAND").setPackage("com.maxmpz.audioplayer").putExtra("cmd", TOGGLE_PLAY_PAUSE));
    }

    public void pause() {
        context.startService(new Intent("com.maxmpz.audioplayer.API_COMMAND").setPackage("com.maxmpz.audioplayer").putExtra("cmd", TOGGLE_PLAY_PAUSE));
    }

    public void next() {
        context.startService(new Intent("com.maxmpz.audioplayer.API_COMMAND").setPackage("com.maxmpz.audioplayer").putExtra("cmd", NEXT));
    }

    public void pre() {
        context.startService(new Intent("com.maxmpz.audioplayer.API_COMMAND").setPackage("com.maxmpz.audioplayer").putExtra("cmd", PREVIOUS));
    }

    @Override
    public void destroy() {
        super.destroy();
        context.unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent intent) {
            if ("com.maxmpz.audioplayer.STATUS_CHANGED".equals(intent.getAction())) {
                refreshState(!intent.getBooleanExtra("paused", true));
            } else if ("com.android.music.playstatechanged".equals(intent.getAction())) {
                String title = intent.getStringExtra("track");
                String artist = intent.getStringExtra("artist");
                refreshState(intent.getBooleanExtra("playing", false));
                refreshInfo(title, artist);
            } else if ("com.maxmpz.audioplayer.AA_CHANGED".equals(intent.getAction())) {
                Bitmap cover = (Bitmap) intent.getExtras().get("aaBitmap");
                refreshCover(cover);
            }
        }
    };
}
