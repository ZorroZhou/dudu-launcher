package com.wow.carlauncher.ex.plugin.music.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.plugin.music.MusciCoverUtil;
import com.wow.carlauncher.ex.plugin.music.MusicController;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

/**
 * Created by 10124 on 2017/10/26.
 */

public class QQMusicCarController extends MusicController {
    private static final String PACKAGE_NAME = "com.tencent.qqmusiccar";
    private static final String CLASS_NAME = "com.tencent.qqmusiccar.app.reciver.BroadcastReceiverCenterForThird";

    private static final int WE_DRIVE_NEXT = 3;
    private static final int WE_DRIVE_PAUSE = 1;
    private static final int WE_DRIVE_PRE = 2;
    private static final int WE_DRIVE_RESUME = 0;
    private Bitmap defcover;

    private Gson gson;

    public void init(Context context, MusicPlugin musicView) {
        super.init(context, musicView);
        gson = GsonUtil.getGson();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tencent.qqmusiccar.action.PLAY_COMMAND_SEND_FOR_THIRD");
        intentFilter.addAction("com.android.music.playstatechanged");
        this.context.registerReceiver(mReceiver, intentFilter);

        Intent intent2 = new Intent("com.tencent.qqmusiccar.action");
        intent2.setClassName(PACKAGE_NAME, CLASS_NAME);
        intent2.setData(Uri.parse("qqmusiccar://asdasd?action=100"));
        context.sendBroadcast(intent2);

        EventBus.getDefault().register(this);
        defcover = BitmapFactory.decodeResource(context.getResources(), R.mipmap.music_dlogo);
    }


    public void play() {
        sendEvent(WE_DRIVE_RESUME);
    }

    public void pause() {
        sendEvent(WE_DRIVE_PAUSE);
    }

    public void next() {
        sendEvent(WE_DRIVE_NEXT);
    }

    public void pre() {
        sendEvent(WE_DRIVE_PRE);
    }

    private void sendEvent(int event) {
        Intent intent = new Intent("com.tencent.qqmusiccar.action");
        intent.setClassName(PACKAGE_NAME, CLASS_NAME);
        intent.setData(Uri.parse("qqmusiccar://asdasd?action=20&m0=" + event));
        context.sendBroadcast(intent);
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
        context.unregisterReceiver(mReceiver);
    }

    @Override
    public String clazz() {
        return "com.tencent.qqmusiccar";
    }

    private boolean run = false;

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final MTimeSecondEvent event) {
        //每隔一秒钟上报一下进度信息
        int ccc = (int) (totalTime + System.currentTimeMillis() - overTime);
        if (ccc < totalTime && run) {
            musicPlugin.refreshProgress((int) (totalTime + System.currentTimeMillis() - overTime), totalTime);
        }
    }

    private long overTime;
    private int totalTime;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent intent) {
            try {
                //&& waitMsg
                if ("com.tencent.qqmusiccar.action.PLAY_COMMAND_SEND_FOR_THIRD".equals(intent.getAction()) && intent.getStringExtra("com.tencent.qqmusiccar.EXTRA_COMMAND_DATA") != null) {
                    String value = intent.getStringExtra("com.tencent.qqmusiccar.EXTRA_COMMAND_DATA");
                    Log.e(PACKAGE_NAME, "onReceive: " + value);
                    Map map = gson.fromJson(value, Map.class);
                    //更新状态的命令
                    String cmd = ((Map) map.get("command")).get("method") + "";

                    if (cmd.equals("update_state")) {
                        Log.d(PACKAGE_NAME, "onReceive: 1");
                        UpdateStateMessage message = gson.fromJson(value, UpdateStateMessage.class);
                        UpdateStateData data = message.getCommand().getData();
                        if (data != null) {
                            int curr_time = data.getCurr_time();
                            totalTime = data.getTotal_time();
                            overTime = System.currentTimeMillis() + totalTime - curr_time;
                            String artist = "";
                            if (data.getKey_artist() != null && data.getKey_artist().size() > 0) {
                                artist = data.getKey_artist().get(0).getSinger();
                            }
                            musicPlugin.refreshInfo(data.getKey_title(), artist);

                            if (data.getState() == 2) {
                                musicPlugin.refreshState(true, true);
                                run = true;
                            } else {
                                musicPlugin.refreshState(false, true);
                                run = false;
                            }
                        }
                    } else if (cmd.equals("update_song")) {
                        Log.d(PACKAGE_NAME, "onReceive: 2");
                        UpdateSongMessage message = gson.fromJson(value, UpdateSongMessage.class);
                        BaseSongInfo data = message.getCommand().getData();
                        if (data != null) {
                            String artist = "";
                            if (data.getKey_artist() != null && data.getKey_artist().size() > 0) {
                                artist = data.getKey_artist().get(0).getSinger();
                            }
                            musicPlugin.refreshInfo(data.getKey_title(), artist);

                            if (!SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_MUSIC_INSIDE_COVER, true)) {
                                MusciCoverUtil.loadCover(data.getKey_title(), artist, musicPlugin);
                            }
                        }
                    } else if (cmd.equals("update_album")) {
                        UpdateAlbumMessage message = gson.fromJson(value, UpdateAlbumMessage.class);
                        final UpdateAlbumData data = message.getCommand().getData();
                        if (SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_MUSIC_INSIDE_COVER, true)) {
                            musicPlugin.refreshCover(data.getAlbum_url());
                        }
                    } else if (cmd.equals("update_lyric")) {
                        UpdateLyricMessage message = gson.fromJson(value, UpdateLyricMessage.class);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private static class UpdateStateMessage extends BaseMessage<BaseCommand<UpdateStateData>, UpdateStateData> {
    }

    private static class UpdateSongMessage extends BaseMessage<BaseCommand<BaseSongInfo>, BaseSongInfo> {
    }

    private static class UpdateAlbumMessage extends BaseMessage<BaseCommand<UpdateAlbumData>, UpdateAlbumData> {
    }

    private static class UpdateLyricMessage extends BaseMessage<BaseCommand<UpdateLyricData>, UpdateLyricData> {
    }

    static class BaseMessage<T extends BaseCommand<D>, D> {
        private String module;
        private T command;

        public String getModule() {
            return module;
        }

        public BaseMessage<T, D> setModule(String module) {
            this.module = module;
            return this;
        }

        public T getCommand() {
            return command;
        }

        public BaseMessage<T, D> setCommand(T command) {
            this.command = command;
            return this;
        }
    }

    static class BaseCommand<T> {
        private String method;
        private T data;

        public String getMethod() {
            return method;
        }

        public BaseCommand<T> setMethod(String method) {
            this.method = method;
            return this;
        }

        public T getData() {
            return data;
        }

        public BaseCommand<T> setData(T data) {
            this.data = data;
            return this;
        }
    }

    private static class KeyArtist {
        private String singer;

        public String getSinger() {
            return singer;
        }

        public KeyArtist setSinger(String singer) {
            this.singer = singer;
            return this;
        }
    }

    static class BaseSongInfo {
        private String key_title;
        private String key_album;
        private int key_is_fav;
        private List<KeyArtist> key_artist;

        public String getKey_title() {
            return key_title;
        }

        public BaseSongInfo setKey_title(String key_title) {
            this.key_title = key_title;
            return this;
        }

        public String getKey_album() {
            return key_album;
        }

        public BaseSongInfo setKey_album(String key_album) {
            this.key_album = key_album;
            return this;
        }

        public int getKey_is_fav() {
            return key_is_fav;
        }

        public BaseSongInfo setKey_is_fav(int key_is_fav) {
            this.key_is_fav = key_is_fav;
            return this;
        }

        public List<KeyArtist> getKey_artist() {
            return key_artist;
        }

        public BaseSongInfo setKey_artist(List<KeyArtist> key_artist) {
            this.key_artist = key_artist;
            return this;
        }
    }

    static class UpdateStateData extends BaseSongInfo {
        private int state;
        private int curr_time;
        private int total_time;

        public int getState() {
            return state;
        }

        public UpdateStateData setState(int state) {
            this.state = state;
            return this;
        }

        public int getCurr_time() {
            return curr_time;
        }

        public UpdateStateData setCurr_time(int curr_time) {
            this.curr_time = curr_time;
            return this;
        }

        public int getTotal_time() {
            return total_time;
        }

        public UpdateStateData setTotal_time(int total_time) {
            this.total_time = total_time;
            return this;
        }
    }

    static class UpdateAlbumData extends BaseSongInfo {
        private int code;
        private String album_url;

        public int getCode() {
            return code;
        }

        public UpdateAlbumData setCode(int code) {
            this.code = code;
            return this;
        }

        public String getAlbum_url() {
            return album_url;
        }

        public UpdateAlbumData setAlbum_url(String album_url) {
            this.album_url = album_url;
            return this;
        }
    }

    static class UpdateLyricData extends BaseSongInfo {
        private int code;
        private String lyric;

        public int getCode() {
            return code;
        }

        public UpdateLyricData setCode(int code) {
            this.code = code;
            return this;
        }

        public String getLyric() {
            return lyric;
        }

        public UpdateLyricData setLyric(String lyric) {
            this.lyric = lyric;
            return this;
        }
    }

}
