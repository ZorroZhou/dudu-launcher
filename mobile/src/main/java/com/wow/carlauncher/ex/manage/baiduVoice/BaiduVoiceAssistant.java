package com.wow.carlauncher.ex.manage.baiduVoice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.ex.manage.baiduVoice.bean.AsrPartial;
import com.wow.carlauncher.ex.manage.baiduVoice.bean.AsrStart;
import com.wow.carlauncher.ex.manage.baiduVoice.bean.WakeUpStart;
import com.wow.carlauncher.ex.manage.baiduVoice.event.MVaNewWordFind;
import com.wow.carlauncher.view.popup.VoiceWin;

import org.greenrobot.eventbus.EventBus;

public class BaiduVoiceAssistant implements EventListener {
    private static BaiduVoiceAssistant self;

    public static BaiduVoiceAssistant self() {
        if (self == null) {
            self = new BaiduVoiceAssistant();
        }
        return self;
    }

    private BaiduVoiceAssistant() {
    }

    private static final String BAIDU_WAKE_UP = "wp";


    private static final String BAIDU_ASR = "asr";
    //唤醒词,嘟嘟嘟嘟,嘟嘟宝贝,嘟嘟你好
    private EventManager wakeup;
    private EventManager asr;
    private Context context;

    public void init(Context context) {
        this.context = context;
        wakeup = EventManagerFactory.create(context, BAIDU_WAKE_UP);
        // 基于SDK唤醒词集成1.3 注册输出事件
        wakeup.registerListener(this); //  EventListener 中 onEvent方法

        asr = EventManagerFactory.create(context, BAIDU_ASR);
        asr.registerListener(this); //  EventListener 中 onEvent方法
    }

    public void startWakeUp() {
        WakeUpStart wakeUpStart = new WakeUpStart().setKwsFile("assets://WakeUpBaidu.bin").setAppid("11197584");
        wakeup.send(SpeechConstant.WAKEUP_START, GsonUtil.getGson().toJson(wakeUpStart), null, 0, 0);
    }

    public void stopWakeUp() {
        wakeup.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0); //
    }

    public void startAsr() {
        asr.send(SpeechConstant.ASR_START, GsonUtil.getGson().toJson(new AsrStart()), null, 0, 0);
    }

    public void stopAsr() {
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
    }


    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        switch (name) {
            case SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS:
                openAssistant();
                break;
            case SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL:
                checkAsrPartial(params);
                break;


        }

        Log.e(getClass().getSimpleName(), "name: " + name);
        Log.e(getClass().getSimpleName(), "params: " + params);
    }

    private void openAssistant() {
        VoiceWin.self().show();
    }

    private void checkAsrPartial(String param) {
        try {
            AsrPartial asrPartial = GsonUtil.getGson().fromJson(param, AsrPartial.class);
            EventBus.getDefault().post(new MVaNewWordFind().setChecking("partial_result".equals(asrPartial.getResult_type())).setWord(asrPartial.getBest_result()));
        } catch (Exception e) {

        }
    }
}
