package com.wow.carlauncher.plugin.obd;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.obd.protocol.GoodDriverTPProtocol;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;

import static com.wow.carlauncher.common.CommonData.SDATA_OBD_CONTROLLER;

/**
 * Created by 10124 on 2017/11/4.
 */

public class ObdPlugin extends BasePlugin<ObdPluginListener> {

    private static ObdPlugin self;

    public static ObdPlugin self() {
        if (self == null) {
            self = new ObdPlugin();
        }
        return self;
    }

    private ObdPlugin() {

    }

    public void init(Context context) {
        super.init(context);
        start();

    }

    private ObdProtocol obdProtocol;

    private ObdPluginListener obdPluginListener = new ObdPluginListener() {
        @Override
        public void connect(final boolean success) {
            runListener(new ListenerRuner<ObdPluginListener>() {
                @Override
                public void run(ObdPluginListener obdPluginListener) {
                    obdPluginListener.connect(success);
                }
            });
        }

        @Override
        public void carRunningInfo(final Integer speed, final Integer rev, final Integer waterTemp, final Float oilConsumption) {
            runListener(new ListenerRuner<ObdPluginListener>() {
                @Override
                public void run(ObdPluginListener obdPluginListener) {
                    obdPluginListener.carRunningInfo(speed, rev, waterTemp, oilConsumption);
                }
            });
        }

        @Override
        public void carTirePressureInfo(final Float lFTirePressure, final Integer lFTemp,
                                        final Float rFTirePressure, final Integer rFTemp,
                                        final Float lBTirePressure, final Integer lBTemp,
                                        final Float rBTirePressure, final Integer rBTemp) {
            runListener(new ListenerRuner<ObdPluginListener>() {
                @Override
                public void run(ObdPluginListener obdPluginListener) {
                    obdPluginListener.carTirePressureInfo(lFTirePressure, lFTemp, rFTirePressure, rFTemp, lBTirePressure, lBTemp, rBTirePressure, rBTemp);
                }
            });
        }
    };


    public synchronized void start() {
        stop();
        final String address = SharedPreUtil.getSharedPreString(CommonData.SDATA_OBD_ADDRESS);
        if (CommonUtil.isNotNull(address)) {
            ObdProtocolEnum p1 = ObdProtocolEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_OBD_CONTROLLER, ObdProtocolEnum.YJ_TYB.getId()));
            switch (p1) {
                case YJ_TYB: {
                    obdProtocol = new GoodDriverTPProtocol(context, address, obdPluginListener);
                    break;
                }
                default:
                    obdProtocol = new GoodDriverTPProtocol(context, address, obdPluginListener);
                    break;
            }
            obdProtocol.run();
        }
    }

    public synchronized void stop() {
        if (obdProtocol != null) {
            obdProtocol.stop();
        }
    }

    public boolean supportTp() {
        if (obdProtocol != null) {
            return obdProtocol.supportTp();
        }
        return false;
    }

}
