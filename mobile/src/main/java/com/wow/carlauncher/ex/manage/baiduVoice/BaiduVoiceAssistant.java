package com.wow.carlauncher.ex.manage.baiduVoice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.AppUtil;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.baiduVoice.bean.AsrEventParam;
import com.wow.carlauncher.ex.manage.baiduVoice.bean.AsrEventPartial;
import com.wow.carlauncher.ex.manage.baiduVoice.bean.AsrStart;
import com.wow.carlauncher.ex.manage.baiduVoice.bean.WakeUpStart;
import com.wow.carlauncher.ex.manage.baiduVoice.event.MVaAsrStateChange;
import com.wow.carlauncher.ex.manage.baiduVoice.event.MVaNewWordFind;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.ex.plugin.console.ConsolePlugin;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;

import java.util.Arrays;

import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.AMAP_PACKAGE;

public class BaiduVoiceAssistant extends ContextEx {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static BaiduVoiceAssistant instance = new BaiduVoiceAssistant();
    }

    public static BaiduVoiceAssistant self() {
        return BaiduVoiceAssistant.SingletonHolder.instance;
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
    private boolean init = false;

    private boolean asrRun = false;
    private static final byte[] asrRunLock = new byte[0];

    private KeyWord[] keyWords;

    public void init(Context context) {
        setContext(context);
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
            wakeup.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0);
            stopAsr();
        }
    }


    private void initVoice() {
        wakeup = EventManagerFactory.create(getContext(), BAIDU_WAKE_UP);
        // 基于SDK唤醒词集成1.3 注册输出事件
        wakeup.registerListener(eventListener); //  EventListener 中 onEvent方法

        asr = EventManagerFactory.create(getContext(), BAIDU_ASR);
        asr.registerListener(eventListener); //  EventListener 中 onEvent方法
    }

    private void initSpeech() throws Exception {
        ss = SpeechSynthesizer.getInstance();
        ss.setContext(getContext());
        ss.setAppId(APP_ID);
        ss.setApiKey(API_KEY, SECRET_KEY);
        ss.setParam(SpeechSynthesizer.PARAM_SPEAKER, "4");
        ss.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, FileUtil.copyAssetsFile(getContext(), BAIDU_MODEL_FILE));
        ss.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, FileUtil.copyAssetsFile(getContext(), BAIDU_TEXT_FILE));
        ss.setSpeechSynthesizerListener(speechSynthesizerListener);
        ss.initTts(TtsMode.MIX);
    }

    public void startWakeUp() {
        if (init) {
            WakeUpStart wakeUpStart = new WakeUpStart().setKwsFile("assets://WakeUp.bin").setAppid(APP_ID);
            wakeup.send(SpeechConstant.WAKEUP_START, GsonUtil.getGson().toJson(wakeUpStart), null, 0, 0);
        }
    }

    public void startAsr() {
        synchronized (asrRunLock) {
            asrRun = true;
            wakeup.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0); //
            asr.send(SpeechConstant.ASR_START, GsonUtil.getGson().toJson(new AsrStart()), null, 0, 0);
            ss.speak("你好");
            postEvent(new MVaAsrStateChange().setRun(true));
        }
    }

    public void stopAsr() {
        synchronized (asrRunLock) {
            asrRun = false;
            asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
            startWakeUp();
            postEvent(new MVaAsrStateChange().setRun(false));
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

    private EventListener eventListener = (name, params, data, offset, length) -> {
        LogEx.d(this, "eventListener.name: " + name);
        LogEx.d(this, "eventListener.params: " + params);
        switch (name) {
            case SpeechConstant.CALLBACK_EVENT_WAKEUP_ERROR:
                ToastManage.self().show("无法启动唤醒功能,可能是权限不足!");
                break;
            case SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS:
                startAsr();
                break;
            case SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL:
                handleAsrPartial(params);
                break;
            case SpeechConstant.CALLBACK_EVENT_ASR_FINISH:
                AsrEventParam asrEventPartial = GsonUtil.getGson().fromJson(params, AsrEventParam.class);
                if (asrEventPartial.getError() != 0) {
                    ToastManage.self().show("出现错误,可能是网络不通畅");
                    stopAsr();
                }
                break;
        }
    };

    private void handleAsrPartial(String param) {
        try {
            AsrEventPartial asrEventPartial = GsonUtil.getGson().fromJson(param, AsrEventPartial.class);
            postEvent(new MVaNewWordFind().setOver("final_result".equals(asrEventPartial.getResult_type())).setWord(asrEventPartial.getBest_result()));
            if ("final_result".equals(asrEventPartial.getResult_type())) {
                TaskExecutor.self().run(() -> {
                    String word = asrEventPartial.getBest_result();
                    LogEx.d(this, "handleAsrPartial: " + word);
                    if (word.endsWith("。") || word.endsWith("？")) {
                        word = word.substring(0, word.length() - 1);
                    }
                    KeyWord keyWord = getAction(word);
                    LogEx.d(this, "handleAsrPartial: " + keyWord);
                    if (keyWord != null) {
                        switch (keyWord.action) {
                            case ACTION_HI: {
                                ss.speak("你好");
                                break;
                            }
                            case ACTION_STOP_ASR: {
                                ss.speak("再见");
                                stopAsr();
                                break;
                            }
                            case ACTION_NAV_COMP: {
                                AMapCarPlugin.self().naviToComp();
                                ss.speak("正在发起导航");
                                stopAsr();
                                break;
                            }
                            case ACTION_NAV_HOME: {
                                AMapCarPlugin.self().naviToHome();
                                ss.speak("回家喽");
                                stopAsr();
                                break;
                            }
                            case ACTION_MUSIC_NEXT: {
                                MusicPlugin.self().next();
                                ss.speak("播放下一首歌");
                                stopAsr();
                                break;
                            }
                            case ACTION_MUSIC_PLAY: {
                                MusicPlugin.self().playOrPause();
                                ss.speak("music");
                                stopAsr();
                                break;
                            }
                            case ACTION_MUSIC_PREV: {
                                MusicPlugin.self().pre();
                                ss.speak("播放上一首歌");
                                stopAsr();
                                break;
                            }
                            case ACTION_SMUTE: {
                                ConsolePlugin.self().mute();
                                ss.speak("好的");
                                stopAsr();
                                break;
                            }
                            case ACTION_SXIAO: {
                                ConsolePlugin.self().decVolume();
                                ss.speak("好的");
                                stopAsr();
                                break;
                            }
                            case ACTION_SDA: {
                                ConsolePlugin.self().incVolume();
                                ss.speak("好的");
                                stopAsr();
                                break;
                            }
                            case ACTION_OPEN_APP: {
                                ss.speak("好的");
                                openApp(word.substring(2));
                                stopAsr();
                                break;
                            }
                            case ACTION_TOHOME: {
                                ss.speak("好的");
                                goHome();
                                stopAsr();
                                break;
                            }


                        }
                    } else {
                        ss.speak("我不太明白");
                    }
                }, 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goHome() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(home);
    }

    private void openApp(String name) {
        if ("主页".equals(name) || "首页".equals(name) || "".equals(name)) {
            goHome();
        } else if ("高德".equals(name) || "导航".equals(name) || "地图".equals(name)) {
            if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
                Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
            } else {
                AppInfoManage.self().openApp(AMAP_PACKAGE);
            }
        } else {
            for (AppInfo appInfo : AppInfoManage.self().getShowAppInfos()) {
                if (appInfo.name.equals(name)) {
                    AppInfoManage.self().openApp(appInfo.clazz);
                    return;
                }
            }
            Toast.makeText(getContext(), "没有找到APP", Toast.LENGTH_SHORT).show();
        }
    }

    private void initKey() {
        keyWords = new KeyWord[]{
                new KeyWord("你好", CONTAINS, ACTION_HI),
                new KeyWord("返回首页", EQ, ACTION_TOHOME),
                new KeyWord("关闭吧|没事了|退出吧|取消吧", CONTAINS, ACTION_STOP_ASR),
                new KeyWord("安静|闭嘴|静音", CONTAINS, ACTION_SMUTE),
                new KeyWord(new String[]{"大", "音量"}, ALL_CONTAINS, ACTION_SDA),
                new KeyWord(new String[]{"小", "音量"}, ALL_CONTAINS, ACTION_SXIAO),
                new KeyWord(new String[]{"大", "声"}, ALL_CONTAINS, ACTION_SDA),
                new KeyWord(new String[]{"小", "声"}, ALL_CONTAINS, ACTION_SXIAO),
                new KeyWord(new String[]{"听", "音乐"}, ALL_CONTAINS, ACTION_MUSIC_PLAY),
                new KeyWord(new String[]{"放", "音乐"}, ALL_CONTAINS, ACTION_MUSIC_PLAY),
                new KeyWord(new String[]{"唱", "歌"}, ALL_CONTAINS, ACTION_MUSIC_PLAY),
                new KeyWord(new String[]{"听", "歌"}, ALL_CONTAINS, ACTION_MUSIC_PLAY),
                new KeyWord(new String[]{"放", "歌"}, ALL_CONTAINS, ACTION_MUSIC_PLAY),
                new KeyWord(new String[]{"换", "歌"}, ALL_CONTAINS, ACTION_MUSIC_NEXT),
                new KeyWord(new String[]{"下,首歌"}, ALL_CONTAINS, ACTION_MUSIC_NEXT),
                new KeyWord(new String[]{"上,首歌"}, ALL_CONTAINS, ACTION_MUSIC_PREV),
                new KeyWord("回家", EQ, ACTION_NAV_HOME),
                new KeyWord("我要回家", EQ, ACTION_NAV_HOME),
                new KeyWord("回公司,去公司", CONTAINS, ACTION_NAV_COMP),
                new KeyWord("打开", START_WIDTH, ACTION_OPEN_APP),
                new KeyWord("启动", START_WIDTH, ACTION_OPEN_APP)
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
                        if (key.startsWith(keyWord.key)) {
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


    private final static int ACTION_HI = 0;//停止
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
