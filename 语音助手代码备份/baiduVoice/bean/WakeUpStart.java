package com.wow.carlauncher.ex.manage.baiduVoice.bean;

import com.baidu.speech.asr.SpeechConstant;
import com.google.gson.annotations.SerializedName;

public class WakeUpStart {

    @SerializedName(SpeechConstant.WP_WORDS_FILE)
    private String kwsFile;

    @SerializedName(SpeechConstant.ACCEPT_AUDIO_VOLUME)
    private boolean acceptAudioVolume = false;

    @SerializedName(SpeechConstant.APP_ID)
    private String appid;

    public String getKwsFile() {
        return kwsFile;
    }

    public WakeUpStart setKwsFile(String kwsFile) {
        this.kwsFile = kwsFile;
        return this;
    }

    public boolean getAcceptAudioVolume() {
        return acceptAudioVolume;
    }

    public WakeUpStart setAcceptAudioVolume(boolean acceptAudioVolume) {
        this.acceptAudioVolume = acceptAudioVolume;
        return this;
    }

    public String getAppid() {
        return appid;
    }

    public WakeUpStart setAppid(String appid) {
        this.appid = appid;
        return this;
    }
}
