package com.wow.carlauncher.view.activity.set.setItem;

public class SetHomeNum implements SetEnum {
    private int num;

    public int getNum() {
        return num;
    }

    public SetHomeNum(int num) {
        this.num = num;
    }

    @Override
    public String getName() {
        return num + "个";
    }
}
