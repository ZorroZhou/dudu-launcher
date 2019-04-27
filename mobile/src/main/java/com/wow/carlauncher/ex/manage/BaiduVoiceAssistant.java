package com.wow.carlauncher.ex.manage;

import android.content.Context;
import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONObject;

import java.util.HashMap;

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

    //唤醒词,嘟嘟嘟嘟,嘟嘟宝贝,嘟嘟你好
    private EventManager wakeup;

    public void init(Context context) {
        wakeup = EventManagerFactory.create(context, BAIDU_WAKE_UP);
        // 基于SDK唤醒词集成1.3 注册输出事件
        wakeup.registerListener(this); //  EventListener 中 onEvent方法


    }

    public void start() {
        HashMap map = new HashMap();
        map.put(SpeechConstant.WP_WORDS_FILE, "assets://WakeUpBaidu.bin");
        map.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        map.put(SpeechConstant.APP_ID, "11197584");
        String json = new JSONObject(map).toString();
        System.out.println(json);
        wakeup.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
    }


    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {

        String logTxt = "name: " + name;
        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :" + params;
        } else if (data != null) {
            logTxt += " ;data length=" + data.length;
        }
        printLog(logTxt);
    }

    private boolean logTime = true;

    private void printLog(String text) {
        if (logTime) {
            text += "  ;time=" + System.currentTimeMillis();
        }
        text += "\n";
        Log.i(getClass().getName(), text);
    }
}
