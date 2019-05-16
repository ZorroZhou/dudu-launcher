package com.wow.carlauncher.view.activity.set.listener;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.ThreadObj;
import com.wow.carlauncher.view.activity.set.SetEnum;

import java.util.ArrayList;
import java.util.List;

public abstract class SetEnumOnClickListener<T extends SetEnum> implements View.OnClickListener {
    private Context context;
    private List<T> allItem;

    public SetEnumOnClickListener(Context context, T[] allItem) {
        this.context = context;
        this.allItem = java.util.Arrays.asList(allItem);
    }

    public SetEnumOnClickListener(Context context, List<T> allItem) {
        this.context = context;
        if (allItem == null) {
            this.allItem = new ArrayList<>();
        } else {
            this.allItem = allItem;
        }
    }

    public abstract T getCurr();

    public abstract void onSelect(T t);

    public abstract String title();

    public boolean equals(T t1, T t2) {
        return CommonUtil.equals(t1, t2);
    }

    @Override
    public void onClick(View v) {
        String[] items = new String[allItem.size()];
        int select = 0;
        final T curr = getCurr();
        for (int i = 0; i < allItem.size(); i++) {
            T item = allItem.get(i);
            items[i] = item.getName();
            if (equals(item, curr)) {
                select = i;
            }
        }
        final ThreadObj<Integer> obj = new ThreadObj<>(select);

        new AlertDialog.Builder(context).setTitle(title()).setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    T sel = allItem.get(obj.getObj());
                    if (!equals(sel, curr)) {
                        onSelect(sel);
                    }
                }).setSingleChoiceItems(items, select, (dialog1, which) -> obj.setObj(which)).show();
    }
}
