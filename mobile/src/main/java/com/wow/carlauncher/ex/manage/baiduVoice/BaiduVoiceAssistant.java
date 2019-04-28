package com.wow.carlauncher.ex.manage.baiduVoice;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.ex.manage.baiduVoice.bean.AsrEventPartial;
import com.wow.carlauncher.ex.manage.baiduVoice.bean.AsrStart;
import com.wow.carlauncher.ex.manage.baiduVoice.bean.WakeUpStart;
import com.wow.carlauncher.ex.manage.baiduVoice.event.MVaAsrStateChange;
import com.wow.carlauncher.ex.manage.baiduVoice.event.MVaNewWordFind;
import com.wow.carlauncher.ex.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.ex.plugin.console.ConsolePlugin;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.view.activity.launcher.event.LItemToFristEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import static com.wow.carlauncher.common.CommonData.TAG;

public class BaiduVoiceAssistant {
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

    private static final String BAIDU_MODEL_FILE = "bd_etts_common_speech_as_mand_eng_high_am_v3.0.0_20170516.dat";
    private static final String BAIDU_TEXT_FILE = "bd_etts_text.dat";

    private static String APP_ID;
    private static String API_KEY;
    private static String SECRET_KEY;

    //唤醒词,嘟嘟嘟嘟,嘟嘟宝贝,嘟嘟你好
    private EventManager wakeup;
    private EventManager asr;
    private SpeechSynthesizer ss;
    private Context context;
    private boolean init = false;

    private boolean asrRun = false;
    private static final byte[] asrRunLock = new byte[0];

    private KeyWord[] keyWords;

    public void init(Context context) {
        this.context = context;
        if (init) {
            return;
        }
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            APP_ID = appInfo.metaData.getInt("com.baidu.speech.APP_ID") + "";
            API_KEY = appInfo.metaData.getString("com.baidu.speech.API_KEY");
            SECRET_KEY = appInfo.metaData.getString("com.baidu.speech.SECRET_KEY");
            initVoice();
            initSpeech();
            initKey();
            init = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initVoice() {
        wakeup = EventManagerFactory.create(context, BAIDU_WAKE_UP);
        // 基于SDK唤醒词集成1.3 注册输出事件
        wakeup.registerListener(eventListener); //  EventListener 中 onEvent方法

        asr = EventManagerFactory.create(context, BAIDU_ASR);
        asr.registerListener(eventListener); //  EventListener 中 onEvent方法
    }

    private void initSpeech() throws Exception {
        ss = SpeechSynthesizer.getInstance();
        ss.setContext(context);
        ss.setAppId(APP_ID);
        ss.setApiKey(API_KEY, SECRET_KEY);
        ss.setParam(SpeechSynthesizer.PARAM_SPEAKER, "4");
        ss.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, FileUtil.copyAssetsFile(context, BAIDU_MODEL_FILE));
        ss.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, FileUtil.copyAssetsFile(context, BAIDU_TEXT_FILE));
        ss.setSpeechSynthesizerListener(speechSynthesizerListener);
        ss.initTts(TtsMode.MIX);
    }

    public void startWakeUp() {
        WakeUpStart wakeUpStart = new WakeUpStart().setKwsFile("assets://WakeUpBaidu.bin").setAppid(APP_ID);
        wakeup.send(SpeechConstant.WAKEUP_START, GsonUtil.getGson().toJson(wakeUpStart), null, 0, 0);
    }

    private void startAsr() {
        synchronized (asrRunLock) {
            asrRun = true;
            wakeup.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0); //
//            HashMap<String,Object> map = new HashMap<>();
//            map.put(SpeechConstant.DECODER, 2);
//// 0:在线 2.离在线融合(在线优先)
//            map.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "/sdcard/yourpath/baidu_speech_grammar.bsg");
//            JSONObject json = new JSONObject();
//            json.put("name", new JSONArray().put("王自强").put("叶问")).put("appname", new JSONArray().put("手百").put("度秘"));
//            map.put(SpeechConstant.SLOT_DATA, json.toString());
//            //map.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "/sdcard/yourpath/baidu_speech_grammar.bsg");
//            asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE,new JSONObject(map).toString());
            asr.send(SpeechConstant.ASR_START, GsonUtil.getGson().toJson(new AsrStart()), null, 0, 0);
            ss.speak("你好,请讲");
            EventBus.getDefault().post(new MVaAsrStateChange().setRun(true));
        }
    }


    public void stopAsr() {
        synchronized (asrRunLock) {
            asrRun = false;
            asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
            startWakeUp();
            EventBus.getDefault().post(new MVaAsrStateChange().setRun(false));
        }
    }


    private SpeechSynthesizerListener speechSynthesizerListener = new SpeechSynthesizerListenerImpl() {
        @Override
        public void onSpeechStart(String s) {
            if (asrRun) {
                asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
            }
        }

        @Override
        public void onSpeechFinish(String s) {
            if (asrRun) {
                asr.send(SpeechConstant.ASR_START, GsonUtil.getGson().toJson(new AsrStart()), null, 0, 0);
            }
        }
    };

    private EventListener eventListener = new EventListener() {
        @Override
        public void onEvent(String name, String params, byte[] data, int offset, int length) {
            switch (name) {
                case SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS:
                    startAsr();
                    break;
                case SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL:
                    handleAsrPartial(params);
                    break;
            }

            Log.e(getClass().getSimpleName(), "name: " + name);
            Log.e(getClass().getSimpleName(), "params: " + params);
        }
    };


    private void handleAsrPartial(String param) {
        try {
            AsrEventPartial asrEventPartial = GsonUtil.getGson().fromJson(param, AsrEventPartial.class);
            EventBus.getDefault().post(new MVaNewWordFind().setOver("final_result".equals(asrEventPartial.getResult_type())).setWord(asrEventPartial.getBest_result()));
            if ("final_result".equals(asrEventPartial.getResult_type())) {
                TaskExecutor.self().run(() -> {
                    String word = asrEventPartial.getBest_result();
                    Log.e(TAG, "handleAsrPartial: " + word);
                    KeyWord keyWord = getAction(word);
                    Log.e(TAG, "handleAsrPartial: " + keyWord);
                    if (keyWord != null) {
                        switch (keyWord.action) {
                            case ACTION_STOP_ASR: {
                                Log.e(TAG, "onEvent: ACTION_STOP_ASR");
                                ss.speak("再见");
                                stopAsr();
                                break;
                            }
                            case ACTION_NAV_COMP: {
                                AMapCarPlugin.self().getComp();
                                Log.e(TAG, "onEvent: ACTION_NAV_COMP");
                                ss.speak("正在发起导航");
                                stopAsr();
                                break;
                            }
                            case ACTION_NAV_HOME: {
                                AMapCarPlugin.self().getHome();
                                Log.e(TAG, "onEvent: ACTION_NAV_HOME");
                                ss.speak("回家喽");
                                stopAsr();
                                break;
                            }
                            case ACTION_MUSIC_NEXT: {
                                MusicPlugin.self().next();
                                Log.e(TAG, "onEvent: ACTION_MUSIC_NEXT");
                                ss.speak("播放下一首歌");
                                stopAsr();
                                break;
                            }
                            case ACTION_MUSIC_PLAY: {
                                MusicPlugin.self().playOrPause();
                                Log.e(TAG, "onEvent: ACTION_MUTE");
                                ss.speak("music");
                                stopAsr();
                                break;
                            }
                            case ACTION_MUSIC_PREV: {
                                MusicPlugin.self().pre();
                                Log.e(TAG, "onEvent: ACTION_MUSIC_PREV");
                                ss.speak("播放上一首歌");
                                stopAsr();
                                break;
                            }
                            case ACTION_SMUTE: {
                                ConsolePlugin.self().mute();
                                Log.e(TAG, "onEvent: ACTION_MUTE");
                                ss.speak("好的");
                                stopAsr();
                            }
                            case ACTION_SXIAO: {
                                ConsolePlugin.self().decVolume();
                                Log.e(TAG, "onEvent: ACTION_MUTE");
                                ss.speak("好的");
                                stopAsr();
                            }
                            case ACTION_SDA: {
                                ConsolePlugin.self().incVolume();
                                Log.e(TAG, "onEvent: ACTION_MUTE");
                                ss.speak("好的");
                                stopAsr();
                            }
                            case ACTION_TOHOME: {
                                EventBus.getDefault().post(new LItemToFristEvent());
                                Log.e(TAG, "onEvent: ACTION_MUTE");
                                ss.speak("好的");
                                stopAsr();
                            }
                            default:
                                ss.speak("我不太明白");
                        }
                    }
                }, 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initKey() {
        keyWords = new KeyWord[]{
                new KeyWord("关闭吧|没事了|退出吧|取消吧", CONTAINS, ACTION_STOP_ASR),
                new KeyWord("安静|闭嘴|静音", CONTAINS, ACTION_SMUTE),
                new KeyWord("小点声|减小音量", CONTAINS, ACTION_SXIAO),
                new KeyWord("打开主页", CONTAINS, ACTION_TOHOME),
                new KeyWord("播放音乐|听歌", CONTAINS, ACTION_MUSIC_PLAY),
                new KeyWord("换首歌", EQ, ACTION_MUSIC_NEXT),
                new KeyWord(new String[]{"下,首歌"}, ALL_CONTAINS, ACTION_MUSIC_NEXT),
                new KeyWord(new String[]{"上,首歌"}, ALL_CONTAINS, ACTION_MUSIC_PREV),
                new KeyWord("回家", EQ, ACTION_NAV_HOME),
                new KeyWord("我要回家", EQ, ACTION_NAV_HOME),
                new KeyWord("回公司,去公司", CONTAINS, ACTION_NAV_COMP),
                new KeyWord("打开", START_WIDTH, ACTION_OPEN_APP),
        };
    }

    private KeyWord getAction(String key) {
        if (CommonUtil.isNull(key)) {
            return null;
        }
        try {
            for (KeyWord keyWord : keyWords) {
                switch (keyWord.type) {
                    case EQ: {
                        if (keyWord.key.equals(key)) {
                            return keyWord;
                        }
                        break;
                    }
                    case START_WIDTH: {
                        if (keyWord.key.startsWith(key)) {
                            return keyWord;
                        }
                        break;
                    }
                    case CONTAINS: {
                        if (keyWord.key.contains(key)) {
                            return keyWord;
                        }
                        break;
                    }
                    case ALL_CONTAINS: {
                        boolean use = true;
                        for (String k : keyWord.keys) {
                            if (!key.contains(k)) {
                                use = false;
                                break;
                            }
                        }
                        if (use) {
                            return keyWord;
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private final static int ACTION_STOP_ASR = 1;//停止
    private final static int ACTION_NAV_HOME = 2;//导航回家
    private final static int ACTION_NAV_COMP = 3;//导航去公司
    private final static int ACTION_MUSIC_NEXT = 4;//下一首歌
    private final static int ACTION_MUSIC_PREV = 5;//上一首歌
    private final static int ACTION_SMUTE = 6;//静音
    private final static int ACTION_OPEN_APP = 7;//打开某个APP
    private final static int ACTION_MUSIC_PLAY = 8;//播放音乐
    private final static int ACTION_SXIAO = 9;//静音
    private final static int ACTION_SDA = 10;//静音
    private final static int ACTION_TOHOME = 11;//静音

    private final static int EQ = 1;//关键次等于语音命令
    private final static int CONTAINS = 2;//关键词包含语音命令
    private final static int START_WIDTH = 3;//关键次开始的词语为语音命令
    private final static int ALL_CONTAINS = 4;//语音命令包含多个关键词

    static class KeyWord {
        String key;
        String[] keys;
        int type;
        int action;

        public KeyWord(String key, int type, int action) {
            this.key = key;
            this.type = type;
            this.action = action;
        }

        public KeyWord(String[] key, int type, int action) {
            this.keys = key;
            this.type = type;
            this.action = action;
        }

        @Override
        public String toString() {
            return "KeyWord{" +
                    "key='" + key + '\'' +
                    ", keys=" + Arrays.toString(keys) +
                    ", type=" + type +
                    ", action=" + action +
                    '}';
        }
    }
}
