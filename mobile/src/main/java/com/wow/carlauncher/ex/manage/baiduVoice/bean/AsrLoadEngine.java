package com.wow.carlauncher.ex.manage.baiduVoice.bean;

import com.baidu.speech.asr.SpeechConstant;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class AsrLoadEngine {
//    HashMap<String,Object> map = new HashMap<>();
//            map.put(SpeechConstant.DECODER, 2);
//// 0:在线 2.离在线融合(在线优先)
//            map.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "/sdcard/yourpath/baidu_speech_grammar.bsg");
//    JSONObject json = new JSONObject();
//            json.put("name", new JSONArray().put("王自强").put("叶问")).put("appname", new JSONArray().put("手百").put("度秘"));
//            map.put(SpeechConstant.SLOT_DATA, json.toString());

    @SerializedName(SpeechConstant.DECODER)
    private int acceptAudioVolume = 2;

    @SerializedName(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH)
    private String filePath;

    @SerializedName(SpeechConstant.SLOT_DATA)
    private String slotData;

    public int getAcceptAudioVolume() {
        return acceptAudioVolume;
    }

    public AsrLoadEngine setAcceptAudioVolume(int acceptAudioVolume) {
        this.acceptAudioVolume = acceptAudioVolume;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public AsrLoadEngine setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getSlotData() {
        return slotData;
    }

    public AsrLoadEngine setSlotData(String slotData) {
        this.slotData = slotData;
        return this;
    }
}
