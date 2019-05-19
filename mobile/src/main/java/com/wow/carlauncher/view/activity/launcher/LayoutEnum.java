package com.wow.carlauncher.view.activity.launcher;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.activity.set.setItem.SetEnum;
import com.wow.carlauncher.view.adapter.PicSelectAdapter;

public enum LayoutEnum implements PicSelectAdapter.PicModel, SetEnum {
    AUTO("使用主题布局", 0),
    LAYOUT1("布局1(dock在左侧,托盘在底部)", 1),
    LAYOUT2("布局2(dock在底部,托盘在顶部)", 2);
    private String name;
    private Integer id;


    LayoutEnum(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPicRes() {
        if (this.equals(LAYOUT2)) {
            return R.mipmap.img_app2;
        } else {
            return R.mipmap.img_app1;
        }
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
            case 0:
                return AUTO;
            case 1:
                return LAYOUT1;
            case 2:
                return LAYOUT2;
        }
        throw new RuntimeException();
    }
}
