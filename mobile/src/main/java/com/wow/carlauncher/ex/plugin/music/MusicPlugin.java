package com.wow.carlauncher.ex.plugin.music;

import android.content.Context;

import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventCoverRefresh;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventProgress;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventState;
import com.wow.carlauncher.ex.plugin.music.plugin.JidouMusicController;
import com.wow.carlauncher.ex.plugin.music.plugin.NwdMusicController;
import com.wow.carlauncher.ex.plugin.music.plugin.QQMusicCarController;
import com.wow.carlauncher.ex.plugin.music.plugin.SystemMusicController;

import org.greenrobot.eventbus.EventBus;

import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_CONTROLLER;

public class MusicPlugin extends ContextEx {
    private static MusicPlugin self;

    public static MusicPlugin self() {
        if (self == null) {
            self = new MusicPlugin();
        }
        return self;
    }

    private MusicPlugin() {

    }

    private MusicController musicController;

    private boolean playing = false;

    public void init(Context context) {
        setContext(context);
        setController(MusicControllerEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_CONTROLLER, MusicControllerEnum.SYSMUSIC.getId())));
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
        }
        if (musicController != null) {
            musicController.init(getContext(), this);
        }
    }

    public String clazz() {
        if (musicController != null) {
            return musicController.clazz();
        }
        return null;
    }

    private PMusicEventInfo lastMusicInfo;

    public void refreshInfo(final String title, final String artist) {
        lastMusicInfo = new PMusicEventInfo().setArtist(artist).setTitle(title);
        EventBus.getDefault().post(new PMusicEventInfo().setArtist(artist).setTitle(title));
    }

    private PMusicEventProgress lastMusicProgress;

    public void refreshProgress(final int curr_time, final int total_tim) {
        lastMusicProgress = new PMusicEventProgress().setCurrTime(curr_time).setTotalTime(total_tim);
        EventBus.getDefault().post(lastMusicProgress);
    }

    private PMusicEventState lastMusicState;

    public void refreshState(final boolean run, final boolean showProgress) {
        playing = run;
        lastMusicState = new PMusicEventState().setRun(run).setShowProgress(showProgress);
        EventBus.getDefault().post(lastMusicState);
    }

    private PMusicEventCoverRefresh lastMusicCover;

    public void refreshCover(final String url) {
        lastMusicCover = new PMusicEventCoverRefresh().setUrl(url);
        EventBus.getDefault().post(lastMusicCover);
    }

    public void requestLast() {
        if (lastMusicState != null) {
            EventBus.getDefault().post(lastMusicState);
        }
        if (lastMusicInfo != null) {
            EventBus.getDefault().post(lastMusicInfo);
        }
        if (lastMusicProgress != null) {
            EventBus.getDefault().post(lastMusicProgress);
        }
        if (lastMusicCover != null) {
            EventBus.getDefault().post(lastMusicCover);
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
