package com.wow.carlauncher.ex.plugin.music;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.plugin.fk.FangkongPlugin;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventCoverRefresh;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventProgress;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventState;
import com.wow.carlauncher.ex.plugin.music.event.PMusicRefresLrc;
import com.wow.carlauncher.ex.plugin.music.plugin.JidouMusicController;
import com.wow.carlauncher.ex.plugin.music.plugin.NwdMusicController;
import com.wow.carlauncher.ex.plugin.music.plugin.QQMusicCarController;
import com.wow.carlauncher.ex.plugin.music.plugin.SystemMusicController;
import com.wow.carlauncher.ex.plugin.music.plugin.ZXMusicController;

import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_CONTROLLER;
import static com.wow.carlauncher.common.CommonData.TAG;

public class MusicPlugin extends ContextEx {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static MusicPlugin instance = new MusicPlugin();
    }

    public static MusicPlugin self() {
        return MusicPlugin.SingletonHolder.instance;
    }

    private MusicPlugin() {
    }

    private MusicController musicController;

    private boolean playing = false;

    public void init(Context context) {
        setContext(context);
        setController(MusicControllerEnum.getById(SharedPreUtil.getInteger(SDATA_MUSIC_CONTROLLER, MusicControllerEnum.SYSMUSIC.getId())));
        Log.e(TAG + getClass().getSimpleName(), "init ");
    }

    public void setController(MusicControllerEnum controller) {
        if (musicController != null) {
            musicController.destroy();
        }
        switch (controller) {
            case SYSMUSIC:
                musicController = new SystemMusicController();
                break;
            case JIDOUMUSIC:
                musicController = new JidouMusicController();
                break;
            case QQCARMUSIC:
                musicController = new QQMusicCarController();
                break;
            case NWDMUSIC:
                musicController = new NwdMusicController();
                break;
            case ZXMUSIC:
                musicController = new ZXMusicController();
                break;
            default:
                musicController = new SystemMusicController();
                break;
        }
        musicController.init(getContext(), this);
    }

    public String clazz() {
        if (musicController != null) {
            return musicController.clazz();
        }
        return null;
    }

    private PMusicEventInfo lastMusicInfo;

    public void refreshInfo(String title, String artist, boolean havelrc) {
        if (!SharedPreUtil.getBoolean(CommonData.SDATA_MUSIC_USE_LRC, true)) {
            havelrc = false;
        }
        lastMusicInfo = new PMusicEventInfo().setArtist(artist).setTitle(title).setHaveLrc(havelrc);
        postEvent(lastMusicInfo);
    }

    private PMusicEventProgress lastMusicProgress;

    public void refreshProgress(final int curr_time, final int total_tim) {
        lastMusicProgress = new PMusicEventProgress().setCurrTime(curr_time).setTotalTime(total_tim);
        postEvent(lastMusicProgress);
    }

    private PMusicEventState lastMusicState;

    public void refreshState(final boolean run, final boolean showProgress) {
        playing = run;
        lastMusicState = new PMusicEventState().setRun(run).setShowProgress(showProgress);
        postEvent(lastMusicState);
    }

    private PMusicEventCoverRefresh lastMusicCover;

    public void refreshCover(final String url) {
        lastMusicCover = new PMusicEventCoverRefresh().setUrl(url);
        postEvent(lastMusicCover);
    }


    private PMusicRefresLrc pMusicRefresLrc;

    public void refreshLrc(String lrc) {
        if (SharedPreUtil.getBoolean(CommonData.SDATA_MUSIC_USE_LRC, true)) {
            pMusicRefresLrc = new PMusicRefresLrc().setLrc(lrc);
            postEvent(pMusicRefresLrc);
        }
    }

    public void requestLast() {
        if (pMusicRefresLrc != null) {
            postEvent(pMusicRefresLrc);
        }
        if (lastMusicState != null) {
            postEvent(lastMusicState);
        }
        if (lastMusicInfo != null) {
            postEvent(lastMusicInfo);
        }
        if (lastMusicProgress != null) {
            postEvent(lastMusicProgress);
        }
        if (lastMusicCover != null) {
            postEvent(lastMusicCover);
        }
    }

    public void playOrPause() {
        if (musicController != null) {
            if (playing) {
                musicController.pause();
            } else {
                musicController.play();
            }
        }
    }

    public void next() {
        if (musicController != null) {
            musicController.next();
        }
    }

    public void pre() {
        if (musicController != null) {
            musicController.pre();
        }
    }
}
