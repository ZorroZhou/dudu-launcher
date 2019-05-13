package com.wow.carlauncher.ex.plugin.music.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.KeyEvent;

import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.ex.plugin.music.MusicController;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemMusicController extends MusicController {
    private String PACKAGE_NAME = null;

    private Map<String, String> clazzs;

    public void init(Context context, MusicPlugin musicView) {
        super.init(context, musicView);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.music.metachanged");
        intentFilter.addAction("com.android.music.playstatechanged");
        intentFilter.addAction("com.android.music.playbackcomplete");
        intentFilter.addAction("com.android.music.queuechanged");
        intentFilter.addAction("com.htc.music.metachanged");
        intentFilter.addAction("fm.last.android.metachanged");
        intentFilter.addAction("fm.last.android.playbackpaused");
        intentFilter.addAction("com.sec.android.app.music.metachanged");
        intentFilter.addAction("com.nullsoft.winamp.metachanged");
        intentFilter.addAction("com.nullsoft.winamp.playstatechanged");
        intentFilter.addAction("com.amazon.mp3.metachanged");
        intentFilter.addAction("com.amazon.mp3.playstatechanged");
        intentFilter.addAction("com.miui.player.metachanged");
        intentFilter.addAction("com.miui.player.playstatechanged");
        intentFilter.addAction("com.real.IMP.metachanged");
        intentFilter.addAction("com.real.IMP.playstatechanged");
        intentFilter.addAction("com.rdio.android.metachanged");
        intentFilter.addAction("com.rdio.android.playstatechanged");
        intentFilter.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        intentFilter.addAction("com.samsung.sec.android.MusicPlayer.playstatechanged");
        intentFilter.addAction("com.andrew.apollo.metachanged");
        intentFilter.addAction("com.andrew.apollo.playstatechanged");
        intentFilter.addAction("com.htc.music.metachanged");
        intentFilter.addAction("com.htc.music.playstatechanged");
        intentFilter.addAction("com.lge.music.metachanged");
        intentFilter.addAction("com.meizu.media.music.metachanged");
        intentFilter.addAction("com.meizu.media.music.playstatechanged");
        intentFilter.addAction("com.tw.music.metachanged");
        intentFilter.addAction("com.tbig.playerprotrial.metachanged");
        intentFilter.addAction("com.tbig.playerpro.metachanged");
        intentFilter.addAction("com.tbig.playerprotrial.playstatechanged");
        intentFilter.addAction("com.tbig.playerpro.playstatechanged");
        intentFilter.addAction("com.musicplayer.music.metachanged");
        intentFilter.addAction("com.musicplayer.music.playstatechanged");
        intentFilter.addAction("com.jetappfactory.jetaudio.metachanged");
        intentFilter.addAction("com.jetappfactory.jetaudio.playstatechanged");
        intentFilter.addAction("com.music.player.mp3player.white.metachanged");
        intentFilter.addAction("com.music.player.mp3player.white.playstatechanged");
        intentFilter.addAction("com.amapps.media.music.metachanged");
        intentFilter.addAction("com.amapps.media.music.playstatechanged");
        intentFilter.addAction("music.search.player.mp3player.cut.music.metachanged");
        intentFilter.addAction("music.search.player.mp3player.cut.music.playstatechanged");
        intentFilter.addAction("com.jrtstudio.music.metachanged");
        intentFilter.addAction("com.jrtstudio.music.playstatechanged");
        intentFilter.addAction("com.musixmatch.android.lyrify.metachanged");
        intentFilter.addAction("com.musixmatch.android.lyrify.playstatechanged");
        intentFilter.addAction("soundbar.music.metachanged");
        intentFilter.addAction("soundbar.music.playstatechanged");
        intentFilter.addAction("com.soundcloud.android.metachanged");
        intentFilter.addAction("com.soundcloud.android.playback.playcurrent");
        intentFilter.addAction("com.pantech.app.music.metachanged");
        intentFilter.addAction("com.pantech.app.music.playstatechanged");
        intentFilter.addAction("com.neowiz.android.bugs.metachanged");
        intentFilter.addAction("com.neowiz.android.bugs.playstatechanged");
        intentFilter.addAction("com.vkontakte.android.metachanged");
        intentFilter.addAction("com.vkontakte.android.playstatechanged");
        intentFilter.addAction("com.apple.android.music.metachanged");
        intentFilter.addAction("com.apple.android.music.playstatechanged");
        intentFilter.addAction("com.rhapsody.playstatechanged");
        intentFilter.addAction("tunein.player.playbackstatechanged");
        intentFilter.addAction("tunein.player.metadatachanged");
        intentFilter.addAction("com.spotify.music.playbackstatechanged");
        intentFilter.addAction("com.spotify.music.metadatachanged");
        intentFilter.addAction("com.Project100Pi.themusicplayer.playstatechanged");
        intentFilter.addAction("com.Project100Pi.themusicplayer.metadatachanged");
        intentFilter.addAction("com.jetappfactory.jetaudioplus.playstatechanged");
        intentFilter.addAction("com.jetappfactory.jetaudioplus.metadatachanged");
        intentFilter.addAction("com.sonyericsson.music.metachanged");
        intentFilter.addAction("com.sonyericsson.music.playbackcontrol.ACTION_PLAYBACK_PLAY");
        intentFilter.addAction("com.sonyericsson.music.TRACK_COMPLETED");
        intentFilter.addAction("com.sonyericsson.music.playbackcomplete");
        intentFilter.addAction("com.sonyericsson.music.playstatechanged");
        intentFilter.addAction("com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED");
        intentFilter.addAction("com.sonyericsson.music.playbackcontrol.ACTION_PAUSED");
        intentFilter.addAction("soundbar.music.metachanged");
        intentFilter.addAction("com.dogsbark.noozy.metadatachanged");
        intentFilter.addAction("com.dogsbark.noozy.playstatechanged");
        intentFilter.addAction("com.kugou.android.auto.music.playstatechanged");
        intentFilter.addAction("com.kugou.android.auto.music.metachanged");
        intentFilter.addAction("com.kugou.android.music.metachanged");
        intentFilter.addAction("com.kugou.android.music.playstatechanged");
        this.context.registerReceiver(mReceiver, intentFilter);

        clazzs = new HashMap<>();
        Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryBroadcastReceivers(intent, 0);
        for (ResolveInfo resolveInfo : list) {
            LogEx.e(this, resolveInfo.activityInfo.name);
            if ((!resolveInfo.activityInfo.name.equals("com.spotify.music.internal.receiver.VideoMediaButtonReceiver")) &&
                    (!resolveInfo.activityInfo.name.equals("com.sec.factory.app.factorytest.MediaButtonIntentReceiver")) &&
                    (!resolveInfo.activityInfo.name.equals("flipboard.service.audio.MediaPlayerService$MusicIntentReceiver")) &&
                    (!resolveInfo.activityInfo.name.equals("com.sec.android.app.mediasync.receiver.RemoteControlReceiver")) &&
                    (!resolveInfo.activityInfo.name.equals("com.sec.android.app.videoplayer.receiver.VideoBtReceiver")) &&
                    (!resolveInfo.activityInfo.name.equals("com.sec.android.app.voicerecorder.util.MediaButtonReceiver")) &&
                    (!resolveInfo.activityInfo.name.equals("com.google.apps.dots.android.newsstand.audio.MediaButtonIntentReceiver")) &&
                    (!resolveInfo.activityInfo.name.equals("com.sec.android.mmapp.RemoteControlReceiver")) &&
                    (!resolveInfo.activityInfo.name.equals("com.infraware.uxcontrol.voice.module.AudioHWKeyReceiver")) &&
                    (!resolveInfo.activityInfo.name.equals("com.flixster.android.cast.CastBroadcastReceiver")) &&
                    (!resolveInfo.activityInfo.name.equals("com.samsung.music.media.MediaButtonReceiver")) &&
                    (!resolveInfo.activityInfo.packageName.equals("com.gotv.crackle.handset")) &&
                    (!resolveInfo.activityInfo.packageName.equals("air.com.vudu.air.DownloaderTablet")) &&
                    (!resolveInfo.activityInfo.packageName.equals("com.netflix.mediaclient")) &&
                    (!resolveInfo.activityInfo.packageName.equals("com.hulu.plus")) &&
                    (!resolveInfo.activityInfo.packageName.startsWith("com.opera")) &&
                    (!resolveInfo.activityInfo.packageName.equals("com.yidio.androidapp")) &&
                    (!resolveInfo.activityInfo.packageName.startsWith("com.kober.head")) &&
                    (!resolveInfo.activityInfo.name.equals("com.plexapp.plex.audioplayer.AudioIntentReceiver")) &&
                    (!resolveInfo.activityInfo.packageName.equals("com.mxtech.videoplayer.ad")) &&
                    (!resolveInfo.activityInfo.packageName.equals("com.sonyericsson.video")) &&
                    (!resolveInfo.activityInfo.packageName.equals("com.google.android.videos")) &&
                    (!resolveInfo.activityInfo.packageName.equals("com.android.chrome")) &&
                    (!resolveInfo.activityInfo.packageName.equals("com.estrongs.android.pop")) &&
                    (!resolveInfo.activityInfo.packageName.equals("com.lonelycatgames.Xplore"))) {
                clazzs.put(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
            }
        }
    }

    @Override
    public String name() {
        return "音乐";
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
        localObject.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(System.currentTimeMillis(), System.currentTimeMillis() + 1, 0, event, 0));
        context.sendOrderedBroadcast(localObject, null);

        localObject = new Intent(Intent.ACTION_MEDIA_BUTTON);
        localObject.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(System.currentTimeMillis(), System.currentTimeMillis() + 1, 1, event, 0));
        context.sendOrderedBroadcast(localObject, null);
    }

    @Override
    public void destroy() {
        context.unregisterReceiver(mReceiver);
    }

    @Override
    public String clazz() {
        return null;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent intent) {
            try {
                if (intent.getStringExtra("android.media.extra.PACKAGE_NAME") != null && clazzs.containsKey(intent.getStringExtra("android.media.extra.PACKAGE_NAME"))) {
                    PACKAGE_NAME = intent.getStringExtra("android.media.extra.PACKAGE_NAME");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogEx.e(SystemMusicController.this, "onReceive: " + intent.getAction());
            if (intent.getExtras() != null) {
                for (String key : intent.getExtras().keySet()) {
                    LogEx.e(SystemMusicController.this, key + ": " + intent.getExtras().get(key));
                }
            }


            if (CommonUtil.isNotNull(intent.getAction()) && intent.getAction().contains("playstatechanged")) {
                try {
                    Boolean playstate = intent.getBooleanExtra("playstate", false);
                    musicPlugin.refreshState(playstate, false);
                } catch (Exception e) {
                }
                try {
                    int playstate = intent.getIntExtra("playstate", -1);
                    if (playstate == 3 || playstate == 4) {
                        if (playstate == 3) {
                            musicPlugin.refreshState(true, false);
                        } else {
                            musicPlugin.refreshState(false, false);
                        }
                    }
                } catch (Exception e) {
                }

                try {
                    if (intent.getStringExtra("artist") != null && intent.getStringExtra("track") != null) {
                        musicPlugin.refreshInfo(intent.getStringExtra("track"), intent.getStringExtra("artist"), false);
                    }
                } catch (Exception e) {
                }
            }

        }
    };
}
