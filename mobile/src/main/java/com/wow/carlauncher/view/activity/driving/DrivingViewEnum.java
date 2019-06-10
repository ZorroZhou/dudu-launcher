package com.wow.carlauncher.view.activity.driving;

import android.content.Context;

import com.wow.carlauncher.view.activity.driving.blue.DrivingBlueView;
import com.wow.carlauncher.view.activity.driving.coolBlack.DrivingCoolBlackView;
import com.wow.carlauncher.view.activity.driving.time.DrivingTimeView;
import com.wow.carlauncher.view.activity.set.setItem.SetEnum;
import com.wow.carlauncher.view.base.BaseView;

/**
 * Created by 10124 on 2018/3/29.
 */

public enum DrivingViewEnum implements SetEnum {
    BLACK("酷黑仪表盘(需要OBD支持,音乐,胎压,导航,车况)", 1),
    BLUE("魅力蓝调(OBD支持更佳,音乐,胎压,导航,车况)", 2),
    TIME("时间待机屏幕(只有时间显示的待机屏幕)", 3);

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
            case 3:
                return TIME;
        }
        return BLACK;
    }

    public static DrivingBaseView createView(Context context, DrivingViewEnum itemEnum) {
        switch (itemEnum) {
            case BLACK:
                return new DrivingCoolBlackView(context);
            case BLUE:
                return new DrivingBlueView(context);
            case TIME:
                return new DrivingTimeView(context);
        }
        return new DrivingCoolBlackView(context);
    }
}
