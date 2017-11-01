package com.wow.carlauncher.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        Object localObject2 = new Intent("android.intent.action.MEDIA_BUTTON");
//        List paramBundle = new ArrayList();
//        Object localObject1 = getPackageManager();
//        localObject2 = ((PackageManager) localObject1).queryBroadcastReceivers((Intent) localObject2, 0);
//        int j = ((List) localObject2).size();
//        int i = 0;
//        while (i < j) {
//            ResolveInfo localResolveInfo = (ResolveInfo) ((List) localObject2).get(i);
//            if ((!localResolveInfo.activityInfo.name.equals("com.spotify.music.internal.receiver.VideoMediaButtonReceiver")) && (!localResolveInfo.activityInfo.name.equals("com.sec.factory.app.factorytest.MediaButtonIntentReceiver")) && (!localResolveInfo.activityInfo.name.equals("flipboard.service.audio.MediaPlayerService$MusicIntentReceiver")) && (!localResolveInfo.activityInfo.name.equals("com.sec.android.app.mediasync.receiver.RemoteControlReceiver")) && (!localResolveInfo.activityInfo.name.equals("com.sec.android.app.videoplayer.receiver.VideoBtReceiver")) && (!localResolveInfo.activityInfo.name.equals("com.sec.android.app.voicerecorder.util.MediaButtonReceiver")) && (!localResolveInfo.activityInfo.name.equals("com.google.apps.dots.android.newsstand.audio.MediaButtonIntentReceiver")) && (!localResolveInfo.activityInfo.name.equals("com.sec.android.mmapp.RemoteControlReceiver")) && (!localResolveInfo.activityInfo.name.equals("com.infraware.uxcontrol.voice.module.AudioHWKeyReceiver")) && (!localResolveInfo.activityInfo.name.equals("com.flixster.android.cast.CastBroadcastReceiver")) && (!localResolveInfo.activityInfo.name.equals("com.samsung.music.media.MediaButtonReceiver")) && (!localResolveInfo.activityInfo.packageName.equals("com.gotv.crackle.handset")) && (!localResolveInfo.activityInfo.packageName.equals("air.com.vudu.air.DownloaderTablet")) && (!localResolveInfo.activityInfo.packageName.equals("com.netflix.mediaclient")) && (!localResolveInfo.activityInfo.packageName.equals("com.hulu.plus")) && (!localResolveInfo.activityInfo.packageName.startsWith("com.opera")) && (!localResolveInfo.activityInfo.packageName.equals("com.yidio.androidapp")) && (!localResolveInfo.activityInfo.packageName.startsWith("com.kober.head")) && (!localResolveInfo.activityInfo.name.equals("com.plexapp.plex.audioplayer.AudioIntentReceiver")) && (!localResolveInfo.activityInfo.packageName.equals("com.mxtech.videoplayer.ad")) && (!localResolveInfo.activityInfo.packageName.equals("com.sonyericsson.video")) && (!localResolveInfo.activityInfo.packageName.equals("com.google.android.videos")) && (!localResolveInfo.activityInfo.packageName.equals("com.android.chrome")) && (!localResolveInfo.activityInfo.packageName.equals("com.estrongs.android.pop")) && (!localResolveInfo.activityInfo.packageName.equals("com.lonelycatgames.Xplore"))) {
//                Log.e("LOG_TAG", "app " + localResolveInfo.activityInfo.packageName);
//                Log.e("LOG_TAG", "class " + localResolveInfo.activityInfo.name);
//            }
//            i += 1;
//        }


        startActivity(new Intent(this, LanncherActivity.class));
        finish();
//        ((ToggleButton) findViewById(R.id.toggleButton)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    musicPlugin = new NeteaseCloudMusicPlugin();
//                } else {
//                    musicPlugin = new QQMusicCarPlugin();
//                }
//                musicPlugin.create(MainActivity.this, null);
//            }
//        });
//
//        musicPlugin.create(this, null);
//
//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                musicPlugin.play();
//            }
//        });
//        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                musicPlugin.pause();
//            }
//        });
//        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                musicPlugin.pre();
//            }
//        });
//        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                musicPlugin.next();
//            }
//        });

    }
}
