package com.wow.carlauncher.ex.manage.baiduVoice.bean;

public class EventParam {

    private String errorDesc;

    private int errorCode;

    private String word;

    public String getErrorDesc() {
        return errorDesc;
    }

    public EventParam setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
        return this;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public EventParam setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getWord() {
        return word;
    }

    public EventParam setWord(String word) {
        this.word = word;
        return this;
    }
}
