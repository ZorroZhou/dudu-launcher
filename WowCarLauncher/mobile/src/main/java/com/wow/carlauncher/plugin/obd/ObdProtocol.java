package com.wow.carlauncher.plugin.obd;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.fk.FangkongPluginListener;
import com.wow.carlauncher.plugin.obd.task.ObdTask;
import com.wow.carlauncher.plugin.obd.task.ObdTaskManage;

import org.xutils.x;

import java.util.ArrayDeque;
import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/3/29.
 */

public abstract class ObdProtocol {
    protected Context context;
    protected ObdPluginListener listener;
    protected ObdTaskManage obdTaskManage;//Obd的任务列队

    protected String address;
    private BleConnectOptions options;

    protected boolean connected = false;

    public ObdProtocol(Context context, String address, ObdPluginListener listener) {
        this.address = address;
        this.context = context;
        this.listener = listener;

        options = new BleConnectOptions.Builder()
                .setConnectRetry(Integer.MAX_VALUE)
                .setConnectTimeout(3000)   // 连接超时30s
                .setServiceDiscoverRetry(Integer.MAX_VALUE)
                .setServiceDiscoverTimeout(2000)  // 发现服务超时20s
                .build();

        obdTaskManage = new ObdTaskManage();
    }

    public abstract void run();

    public void stop() {
        obdTaskManage.destroy();
    }

    public abstract void receiveMessage(byte[] message);

    public abstract UUID getNotifyService();

    public abstract UUID getNotifyCharacter();

    public abstract UUID getWriteService();

    public abstract UUID getWriteCharacter();

    private boolean isConnecting = false;//是否是连接中

    private boolean reConnectAble = true;    //允许重连
    private final BleConnectStatusListener bleConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            //允许重连,同时是断开连接了,同时方控是由参数的,同时是方控的地址
            if (reConnectAble && status != STATUS_CONNECTED && mac.equals(address)) {
                connect();
            }
        }
    };

    protected synchronized void connect() {
        if (isConnecting || address == null) {
            return;
        }
        connected = false;
        isConnecting = true;
        AppContext.self().getBluetoothClient().registerConnectStatusListener(address, bleConnectStatusListener);
        AppContext.self().getBluetoothClient().connect(address, options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                if (code == REQUEST_SUCCESS) {
                    AppContext.self().getBluetoothClient().notify(address,
                            getNotifyService(),
                            getNotifyCharacter(),
                            new BleNotifyResponse() {
                                @Override
                                public void onNotify(UUID service, UUID character, byte[] msg) {
                                    Log.d(TAG, "onNotify: " + bytesToHexString(msg));
                                    receiveMessage(msg);
                                }

                                @Override
                                public void onResponse(int code) {
                                    if (code == REQUEST_SUCCESS) {
                                        Toast.makeText(context, "OBD连接成功", Toast.LENGTH_SHORT).show();
                                        connectCallback(true);
                                    } else {
                                        connectCallback(false);
                                    }
                                    isConnecting = false;
                                    connected = true;
                                }
                            });
                } else {
                    if (AppContext.self().getBluetoothClient().getConnectStatus(address) != STATUS_CONNECTED) {
                        connectCallback(false);
                    } else {
                        Toast.makeText(context, "OBD已经连接", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    protected synchronized void disconnect() {
        reConnectAble = false;
        if (address != null) {
            AppContext.self().getBluetoothClient().unregisterConnectStatusListener(address, bleConnectStatusListener);
            AppContext.self().getBluetoothClient().disconnect(address);
        }
    }

    public synchronized void write(byte[] msg) {
        AppContext.self().getBluetoothClient().write(address, getWriteService(), getWriteCharacter(), msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        });
    }

    private void connectCallback(final boolean success) {
        listener.connect(success);
        if (!success) {
            x.task().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (reConnectAble) {
                        connect();
                    }
                }
            }, 200);
        }
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv + " ");
        }
        return stringBuilder.toString();
    }
}
