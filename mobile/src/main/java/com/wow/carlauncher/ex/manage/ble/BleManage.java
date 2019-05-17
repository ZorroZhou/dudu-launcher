package com.wow.carlauncher.ex.manage.ble;

import android.annotation.SuppressLint;
import android.content.Context;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.ex.ContextEx;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

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
        long t1 = System.currentTimeMillis();

        setContext(context);
        bluetoothClient = new BluetoothClient(context);
        listeners = new HashMap<>();
        markMacMap = new HashMap<>();
        statusListener = new HashMap<>();
        options = new BleConnectOptions.Builder()
                .setConnectTimeout(3000)   // 连接超时5s
                .setServiceDiscoverTimeout(2000)  // 发现服务超时5s
                .build();

        LogEx.d(this, "init time:" + (System.currentTimeMillis() - t1));
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
        LogEx.d(this, "disconnect:" + mark);
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
            LogEx.d(this, "ble not supported or not open");
            BleListener bleListener = listeners.get(mark);
            if (bleListener != null) {
                bleListener.connect(false);
            }
            return;
        }
        LogEx.d(this, "connect:" + mark);
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
        TaskExecutor.self().run(() -> bluetoothClient.connect(mac, options, (code, data) -> {
            if (code == REQUEST_SUCCESS) {
                LogEx.d(BleManage.this, mac + " connect success");
                bluetoothClient.notify(mac,
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
                                if (bleListener != null) {
                                    if (code == REQUEST_SUCCESS) {
                                        LogEx.d(BleManage.this, mac + " find service success");
                                        bleListener.connect(true);
                                    } else {
                                        LogEx.d(BleManage.this, mac + " find service fail");
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
                LogEx.d(BleManage.this, mac + "connect fail");
                if (bleListener != null) {
                    bleListener.connect(false);
                }
                disconnect(mark);
                bluetoothClient.disconnect(mac);
            }
        }));
    }

    public int getConnectStatus(String mac) {
        return bluetoothClient.getConnectStatus(mac);
    }
}
