package com.wow.carlauncher.ex.plugin.dudufm;

public class PDuduFmEventRadioInfo {
    private String title;
    private String cover;
    private String programName;

    public String getProgramName() {
        return programName;
    }

    public PDuduFmEventRadioInfo setProgramName(String programName) {
        this.programName = programName;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PDuduFmEventRadioInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCover() {
        return cover;
    }

    public PDuduFmEventRadioInfo setCover(String cover) {
        this.cover = cover;
        return this;
    }

    @Override
    public String toString() {
        return "PDuduFmEventRadioInfo{" +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }
}
