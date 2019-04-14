package com.wow.carlauncher.ex.manage.ble;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.wow.carlauncher.ex.ContextEx;

import org.xutils.x;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/22.
 */

public class BleManage extends ContextEx {
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static BleManage instance = new BleManage();
    }

    public static BleManage self() {
        return SingletonHolder.instance;
    }

    private BleManage() {
        super();
    }

    public void init(Context context) {
        setContext(context);
        bluetoothClient = new BluetoothClient(context);
    }

    private BluetoothClient bluetoothClient;

    public BluetoothClient client() {
        return bluetoothClient;
    }

    public void searchBle(final MySearchResponse response) {
        final SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(Integer.MAX_VALUE)   // 先扫BLE设备3次，每次3s
                .build();

        x.task().post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: " + response);
                bluetoothClient.search(request, new SearchResponse() {
                    @Override
                    public void onSearchStarted() {

                    }

                    @Override
                    public void onDeviceFounded(SearchResult device) {
                        response.onDeviceFounded(device);
                    }

                    @Override
                    public void onSearchStopped() {

                    }

                    @Override
                    public void onSearchCanceled() {

                    }
                });
            }
        });
    }

    public void stopSearch() {
        bluetoothClient.stopSearch();
    }
}
