package com.wow.carlauncher.ex.manage.ble.event;

import com.inuker.bluetooth.library.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class BMEventFindDevice {
    private List<SearchResult> deviceList = new ArrayList<>(0);

    public List<SearchResult> getDeviceList() {
        return deviceList;
    }

    public BMEventFindDevice setDeviceList(List<SearchResult> deviceList) {
        this.deviceList = deviceList;
        return this;
    }
}
