package com.wow.carlauncher.view.base;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyixian on 15/11/5.
 */
public abstract class BaseAdapterEx<T> extends BaseAdapter {
    protected List<T> items;
    protected Context context;

    public BaseAdapterEx(Context context) {
        items = new ArrayList<T>();
        this.context = context;
    }

    public void addItems(List<T> items) {
        if (items != null) {
            this.items.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void addItem(T item) {
        if (item != null) {
            this.items.add(item);
        }
    }

    public void remove(int p) {
        if (p < items.size()) {
            items.remove(p);
        }
    }

    public void remove(T p) {
        items.remove(p);
    }

    public List<T> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
