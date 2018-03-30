package com.wow.carlauncher.plugin.music;

import android.content.Context;
import android.graphics.Bitmap;

import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.music.plugin.JidouMusicController;
import com.wow.carlauncher.plugin.music.plugin.NwdMusicController;
import com.wow.carlauncher.plugin.music.plugin.PowerAmpCarController;
import com.wow.carlauncher.plugin.music.plugin.QQMusicCarController;
import com.wow.carlauncher.plugin.music.plugin.SystemMusicController;
import com.wow.frame.util.SharedPreUtil;

import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_CONTROLLER;

public class MusicPlugin extends BasePlugin<MusicPluginListener> {
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
        super.init(context);
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
            case POWERAMPMUSIC:
                musicController = new PowerAmpCarController();
                break;
        }
        if(musicController!=null){
            musicController.init(context, this);
        }
    }


    public void refreshInfo(final String title, final String artist) {
        runListener(new ListenerRuner<MusicPluginListener>() {
            @Override
            public void run(MusicPluginListener musicPluginListener) {
                musicPluginListener.refreshInfo(title, artist);
            }
        });
    }

    public void refreshCover(final Bitmap cover) {
        runListener(new ListenerRuner<MusicPluginListener>() {
            @Override
            public void run(MusicPluginListener musicPluginListener) {
                musicPluginListener.refreshCover(cover);
            }
        });
    }

    public void refreshProgress(final int curr_time, final int total_tim) {
        runListener(new ListenerRuner<MusicPluginListener>() {
            @Override
            public void run(MusicPluginListener musicPluginListener) {
                musicPluginListener.refreshProgress(curr_time, total_tim);
            }
        });
    }

    public void refreshState(final boolean run) {
        playing = run;
        runListener(new ListenerRuner<MusicPluginListener>() {
            @Override
            public void run(MusicPluginListener musicPluginListener) {
                musicPluginListener.refreshState(run);
            }
        });
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
