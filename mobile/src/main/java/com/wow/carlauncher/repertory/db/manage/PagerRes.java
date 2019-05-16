package com.wow.carlauncher.repertory.db.manage;

import java.util.List;

/**
 * Created by liuyixian on 15/11/26.
 */
public class PagerRes<T> {
    private int total;
    private List<T> items;

    public int getTotal() {
        return total;
    }

    public PagerRes<T> setTotal(int total) {
        this.total = total;
        return this;
    }

    public List<T> getItems() {
        return items;
    }

    public PagerRes<T> setItems(List<T> items) {
        this.items = items;
        return this;
    }
}
