package com.wow.carlauncher.ex.manage.ble;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.wow.carlauncher.ex.ContextEx;

import org.xutils.x;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
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
        listeners = new HashMap<>();
        markMacMap = new HashMap<>();
        statusListener = new HashMap<>();
        options = new BleConnectOptions.Builder()
                .setConnectTimeout(5000)   // 连接超时5s
                .setServiceDiscoverTimeout(5000)  // 发现服务超时5s
                .build();
    }


    private BleConnectOptions options;

    private BluetoothClient bluetoothClient;

    private Map<String, BleListener> listeners;

    Map<String, BleListener> getListeners() {
        return listeners;
    }

    private Map<String, String> markMacMap;

    private Map<String, BleConnectStatusListener> statusListener;
    private boolean search = false;

    public void startSearch(BleSearchResponse response) {
        if (bluetoothClient.isBleSupported() && bluetoothClient.isBluetoothOpened() && !search) {
            search = true;
            final SearchRequest request = new SearchRequest.Builder()
                    .searchBluetoothLeDevice(3000, 10)//开始扫描蓝牙设备每次3s
                    .build();
            bluetoothClient.search(request, new SearchResponse() {
                @Override
                public void onSearchStarted() {
                    response.onSearchStarted();
                }

                @Override
                public void onDeviceFounded(SearchResult device) {
                    response.onDeviceFounded(device);
                }

                @Override
                public void onSearchStopped() {
                    response.onSearchStopped();
                    search = false;
                }

                @Override
                public void onSearchCanceled() {
                    response.onSearchCanceled();
                    search = false;
                }
            });
        }
    }

    public void stopSearch() {
        bluetoothClient.stopSearch();
    }


    public BluetoothClient getClient() {
        return bluetoothClient;
    }

    public void disconnect(String mark) {
        statusListener.remove(mark);
        if (markMacMap.containsKey(mark)) {
            bluetoothClient.disconnect(markMacMap.get(mark));
            markMacMap.remove(mark);
        }
    }

    public void addListener(BleListener listener) {
        listeners.put(listener.getMark(), listener);
    }

    public void removeListener(BleListener listener) {
        listeners.remove(listener.getMark());
    }

    public void connect(String mark, String mac, UUID notifyService, UUID notifyCharacter, BleConnectStatusListener myBleConnectStatusListener) {
        if (!bluetoothClient.isBleSupported() || !bluetoothClient.isBluetoothOpened()) {
            BleListener bleListener = listeners.get(mark);
            if (bleListener != null) {
                bleListener.connect(false);
            }
            return;
        }
        disconnect(mark);
        bluetoothClient.clearRequest(mac, 0);
        bluetoothClient.refreshCache(mac);
        BleConnectStatusListener listener = statusListener.get(mark);
        if (listener != null) {
            bluetoothClient.unregisterConnectStatusListener(mac, listener);
            statusListener.remove(mark);
        }
        statusListener.put(mark, myBleConnectStatusListener);
        bluetoothClient.registerConnectStatusListener(mac, myBleConnectStatusListener);

        markMacMap.put(mark, mac);

        final BleListener bleListener = listeners.get(mark);
        x.task().run(() -> bluetoothClient.connect(mac, options, (code, data) -> {
            if (code == REQUEST_SUCCESS) {
                BleManage.self().getClient().notify(mac,
                        notifyService,
                        notifyCharacter,
                        new BleNotifyResponse() {
                            @Override
                            public void onNotify(UUID service, UUID character, byte[] msg) {
                                if (bleListener != null) {
                                    if (service.equals(notifyService) && character.equals(notifyCharacter)) {
                                        bleListener.receiveMessage(msg);
                                    } else {
                                        bleListener.receiveMessage(service, character, msg);
                                    }
                                }
                            }

                            @Override
                            public void onResponse(int code) {
                                Log.d(TAG, "onResponse: " + code);
                                if (bleListener != null) {
                                    if (code == REQUEST_SUCCESS) {
                                        bleListener.connect(true);
                                    } else {
                                        bleListener.connect(false);

                                    }
                                }
                                if (code != REQUEST_SUCCESS) {
                                    disconnect(mark);
                                    bluetoothClient.disconnect(mac);
                                }
                            }
                        });
            } else {
                if (bleListener != null) {
                    bleListener.connect(false);
                }
                disconnect(mark);
                bluetoothClient.disconnect(mac);
                Log.d(TAG, "onResponse: 连接失败!!!");
            }
        }));
    }

    public int getConnectStatus(String mac) {
        return bluetoothClient.getConnectStatus(mac);
    }
}
