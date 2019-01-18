package com.wow.carlauncher.view.activity.driving;

/**
 * Created by 10124 on 2018/3/29.
 */

public enum DrivingViewEnum {
    COOL_BLACK("酷黑仪表盘1", 1);

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
                return COOL_BLACK;
        }
        return COOL_BLACK;
    }
}
