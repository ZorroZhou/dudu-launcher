package com.wow.carlauncher.view.activity.driving;

import android.content.Context;

import com.wow.carlauncher.view.activity.driving.blue.BlueView;
import com.wow.carlauncher.view.activity.driving.coolBlack.CoolBlackView;
import com.wow.carlauncher.view.activity.set.SetEnum;

/**
 * Created by 10124 on 2018/3/29.
 */

public enum DrivingViewEnum implements SetEnum {
    BLACK("酷黑仪表盘1", 1),
    BLUE("魅力蓝调", 2);

    private String name;
    private Integer id;

    DrivingViewEnum(String name, Integer id) {
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

    public static DrivingViewEnum getById(Integer id) {
        switch (id) {
            case 1:
                return BLACK;
            case 2:
                return BLUE;
        }
        return BLACK;
    }

    public static DrivingView createView(Context context, DrivingViewEnum itemEnum) {
        switch (itemEnum) {
            case BLACK:
                return new CoolBlackView(context);
            case BLUE:
                return new BlueView(context);
        }
        return new CoolBlackView(context);
    }
}
