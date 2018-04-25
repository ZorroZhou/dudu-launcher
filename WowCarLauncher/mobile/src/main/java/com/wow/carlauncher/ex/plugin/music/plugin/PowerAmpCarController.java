package com.wow.carlauncher.ex.plugin.music.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;

import com.wow.carlauncher.ex.plugin.music.MusicController;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;

public class PowerAmpCarController extends MusicController {

    private static final int NEXT = 4;
    private static final int TOGGLE_PLAY_PAUSE = 1;
    private static final int PREVIOUS = 5;

    public void init(Context context, MusicPlugin musicView) {
        super.init(context, musicView);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.maxmpz.audioplayer.STATUS_CHANGED");
        intentFilter.addAction("com.maxmpz.audioplayer.PLAYING_MODE_CHANGED");
        intentFilter.addAction("com.maxmpz.audioplayer.TRACK_CHANGED");
        intentFilter.addAction("com.maxmpz.audioplayer.AA_CHANGED");
        intentFilter.addAction("com.android.music.playstatechanged");
        this.context.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void play() {
        context.startService(new Intent("com.maxmpz.audioplayer.API_COMMAND").setPackage("com.maxmpz.audioplayer").putExtra("cmd", TOGGLE_PLAY_PAUSE));
    }

    @Override
    public void pause() {
        context.startService(new Intent("com.maxmpz.audioplayer.API_COMMAND").setPackage("com.maxmpz.audioplayer").putExtra("cmd", TOGGLE_PLAY_PAUSE));
    }

    @Override
    public void next() {
        context.startService(new Intent("com.maxmpz.audioplayer.API_COMMAND").setPackage("com.maxmpz.audioplayer").putExtra("cmd", NEXT));
    }

    @Override
    public void pre() {
        context.startService(new Intent("com.maxmpz.audioplayer.API_COMMAND").setPackage("com.maxmpz.audioplayer").putExtra("cmd", PREVIOUS));
    }

    @Override
    public void destroy() {
        context.unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent intent) {
            if ("com.maxmpz.audioplayer.STATUS_CHANGED" .equals(intent.getAction())) {
                musicView.refreshState(!intent.getBooleanExtra("paused", true));
            } else if ("com.android.music.playstatechanged" .equals(intent.getAction())) {
                String title = intent.getStringExtra("track");
                String artist = intent.getStringExtra("artist");
                musicView.refreshState(intent.getBooleanExtra("playing", false));
                musicView.refreshInfo(title, artist);
            } else if ("com.maxmpz.audioplayer.AA_CHANGED" .equals(intent.getAction())) {
                Bitmap cover = (Bitmap) intent.getExtras().get("aaBitmap");
                musicView.refreshCover(cover);
            }
        }
    };
}
