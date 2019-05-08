package com.wow.carlauncher.ex.manage.baiduVoice;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.wow.carlauncher.common.LogEx;

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
      LogEx.e(this, "SpeechSynthesizerListener.onError: " + s);
      LogEx.e(this, "onError: " + speechError.code + "  " + speechError.description);
    }
}
