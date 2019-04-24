package com.wow.carlauncher.ex.plugin.music.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.ex.plugin.music.MusicController;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.repertory.db.entiy.CoverTemp;
import com.wow.carlauncher.repertory.db.manage.DatabaseManage;
import com.wow.carlauncher.repertory.web.qqmusic.QQMusicWebService;
import com.wow.carlauncher.repertory.web.qqmusic.res.SearchRes;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;

import static com.wow.carlauncher.common.CommonData.TAG;

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

    @Override
    public String clazz() {
        return "com.nwd.android.music.ui";
    }

    public void play() {
        sendEvent(CMD_RESUME);
        musicPlugin.refreshState(true, false);
    }

    public void pause() {
        sendEvent(CMD_PAUSE);
        musicPlugin.refreshState(false, false);
    }

    public void next() {
        sendEvent(CMD_NEXT);
        musicPlugin.refreshState(true, false);
    }

    public void pre() {
        sendEvent(CMD_PRE);
        musicPlugin.refreshState(true, false);
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
                    musicPlugin.refreshInfo(title, artist);
                    x.task().run(() -> {
                        CoverTemp temp = DatabaseManage.getBean(CoverTemp.class, " key='" + title + "-" + artist + "'");
                        if (temp == null) {
                            QQMusicWebService.searchMusic(title + " " + artist, 1, new QQMusicWebService.CommonCallback<SearchRes>() {
                                @Override
                                public void callback(final SearchRes res) {
                                    if (res != null &&
                                            res.getCode() == 0 &&
                                            res.getData() != null &&
                                            res.getData().getSong() != null &&
                                            res.getData().getSong().getList() != null &&
                                            res.getData().getSong().getList().size() > 0) {
                                        SearchRes.SongItem songItem = res.getData().getSong().getList().get(0);
                                        String url = QQMusicWebService.picUrl(songItem.getAlbumid());
                                        musicPlugin.refreshCover(url);
                                        DatabaseManage.insert(new CoverTemp().setKey(title + "-" + artist).setUrl(url));
                                    }
                                }
                            });
                        } else {
                            Bitmap cover = ImageManage.self().loadImageSync(temp.getUrl());
                            if (cover == null || cover.getWidth() < 10) {
                                musicPlugin.refreshCover(null);
                                DatabaseManage.delete(CoverTemp.class, " key='" + title + "-" + artist + "'");
                            } else {
                                musicPlugin.refreshCover(temp.getUrl());
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    boolean playing = intent.getBooleanExtra("playing", false);
                    musicPlugin.refreshState(playing, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
