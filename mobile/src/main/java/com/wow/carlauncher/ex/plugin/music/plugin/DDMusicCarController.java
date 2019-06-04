package com.wow.carlauncher.ex.plugin.music.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.LrcAnalyze;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.AppUtil;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.time.event.TMEventSecond;
import com.wow.carlauncher.ex.plugin.music.MusciCoverUtil;
import com.wow.carlauncher.ex.plugin.music.MusicController;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import static com.wow.carlauncher.common.CommonData.SDATA_QQ_MUSIC_REG_DELAY;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.AMAP_PACKAGE;

/**
 * Created by 10124 on 2017/10/26.
 */

public class DDMusicCarController extends MusicController {
    private static final String PACKAGE_NAME = "com.wow.dudu.music";
    private static final String CLASS_NAME = "com.wow.dudu.music.receiver.MusicCmdReceiver";

    private static final String ACTION = "com.wow.dudu.music.cmd";

    private static final String CMD = "CMD";
    private static final int CMD_PLAY_OR_PAUSE = 1;
    private static final int CMD_NEXT = 2;
    private static final int CMD_PRE = 3;
    private static final int CMD_REQUEST_LAST = 4;

    private static final String RECEIVE_ACTION = "com.wow.dudu.music.notice";

    private static final String TYPE = "TYPE";
    //状态变更
    private static final int STATE_CHANGE = 1;
    private static final String STATE_CHANGE_STATE = "STATE_CHANGE_STATE";

    //歌曲变更
    private static final int SONG_CHANGE = 2;
    private static final String SONG_CHANGE_TITLE = "SONG_CHANGE_TITLE";
    private static final String SONG_CHANGE_SINGER = "SONG_CHANGE_SINGER";
    private static final String SONG_CHANGE_TOTAL_TIME = "SONG_CHANGE_TOTAL_TIME";

    private static final int PROGRESS_CHANGE = 3;
    private static final String CURRENT_PROGRESS = "CURRENT_PROGRESS";

    private static final int COVER_CHANGE = 4;
    private static final String SONG_CHANGE_COVER = "SONG_CHANGE_COVER";


    public void init(Context context, MusicPlugin musicView) {
        super.init(context, musicView);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_ACTION);
        this.context.registerReceiver(mReceiver, intentFilter);

        sendEvent(CMD_REQUEST_LAST);
    }

    @Override
    public String name() {
        return "嘟嘟音乐";
    }

    public void play() {
        sendEvent(CMD_PLAY_OR_PAUSE);
    }

    public void pause() {
        sendEvent(CMD_PLAY_OR_PAUSE);
    }

    public void next() {
        sendEvent(CMD_NEXT);
    }

    public void pre() {
        sendEvent(CMD_PRE);
    }

    private void sendEvent(int event) {
        if (!AppUtil.isInstall(context, AMAP_PACKAGE)) {
            Toast.makeText(context, "没有安装嘟嘟音乐", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(ACTION);
        intent.setClassName(PACKAGE_NAME, CLASS_NAME);
        intent.putExtra(CMD, event);
        context.sendBroadcast(intent);
    }

    @Override
    public void destroy() {
    }

    @Override
    public String clazz() {
        return "com.wow.dudu.music";
    }

    private boolean run = false;

    private int totalTime;

    private String title, singer;

    private ScheduledFuture coverRefreshTask;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent intent) {
            try {
                int type = intent.getIntExtra(TYPE, -1);
                switch (type) {
                    case STATE_CHANGE: {
                        run = intent.getBooleanExtra(STATE_CHANGE_STATE, false);
                        musicPlugin.refreshState(run, true);
                        break;
                    }
                    case SONG_CHANGE: {
                        title = intent.getStringExtra(SONG_CHANGE_TITLE);
                        singer = intent.getStringExtra(SONG_CHANGE_SINGER);
                        musicPlugin.refreshInfo(title, singer, false);
                        totalTime = intent.getIntExtra(SONG_CHANGE_TOTAL_TIME, 0);
                        musicPlugin.refreshProgress(0, totalTime);
                        coverRefreshTask = TaskExecutor.self().run(() -> musicPlugin.refreshCover(null), 5000);
                        break;
                    }
                    case PROGRESS_CHANGE: {
                        int current = intent.getIntExtra(CURRENT_PROGRESS, 0);
                        if (current <= totalTime && run) {
                            musicPlugin.refreshProgress(current, totalTime);
                        }
                        break;
                    }
                    case COVER_CHANGE: {
                        String title = intent.getStringExtra(SONG_CHANGE_TITLE);
                        String singer = intent.getStringExtra(SONG_CHANGE_SINGER);
                        String cover = intent.getStringExtra(SONG_CHANGE_COVER);
                        if (CommonUtil.equals(title, DDMusicCarController.this.title) && CommonUtil.equals(singer, DDMusicCarController.this.singer) && CommonUtil.isNotNull(cover)) {
                            musicPlugin.refreshCover(null, cover);
                            if (coverRefreshTask != null) {
                                coverRefreshTask.cancel(true);
                                coverRefreshTask = null;
                            }
                        }
                        break;
                    }
                }
            } catch (Exception e) {

            }
        }
    };
}
