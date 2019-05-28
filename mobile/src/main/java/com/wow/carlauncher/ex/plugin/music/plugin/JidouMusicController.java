package com.wow.carlauncher.ex.plugin.music.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;

import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.plugin.music.MusicController;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;

public class JidouMusicController extends MusicController {
    public void init(Context context, MusicPlugin musicView) {
        super.init(context, musicView);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.ijidou.action.UPDATE_PROGRESS");
        intentFilter.addAction("com.ijidou.card.music");
        this.context.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public String name() {
        return "极豆音乐";
    }

    public void play() {
        sendEvent(KeyEvent.KEYCODE_MEDIA_PLAY);
    }

    public void pause() {
        sendEvent(KeyEvent.KEYCODE_MEDIA_PAUSE);
    }

    public void next() {
        sendEvent(KeyEvent.KEYCODE_MEDIA_NEXT);
    }

    public void pre() {
        sendEvent(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
    }

    private void sendEvent(int event) {
        Intent localObject = new Intent(Intent.ACTION_MEDIA_BUTTON);
        localObject.setPackage("com.ijidou.music");
        localObject.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(System.currentTimeMillis(), System.currentTimeMillis() + 1, 0, event, 0));
        context.sendOrderedBroadcast(localObject, null);

        localObject = new Intent(Intent.ACTION_MEDIA_BUTTON);
        localObject.setPackage("com.ijidou.music");
        localObject.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(System.currentTimeMillis(), System.currentTimeMillis() + 1, 1, event, 0));
        context.sendOrderedBroadcast(localObject, null);
    }

    @Override
    public void destroy() {
        context.unregisterReceiver(mReceiver);
    }

    @Override
    public String clazz() {
        return "com.ijidou.card.music";
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        String music_title;
        String music_artist;

        public void onReceive(Context paramAnonymousContext, Intent intent) {
            if (intent.getAction().equals("com.ijidou.card.music")) {
                boolean music_status = intent.getBooleanExtra("music_status", false);
                musicPlugin.refreshState(music_status, false);
                music_artist = intent.getStringExtra("music_artist");
                music_title = intent.getStringExtra("music_title");
                if (music_title != null && music_artist != null) {
                    musicPlugin.refreshInfo(music_title, music_artist, false);
                }
            }
            if (intent.getAction().equals("com.ijidou.action.UPDATE_PROGRESS")) {
                int elapse = intent.getIntExtra("elapse", 0);
                int duration = intent.getIntExtra("duration", 0);
                musicPlugin.refreshProgress(elapse, duration);
            }
        }
    };
}
