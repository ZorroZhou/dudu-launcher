package com.wow.carlauncher.view.activity.driving;

import com.wow.carlauncher.view.activity.set.SetEnum;

/**
 * Created by 10124 on 2018/3/29.
 */

public enum AutoDrivingEnum implements SetEnum {
    TIME("时间", 1), REV("转速(OBD支持)", 2);

    private String name;
    private Integer id;

    AutoDrivingEnum(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static AutoDrivingEnum getById(Integer id) {
        switch (id) {
            case 1:
                return TIME;
            case 2:
                return REV;
        }
        return TIME;
    }
}
