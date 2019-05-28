package com.wow.carlauncher.ex.plugin.music;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.plugin.music.event.MMEventControllerRefresh;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventCoverRefresh;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventProgress;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventState;
import com.wow.carlauncher.ex.plugin.music.event.PMusicRefresLrc;
import com.wow.carlauncher.ex.plugin.music.plugin.JidouMusicController;
import com.wow.carlauncher.ex.plugin.music.plugin.KuwoMusicController;
import com.wow.carlauncher.ex.plugin.music.plugin.NwdMusicController;
import com.wow.carlauncher.ex.plugin.music.plugin.QQMusicCarController;
import com.wow.carlauncher.ex.plugin.music.plugin.SystemMusicController;
import com.wow.carlauncher.ex.plugin.music.plugin.ZXMusicController;
import com.wow.carlauncher.ex.plugin.xmlyfm.XmlyfmPlugin;

import static com.wow.carlauncher.common.CommonData.SDATA_LAST_ACTIVITY_TYPE;
import static com.wow.carlauncher.common.CommonData.SDATA_LAST_ACTIVITY_TYPE_MUSIC;
import static com.wow.carlauncher.common.CommonData.SDATA_LAST_ACTIVITY_TYPE_NONE;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_CONTROLLER;

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
        long t1 = System.currentTimeMillis();
        setContext(context);
        setController(MusicControllerEnum.getById(SharedPreUtil.getInteger(SDATA_MUSIC_CONTROLLER, MusicControllerEnum.SYSMUSIC.getId())));
        if (SharedPreUtil.getBoolean(CommonData.SDATA_START_LAST_ACTIVITY, true) && SharedPreUtil.getInteger(CommonData.SDATA_LAST_ACTIVITY_TYPE, SDATA_LAST_ACTIVITY_TYPE_NONE) == SDATA_LAST_ACTIVITY_TYPE_MUSIC) {
            TaskExecutor.self().run(() -> {
                if (!playing) {
                    next();
                }
            }, 5000);
        }
        LogEx.d(this, "init time:" + (System.currentTimeMillis() - t1));
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
            case KUWOMUSIC:
                musicController = new KuwoMusicController();
                break;
            default:
                musicController = new SystemMusicController();
                break;
        }
        musicController.init(getContext(), this);
        LogEx.d(this, "musicController:" + musicController);
        postEvent(new MMEventControllerRefresh());
    }

    public String clazz() {
        if (musicController != null) {
            return musicController.clazz();
        }
        return null;
    }

    public String name() {
        if (musicController != null) {
            return musicController.name();
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
    private String lastCover = "";

    public void refreshCover(final String url) {
        if (CommonUtil.isNull(url) || !CommonUtil.equals(lastCover, url)) {
            lastCover = url;
            lastMusicCover = new PMusicEventCoverRefresh().setUrl(url).setHave(CommonUtil.isNotNull(url));
            postEvent(lastMusicCover);
        }
    }


    private PMusicRefresLrc pMusicRefresLrc;

    public void refreshLrc(String lrc) {
        if (SharedPreUtil.getBoolean(CommonData.SDATA_MUSIC_USE_LRC, true)) {
            pMusicRefresLrc = new PMusicRefresLrc().setLrc(lrc);
            postEvent(pMusicRefresLrc);
        }
    }

    public void requestLast() {
        LogEx.d(this, "requestLast");
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
        LogEx.d(this, "playOrPause:" + playing);
        if (musicController != null) {
            if (playing) {
                musicController.pause();
                SharedPreUtil.saveInteger(SDATA_LAST_ACTIVITY_TYPE, SDATA_LAST_ACTIVITY_TYPE_NONE);
            } else {
                XmlyfmPlugin.self().stop();
                musicController.play();
                SharedPreUtil.saveInteger(SDATA_LAST_ACTIVITY_TYPE, SDATA_LAST_ACTIVITY_TYPE_MUSIC);
            }
        }
    }

    public void pause() {
        LogEx.d(this, "pause:" + playing);
        if (musicController != null) {
            musicController.pause();
        }
    }

    public void next() {
        LogEx.d(this, "next");
        if (musicController != null) {
            XmlyfmPlugin.self().stop();
            musicController.next();
            SharedPreUtil.saveInteger(SDATA_LAST_ACTIVITY_TYPE, SDATA_LAST_ACTIVITY_TYPE_MUSIC);
        }
    }

    public void pre() {
        LogEx.d(this, "pre");
        if (musicController != null) {
            XmlyfmPlugin.self().stop();
            musicController.pre();
            SharedPreUtil.saveInteger(SDATA_LAST_ACTIVITY_TYPE, SDATA_LAST_ACTIVITY_TYPE_MUSIC);
        }
    }


}
