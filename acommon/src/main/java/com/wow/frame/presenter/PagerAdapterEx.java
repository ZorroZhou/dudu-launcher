package com.wow.frame.presenter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyixian on 16/1/4.
 */
public abstract class PagerAdapterEx<T> extends PagerAdapter {
    protected List<T> items;
    protected Context context;

    public PagerAdapterEx(Context context) {
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
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        //这里删除view
        for (int i = 0; i < view.getChildCount(); i++) {
            View c = view.getChildAt(i);
            if (("position" + position).equals(c.getTag())) {
                view.removeView(c);
            }
        }

    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View v = getView(view, position);
        v.setTag("position" + position);
        view.addView(v, 0);
        return v;
    }

    protected abstract View getView(ViewGroup view, int position);
}
