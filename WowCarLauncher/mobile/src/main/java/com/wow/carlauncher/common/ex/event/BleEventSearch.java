package com.wow.carlauncher.common.ex.event;

/**
 * Created by 10124 on 2018/4/22.
 */

public class BleEventSearch {
    private boolean search;

    public boolean isSearch() {
        return search;
    }

    public BleEventSearch setSearch(boolean search) {
        this.search = search;
        return this;
    }
}
