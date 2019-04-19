package com.wow.carlauncher.view.activity.launcher;

public class ItemModel {
    public ItemEnum info;
    public int index;
    public boolean check = false;

    public ItemModel(ItemEnum info, int index, boolean check) {
        this.info = info;
        this.index = index;
        this.check = check;
    }

    @Override
    public String toString() {
        return "Item{" +
                "info=" + info +
                ", index=" + index +
                '}';
    }
}
