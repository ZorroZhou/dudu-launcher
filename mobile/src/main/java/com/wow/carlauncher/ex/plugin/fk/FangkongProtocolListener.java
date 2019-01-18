package com.wow.carlauncher.ex.plugin.fk;

/**
 * Created by 10124 on 2018/4/22.
 */

public interface FangkongProtocolListener {
    void changeModel(String name);

    void batteryLevel(Integer level, Integer total);
}
