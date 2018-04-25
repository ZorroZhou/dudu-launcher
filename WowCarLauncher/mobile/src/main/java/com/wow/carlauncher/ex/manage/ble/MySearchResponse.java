package com.wow.carlauncher.ex.manage.ble;

import com.inuker.bluetooth.library.search.SearchResult;

/**
 * Created by 10124 on 2018/4/25.
 */

public interface MySearchResponse {
    void onDeviceFounded(SearchResult device);
}
