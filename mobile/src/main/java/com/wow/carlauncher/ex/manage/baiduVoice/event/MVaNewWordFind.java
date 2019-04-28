package com.wow.carlauncher.ex.manage.baiduVoice.event;

public class MVaNewWordFind {
    private String word;
    private boolean checking;

    public String getWord() {
        return word;
    }

    public MVaNewWordFind setWord(String word) {
        this.word = word;
        return this;
    }

    public boolean isChecking() {
        return checking;
    }

    public MVaNewWordFind setChecking(boolean checking) {
        this.checking = checking;
        return this;
    }
}
