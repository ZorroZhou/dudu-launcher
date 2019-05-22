package com.wow.carlauncher.repertory.server.response;

public class AppUpdateRes {
    private Integer version;
    private String about;
    private String url;

    public Integer getVersion() {
        return version;
    }

    public AppUpdateRes setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public String getAbout() {
        return about;
    }

    public AppUpdateRes setAbout(String about) {
        this.about = about;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public AppUpdateRes setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String toString() {
        return "AppUpdateRes{" +
                "version=" + version +
                ", about='" + about + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

}
