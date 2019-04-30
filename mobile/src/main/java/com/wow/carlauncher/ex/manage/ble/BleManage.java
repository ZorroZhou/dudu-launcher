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
import com.wow.carlauncher.ex.manage.ble.event.BMEventFindDevice;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        deviceList = new ArrayList<>();
        listeners = new HashMap<>();
        markMacMap = new HashMap<>();
        statusListener = new HashMap<>();
        options = new BleConnectOptions.Builder()
                .setConnectRetry(2)
                .setConnectTimeout(5000)   // 连接超时5s
                .setServiceDiscoverRetry(5)
                .setServiceDiscoverTimeout(5000)  // 发现服务超时5s
                .build();

        EventBus.getDefault().register(this);
        startTask();
    }


    private BleConnectOptions options;

    private BluetoothClient bluetoothClient;

    private List<SearchResult> deviceList;

    private boolean running = false;

    private boolean taskRun = false;

    private Map<String, BleListener> listeners;

    Map<String, BleListener> getListeners() {
        return listeners;
    }

    private Map<String, String> markMacMap;

    private Map<String, BleConnectStatusListener> statusListener;

    public boolean startTask() {
        if (bluetoothClient.isBleSupported() && bluetoothClient.isBluetoothOpened()) {
            taskRun = true;
            Log.e(TAG + getClass().getSimpleName(), "支持蓝牙并打开 ");
            return true;
        } else {
            Log.e(TAG + getClass().getSimpleName(), "不支持蓝牙或者蓝牙未打开 ");
            return false;
        }
    }

    public void stopTask() {
        taskRun = false;
        bluetoothClient.stopSearch();
    }

    private void start() {
        if (bluetoothClient.isBleSupported() && bluetoothClient.isBluetoothOpened()) {
            running = true;
            final SearchRequest request = new SearchRequest.Builder()
                    .searchBluetoothLeDevice(3000)//开始扫描蓝牙设备每次3s
                    .build();
            deviceList.clear();
            bluetoothClient.search(request, new SearchResponse() {
                @Override
                public void onSearchStarted() {
                }

                @Override
                public void onDeviceFounded(SearchResult device) {
                    deviceList.add(device);
                }

                @Override
                public void onSearchStopped() {
                    running = false;
                    EventBus.getDefault().post(new BMEventFindDevice().setDeviceList(deviceList));
                }

                @Override
                public void onSearchCanceled() {

                }
            });
        }
    }

    public BluetoothClient getClient() {
        return bluetoothClient;
    }

    public void disconnect(String mark) {
        statusListener.remove(mark);
        if (markMacMap.containsKey(mark)) {
            bluetoothClient.disconnect(markMacMap.get(mark));
        }
    }

    public void addListener(BleListener listener) {
        listeners.put(listener.getMark(), listener);
    }

    public void removeListener(BleListener listener) {
        listeners.remove(listener.getMark());
    }

    public void connect(String mark, String mac, UUID notifyService, UUID notifyCharacter) {
        disconnect(mark);
        bluetoothClient.clearRequest(mac, 0);
        bluetoothClient.refreshCache(mac);
        BleConnectStatusListener listener = statusListener.get(mark);
        if (listener != null) {
            bluetoothClient.unregisterConnectStatusListener(mac, listener);
            statusListener.remove(mark);
        }
        MyBleConnectStatusListener myBleConnectStatusListener = new MyBleConnectStatusListener(mark, this);
        bluetoothClient.registerConnectStatusListener(mac, myBleConnectStatusListener);

        markMacMap.put(mark, mac);

        final BleListener bleListener = listeners.get(mark);
        bluetoothClient.connect(mac, options, (code, data) -> {
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
                                        bluetoothClient.disconnect(mac);
                                    }
                                }
                            }
                        });
            } else {
                if (bleListener != null) {
                    bleListener.connect(false);
                }
                Log.d(TAG, "onResponse: OBD连接失败!!!");
            }
        });
    }

    public int getConnectStatus(String mac) {
        return bluetoothClient.getConnectStatus(mac);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final MTimeSecondEvent event) {
        if (taskRun && !running) {
            start();
        }
    }
}
