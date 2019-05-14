package com.wow.carlauncher.ex.plugin.music.event;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PMusicEventCoverRefresh {
    private String url;
    private boolean have;

    public String getUrl() {
        return url;
    }

    public PMusicEventCoverRefresh setUrl(String url) {
        this.url = url;
        return this;
    }

    public boolean isHave() {
        return have;
    }

    public PMusicEventCoverRefresh setHave(boolean have) {
        this.have = have;
        return this;
    }
}
