package com.wow.carlauncher.ex.manage.baiduVoice;

import android.util.Log;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizerListener;

import static com.wow.carlauncher.common.CommonData.TAG;

public class SpeechSynthesizerListenerImpl implements SpeechSynthesizerListener {
    @Override
    public void onSynthesizeStart(String s) {

    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

    }

    @Override
    public void onSynthesizeFinish(String s) {

    }

    @Override
    public void onSpeechStart(String s) {

    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {

    }

    @Override
    public void onSpeechFinish(String s) {

    }

    @Override
    public void onError(String s, SpeechError speechError) {
        Log.e(TAG, "SpeechSynthesizerListener.onError: " + s);
        Log.e(TAG, "onError: " + speechError.code + "  " + speechError.description);
    }
}
