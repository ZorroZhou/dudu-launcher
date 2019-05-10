package com.wow.carlauncher.ex.plugin.music.plugin;

import android.content.Context;

import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.plugin.music.MusicController;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.kuwo.autosdk.api.KWAPI;
import cn.kuwo.autosdk.api.OnGetLyricsListener;
import cn.kuwo.autosdk.api.OnGetSongImgUrlListener;
import cn.kuwo.autosdk.api.PlayState;
import cn.kuwo.base.bean.Music;

import static cn.kuwo.autosdk.api.PlayerStatus.PLAYING;

/**
 * Created by 10124 on 2017/10/26.
 */

public class KuwoMusicController extends MusicController {

    private KWAPI mKwapi;
    private boolean run;

    public void init(Context context, MusicPlugin musicView) {
        super.init(context, musicView);
        mKwapi = KWAPI.createKWAPI(context.getApplicationContext(), "auto");
        mKwapi.bindAutoSdkService();
        mKwapi.registerConnectedListener(b -> {
            if (b) {
                setRun(CommonUtil.equals(PLAYING, mKwapi.getPlayerStatus()));
                Music music = mKwapi.getNowPlayingMusic();
                if (music != null) {
                    musicPlugin.refreshInfo(music.name, music.artist, false);
                    mKwapi.getSongPicUrl(music, onGetSongImgUrlListener);
                    mKwapi.getLyrics(music, new OnGetLyricsListener() {

                        @Override
                        public void sendSyncNotice_LyricsStart(Music music) {
                            
                        }

                        @Override
                        public void sendSyncNotice_LyricsFinished(Music music, String s) {

                        }

                        @Override
                        public void sendSyncNotice_LyricsFailed(Music music) {

                        }

                        @Override
                        public void sendSyncNotice_LyricsNone(Music music) {

                        }
                    });

                    totalTime = mKwapi.getCurrentMusicDuration();
                    overTime = System.currentTimeMillis() + totalTime - mKwapi.getCurrentPos();
                }
            } else {
                setRun(false);
            }
        });
        mKwapi.registerPlayerStatusListener((playerStatus, music) -> {
            setRun(CommonUtil.equals(PLAYING, playerStatus));
            musicPlugin.refreshInfo(music.name, music.artist, false);
            mKwapi.getSongPicUrl(music, onGetSongImgUrlListener);

            totalTime = mKwapi.getCurrentMusicDuration();
            overTime = System.currentTimeMillis() + totalTime - mKwapi.getCurrentPos();
        });
        mKwapi.registerExitListener(() -> {
            setRun(false);
        });
        mKwapi.openOrCloseToast(false);

        EventBus.getDefault().register(this);
    }

    private void setRun(boolean run) {
        this.run = run;
        musicPlugin.refreshState(run, true);
    }

    @Override
    public String clazz() {
        return "com.acloud.stub.localmusic";
    }

    public void play() {
        mKwapi.setPlayState(PlayState.STATE_PLAY);
    }

    public void pause() {
        mKwapi.setPlayState(PlayState.STATE_PAUSE);
    }

    public void next() {
        mKwapi.setPlayState(PlayState.STATE_NEXT);
    }

    public void pre() {
        mKwapi.setPlayState(PlayState.STATE_PRE);
    }

    private long overTime;
    private int totalTime;

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final MTimeSecondEvent event) {
        //每隔一秒钟上报一下进度信息
        int ccc = (int) (totalTime + System.currentTimeMillis() - overTime);
        if (ccc < totalTime && run) {
            musicPlugin.refreshProgress((int) (totalTime + System.currentTimeMillis() - overTime), totalTime);
        }
    }

    @Override
    public void destroy() {
        mKwapi.unRegisterPlayerStatusListener();
        mKwapi.unRegisterExitListener();
        mKwapi.unBindKuWoApp();
        EventBus.getDefault().unregister(this);
    }

    private OnGetSongImgUrlListener onGetSongImgUrlListener = new OnGetSongImgUrlListener() {
        @Override
        public void onGetSongImgUrlSucessed(Music music, String s) {
            musicPlugin.refreshCover(s);
        }

        @Override
        public void onGetSongImgUrlFailed(Music music, int i) {
            musicPlugin.refreshCover(null);
        }
    };
}
