package com.wow.carlauncher.ex.plugin.obd;

/**
 * Created by 10124 on 2018/3/29.
 */

public interface ObdProtocolListener {
    void write(byte[] req);

    boolean isConnect();

    //汽车运行信息回掉
    void carRunningInfo(Integer speed,
                        Integer rev,
                        Integer waterTemp,
                        Integer oilConsumption);

    //汽车胎压回掉
    void carTirePressureInfo(Float lFTirePressure,
                             Integer lFTemp,
                             Float rFTirePressure,
                             Integer rFTemp,
                             Float lBTirePressure,
                             Integer lBTemp,
                             Float rBTirePressure,
                             Integer rBTemp);
}
