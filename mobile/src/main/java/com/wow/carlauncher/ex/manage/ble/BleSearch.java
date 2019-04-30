package com.wow.carlauncher.ex.manage.ble;

import com.inuker.bluetooth.library.search.SearchResult;
import com.wow.carlauncher.ex.manage.ble.event.BMEventFindDevice;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by 10124 on 2018/4/22.
 */

public abstract class BleSearch {
    public BleSearch() {
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final BMEventFindDevice event) {
        findDevice(event.getDeviceList());
    }

    public abstract void findDevice(List<SearchResult> deviceList);

    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
