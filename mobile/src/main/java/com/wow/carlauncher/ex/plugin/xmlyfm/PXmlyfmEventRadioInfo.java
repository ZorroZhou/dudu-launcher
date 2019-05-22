package com.wow.carlauncher.ex.plugin.xmlyfm;

public class PXmlyfmEventRadioInfo {
    private boolean run;
    private String title;
    private String cover;
    private String programName;

    public String getProgramName() {
        return programName;
    }

    public PXmlyfmEventRadioInfo setProgramName(String programName) {
        this.programName = programName;
        return this;
    }

    public boolean isRun() {
        return run;
    }

    public PXmlyfmEventRadioInfo setRun(boolean run) {
        this.run = run;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PXmlyfmEventRadioInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCover() {
        return cover;
    }

    public PXmlyfmEventRadioInfo setCover(String cover) {
        this.cover = cover;
        return this;
    }

    @Override
    public String toString() {
        return "PXmlyfmEventRadioInfo{" +
                "run=" + run +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }
}
