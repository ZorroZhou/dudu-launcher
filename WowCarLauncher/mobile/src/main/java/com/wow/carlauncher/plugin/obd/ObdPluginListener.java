package com.wow.carlauncher.plugin.obd;

/**
 * Created by 10124 on 2018/3/29.
 */

public interface ObdPluginListener {
    void connect(boolean success);

    //汽车运行信息回掉
    void carRunningInfo(Integer speed,
                        Integer rev,
                        Integer waterTemp,
                        Float residualOil,
                        Float oilConsumption);

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
