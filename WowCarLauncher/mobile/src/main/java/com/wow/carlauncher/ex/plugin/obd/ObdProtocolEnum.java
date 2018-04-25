package com.wow.carlauncher.ex.plugin.obd;

/**
 * Created by 10124 on 2018/3/29.
 */

public enum ObdProtocolEnum {
    YJ_TYB("优驾胎压版", 1);

    private String name;
    private Integer id;

    ObdProtocolEnum(String name, Integer id) {
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

    public static ObdProtocolEnum getById(Integer id) {
        switch (id) {
            case 1:
                return YJ_TYB;
        }
        return YJ_TYB;
    }
}
