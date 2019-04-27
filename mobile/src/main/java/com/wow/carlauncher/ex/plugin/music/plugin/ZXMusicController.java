package com.wow.carlauncher.ex.plugin.music.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.ex.plugin.music.MusciCoverUtil;
import com.wow.carlauncher.ex.plugin.music.MusicController;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;

import static com.wow.carlauncher.common.CommonData.TAG;

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
        intentFilter2.addAction("cupdate.widget.songname");
        this.context.registerReceiver(mReceiver, intentFilter2);
    }

    @Override
    public String clazz() {
        return "com.nwd.android.music.ui";
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


//    Intent localIntent = new Intent("update.widget.songname");
//    localIntent.putExtra("curplaysong", paramString1);
//    localIntent.putExtra("PlayState", paramBoolean);
//    localIntent.putExtra("curplaysongpath", paramString2);
//    localIntent.putExtra("curMusicIndex", getPlayList().getCurPlayIndex());
//    localIntent.putExtra("totalMusicCount", getPlayList().size());
//    localIntent.putExtra("artistPicPath", getArtistImagePath(paramString1));

//    Intent localIntent = new Intent("update.widget.playbtnstate");
//    localIntent.putExtra("PlayState", false);

//    Intent localIntent = new Intent("update.widget.update_proBar");
//    localIntent.putExtra("proBarmax", paramInt1);
//    localIntent.putExtra("proBarvalue", paramInt2);
//    localIntent.putExtra("curplaytime", StringUtils.generateTime(paramLong));
//    localIntent.putExtra("isExistMusicApp", paramBoolean);
//    localIntent.putExtra("curplaysong", paramString);
//    localIntent.putExtra("artistPicPath", getArtistImagePath(paramString));

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent intent) {
            if ("update.widget.playbtnstate".equals(intent.getAction())) {
                try {
                    boolean playing = intent.getBooleanExtra("PlayState", false);
                    musicPlugin.refreshState(playing, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("update.widget.songname".equals(intent.getAction())) {
                try {
                    String curplaysong = intent.getStringExtra("curplaysong");
                    musicPlugin.refreshInfo(curplaysong, "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                    musicPlugin.refreshInfo(intent.getStringExtra("curplaysong"), "");
                    musicPlugin.refreshProgress(proBarvalue, proBarmax);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
