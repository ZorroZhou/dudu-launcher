package com.wow.carlauncher.ex.manage.baiduVoice.event;

public class MVaNewWordFind {
    private String word;
    private boolean over;

    public String getWord() {
        return word;
    }

    public MVaNewWordFind setWord(String word) {
        this.word = word;
        return this;
    }

    public boolean isOver() {
        return over;
    }

    public MVaNewWordFind setOver(boolean over) {
        this.over = over;
        return this;
    }
}
