package com.wow.carlauncher.ex.plugin.dudufm;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.AppUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.ContextEx;

import static com.wow.carlauncher.common.CommonData.SDATA_LAST_ACTIVITY_TYPE;
import static com.wow.carlauncher.common.CommonData.SDATA_LAST_ACTIVITY_TYPE_FM;
import static com.wow.carlauncher.common.CommonData.SDATA_LAST_ACTIVITY_TYPE_MUSIC;
import static com.wow.carlauncher.common.CommonData.SDATA_LAST_ACTIVITY_TYPE_NONE;
import static com.wow.carlauncher.common.CommonData.SDATA_START_LAST_ACTIVITY_DELAY;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.AMAP_PACKAGE;

/**
 * Created by 10124 on 2017/11/9.
 */

public class DudufmPlugin extends ContextEx {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static DudufmPlugin instance = new DudufmPlugin();
    }

    public static DudufmPlugin self() {
        return DudufmPlugin.SingletonHolder.instance;
    }

    private DudufmPlugin() {

    }

    public static final String PACKAGE_NAME = "com.wow.dudu.fm";
    private static final String CLASS_NAME = "com.wow.dudu.fm.receiver.FmCmdReceiver";

    private static final String ACTION = "com.wow.dudu.fm.cmd";

    private static final String CMD = "CMD";
    private static final int CMD_PLAY_OR_PAUSE = 1;
    private static final int CMD_NEXT = 2;
    private static final int CMD_PRE = 3;
    private static final int CMD_REQUEST_LAST = 4;
    private static final int CMD_STOP = 5;

    private static final String RECEIVE_ACTION = "com.wow.dudu.fm.notice";

    private static final String TYPE = "TYPE";
    //状态变更
    private static final int STATE_CHANGE = 1;
    private static final String STATE_CHANGE_STATE = "STATE_CHANGE_STATE";

    //歌曲变更
    private static final int RADIO_CHANGE = 2;
    private static final String RADIO_CHANGE_TITLE = "SONG_CHANGE_TITLE";
    private static final String RADIO_CHANGE_PNAME = "RADIO_CHANGE_PNAME";
    private static final String RADIO_CHANGE_LOGO = "RADIO_CHANGE_LOGO";

    private void sendEvent(int event) {
        if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
            Toast.makeText(getContext(), "没有安装嘟嘟音乐", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(ACTION);
        intent.setClassName(PACKAGE_NAME, CLASS_NAME);
        intent.putExtra(CMD, event);
        getContext().sendBroadcast(intent);
    }

    public void playOrStop() {
        sendEvent(CMD_PLAY_OR_PAUSE);
        SharedPreUtil.saveInteger(SDATA_LAST_ACTIVITY_TYPE, SDATA_LAST_ACTIVITY_TYPE_FM);
    }

    public void stop() {
        sendEvent(CMD_STOP);
    }

    public void next() {
        sendEvent(CMD_NEXT);
        SharedPreUtil.saveInteger(SDATA_LAST_ACTIVITY_TYPE, SDATA_LAST_ACTIVITY_TYPE_FM);
    }

    public void prev() {
        sendEvent(CMD_PRE);
        SharedPreUtil.saveInteger(SDATA_LAST_ACTIVITY_TYPE, SDATA_LAST_ACTIVITY_TYPE_FM);
    }

    public void init(Context context) {
        long t1 = System.currentTimeMillis();
        setContext(context);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_ACTION);
        context.registerReceiver(mReceiver, intentFilter);
        sendEvent(CMD_REQUEST_LAST);

        if (SharedPreUtil.getBoolean(CommonData.SDATA_START_LAST_ACTIVITY, true) && SharedPreUtil.getInteger(CommonData.SDATA_LAST_ACTIVITY_TYPE, SDATA_LAST_ACTIVITY_TYPE_NONE) == SDATA_LAST_ACTIVITY_TYPE_FM) {
            TaskExecutor.self().run(() -> {
                System.out.println("1!!!!" + run);
                if (!run) {
                    playOrStop();
                }
            }, SharedPreUtil.getInteger(SDATA_START_LAST_ACTIVITY_DELAY, 10) * 1000);
        }
        LogEx.d(this, "init time:" + (System.currentTimeMillis() - t1));
    }

    private boolean run = false;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent intent) {
            try {
                int type = intent.getIntExtra(TYPE, -1);
                switch (type) {
                    case STATE_CHANGE: {
                        run = intent.getBooleanExtra(STATE_CHANGE_STATE, false);
                        postEvent(new PDuduFmEventStateChange().setRun(run));
                        break;
                    }
                    case RADIO_CHANGE: {
                        postEvent(new PDuduFmEventRadioInfo()
                                .setTitle(intent.getStringExtra(RADIO_CHANGE_TITLE))
                                .setProgramName(intent.getStringExtra(RADIO_CHANGE_PNAME))
                                .setCover(intent.getStringExtra(RADIO_CHANGE_LOGO)));
                        break;
                    }
                }
            } catch (Exception ignored) {

            }
        }
    };
}
