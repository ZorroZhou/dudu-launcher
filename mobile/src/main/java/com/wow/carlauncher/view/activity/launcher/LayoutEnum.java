package com.wow.carlauncher.view.activity.launcher;

import com.wow.carlauncher.view.activity.set.SetEnum;

public enum LayoutEnum implements SetEnum {
    LAYOUT1("布局1", 1),
    LAYOUT2("布局2", 2);
    private String name;
    private Integer id;


    LayoutEnum(String name, Integer id) {
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

    public static LayoutEnum getById(Integer id) {
        switch (id) {
            case 1:
                return LAYOUT1;
            case 2:
                return LAYOUT2;
        }
        throw new RuntimeException();
    }
}
