package com.wow.carlauncher.ex.manage.skin;

import com.wow.carlauncher.view.activity.set.SetEnum;

public enum SkinModel implements SetEnum {
    SHIJIAN("根据日出日落切换", 0),
    DENGGUANG("根据灯光切换(部分车型支持)", 1),
    BAISE("固定主题", 2);

    private String name;
    private Integer id;

    SkinModel(String name, Integer id) {
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

    public static SkinModel getById(Integer id) {
        switch (id) {
            case 0:
                return SHIJIAN;
            case 1:
                return DENGGUANG;
            case 2:
                return BAISE;
        }
        return SHIJIAN;
    }
}
