package com.wow.carlauncher.ex.plugin.fk;

/**
 * Created by 10124 on 2018/3/29.
 */

public enum FangkongProtocolEnum {
    YLFK("亿连方控", 1);

    private String name;
    private Integer id;

    FangkongProtocolEnum(String name, Integer id) {
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

    public static FangkongProtocolEnum getById(Integer id) {
        switch (id) {
            case 1:
                return YLFK;
        }
        return YLFK;
    }
}
