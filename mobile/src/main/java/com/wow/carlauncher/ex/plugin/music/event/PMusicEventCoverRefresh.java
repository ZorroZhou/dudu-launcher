package com.wow.carlauncher.ex.plugin.music.event;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PMusicEventCoverRefresh {
    public final static int TYPE_NONE = 1;
    public final static int TYPE_URL = 2;
    public final static int TYPE_BASE64 = 3;

    private String url;
    private int coverType;
    private String coverBas64;

    public String getUrl() {
        return url;
    }

    public PMusicEventCoverRefresh setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getCoverBas64() {
        return coverBas64;
    }

    public PMusicEventCoverRefresh setCoverBas64(String coverBas64) {
        this.coverBas64 = coverBas64;
        return this;
    }

    public int getCoverType() {
        return coverType;
    }

    public PMusicEventCoverRefresh setCoverType(int coverType) {
        this.coverType = coverType;
        return this;
    }
}
