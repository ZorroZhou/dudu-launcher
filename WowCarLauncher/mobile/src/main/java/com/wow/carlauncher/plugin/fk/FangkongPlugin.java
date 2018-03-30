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

import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

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
        Toast.makeText(getContext(), "尝试连接方控", Toast.LENGTH_LONG).show();
        connectFangkong();

        options = new BleConnectOptions.Builder()
                .setConnectRetry(Integer.MAX_VALUE)
                .setConnectTimeout(3000)   // 连接超时30s
                .setServiceDiscoverRetry(Integer.MAX_VALUE)
                .setServiceDiscoverTimeout(2000)  // 发现服务超时20s
                .build();


    }

    private FangkongProtocol fangkongProtocol;

    public synchronized void connectFangkong() {
        if (fangkongProtocol != null) {
            AppContext.self().getBluetoothClient().disconnect(fangkongProtocol.getAddress());
        }
        final String fkaddress = SharedPreUtil.getSharedPreString(CommonData.SDATA_FANGKONG_ADDRESS);
        if (CommonUtil.isNotNull(fkaddress)) {
            fangkongProtocol = new YiLianProtocol(fkaddress, context);

            AppContext.self().getBluetoothClient().connect(fangkongProtocol.getAddress(), options, new BleConnectResponse() {
                @Override
                public void onResponse(int code, BleGattProfile data) {
                    if (code == REQUEST_SUCCESS) {
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
                        runListener(new ListenerRuner<FangkongPluginListener>() {
                            @Override
                            public void run(FangkongPluginListener fangkongPluginListener) {
                                fangkongPluginListener.connect(false);
                            }
                        });
                    }
                }
            });
        }
    }


}
