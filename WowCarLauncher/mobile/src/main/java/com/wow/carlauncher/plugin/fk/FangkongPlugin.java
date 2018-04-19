package com.wow.carlauncher.plugin.fk;

import android.content.Context;
import android.widget.Toast;

import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.fk.protocol.YiLianProtocol;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;

import org.xutils.x;

import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.wow.carlauncher.common.CommonData.SDATA_FANGKONG_CONTROLLER;

/**
 * Created by 10124 on 2017/11/4.
 */

public class FangkongPlugin extends BasePlugin<FangkongPluginListener> {

    private static FangkongPlugin self;

    public static FangkongPlugin self() {
        if (self == null) {
            self = new FangkongPlugin();
        }
        return self;
    }

    private BleConnectOptions options;

    private FangkongPlugin() {

    }

    public void init(Context context) {
        super.init(context);
        connect();

        options = new BleConnectOptions.Builder()
                .setConnectRetry(Integer.MAX_VALUE)
                .setConnectTimeout(3000)   // 连接超时30s
                .setServiceDiscoverRetry(Integer.MAX_VALUE)
                .setServiceDiscoverTimeout(2000)  // 发现服务超时20s
                .build();
    }

    private FangkongProtocol fangkongProtocol;

    private boolean isConnect = false;

    public synchronized void connect() {
        if (isConnect) {
            return;
        }
        isConnect = true;
        if (fangkongProtocol != null) {
            AppContext.self().getBluetoothClient().disconnect(fangkongProtocol.getAddress());
        }
        final String fkaddress = SharedPreUtil.getSharedPreString(CommonData.SDATA_FANGKONG_ADDRESS);
        if (CommonUtil.isNotNull(fkaddress)) {
            FangkongProtocolEnum p1 = FangkongProtocolEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_FANGKONG_CONTROLLER, FangkongProtocolEnum.YLFK.getId()));
            switch (p1) {
                case YLFK: {
                    fangkongProtocol = new YiLianProtocol(fkaddress, context);
                    break;
                }
                default:
                    fangkongProtocol = new YiLianProtocol(fkaddress, context);
                    break;
            }
            AppContext.self().getBluetoothClient().connect(fangkongProtocol.getAddress(), options, new BleConnectResponse() {
                @Override
                public void onResponse(int code, BleGattProfile data) {
                    isConnect = false;
                    if (code == REQUEST_SUCCESS) {
                        Toast.makeText(context, "方控连接成功", Toast.LENGTH_SHORT).show();
                        AppContext.self().getBluetoothClient().notify(fangkongProtocol.getAddress(),
                                fangkongProtocol.getService(),
                                fangkongProtocol.getCharacter(),
                                new BleNotifyResponse() {
                                    @Override
                                    public void onNotify(UUID service, UUID character, byte[] msg) {
                                        if (fangkongProtocol != null) {
                                            fangkongProtocol.receiveMessage(msg);
                                        }
                                    }

                                    @Override
                                    public void onResponse(int code) {
                                        if (code == REQUEST_SUCCESS) {
                                            runListener(new ListenerRuner<FangkongPluginListener>() {
                                                @Override
                                                public void run(FangkongPluginListener fangkongPluginListener) {
                                                    fangkongPluginListener.connect(true);
                                                }
                                            });
                                        }
                                    }
                                });
                    } else {
                        if (AppContext.self().getBluetoothClient().getConnectStatus(fangkongProtocol.getAddress()) != STATUS_CONNECTED) {
                            runListener(new ListenerRuner<FangkongPluginListener>() {
                                @Override
                                public void run(FangkongPluginListener fangkongPluginListener) {
                                    fangkongPluginListener.connect(false);
                                }
                            });
                            x.task().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    connect();
                                }
                            }, 200);
                        } else {
                            Toast.makeText(context, "方控已经连接", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else {
            isConnect = false;
            Toast.makeText(context, "请到设置里进行方控的绑定", Toast.LENGTH_SHORT).show();
        }
    }

    public synchronized void disconnect() {
        if (fangkongProtocol != null) {
            AppContext.self().getBluetoothClient().disconnect(fangkongProtocol.getAddress());
        }
    }

}
