package com.wow.musicapi.provider.qq;

enum QQSongQuality {
    High("M800", "mp3"), Medium("M500", "mp3"), Low("C400", "m4a");

    private String prefix;
    private String suffix;

    QQSongQuality(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}
