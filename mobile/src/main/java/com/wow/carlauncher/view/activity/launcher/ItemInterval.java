package com.wow.carlauncher.view.activity.launcher;

import com.wow.carlauncher.view.activity.set.setItem.SetEnum;

public enum ItemInterval implements SetEnum {
    XIAO("小", 1),
    ZHONG("中", 2),
    DA("大", 3);

    private String name;
    private Integer id;


    ItemInterval(String name, Integer id) {
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

    public static ItemInterval getById(Integer id) {
        switch (id) {
            case 1:
                return XIAO;
            case 2:
                return ZHONG;
            case 3:
                return DA;
        }
        return XIAO;
    }

    public static int getSizeById(Integer id) {
        switch (id) {
            case 1:
                return 15;
            case 2:
                return 20;
            case 3:
                return 25;
        }
        return 15;
    }
}
