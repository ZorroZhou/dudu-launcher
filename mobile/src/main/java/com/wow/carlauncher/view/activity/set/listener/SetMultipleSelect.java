package com.wow.carlauncher.view.activity.set.listener;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.view.activity.set.SetEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class SetMultipleSelect<T extends SetEnum> implements View.OnClickListener {
    private Context context;
    private List<T> allItem;

    public SetMultipleSelect(Context context) {
        this.context = context;
        allItem = new ArrayList<>();
    }


    public abstract Collection<T> getAll();

    public abstract T[] getCurr();

    public abstract void onSelect(List<T> temp);

    public abstract String title();

    public boolean equals(T t1, T t2) {
        return CommonUtil.equals(t1, t2);
    }

    @Override
    public void onClick(View v) {
        allItem.clear();
        allItem.addAll(getAll());

        String[] items = new String[allItem.size()];
        boolean[] check = new boolean[allItem.size()];
        final T[] curr = getCurr();
        for (int i = 0; i < allItem.size(); i++) {
            T item = allItem.get(i);
            items[i] = item.getName();
            check[i] = isSelect(curr, item);
        }
        new AlertDialog.Builder(context).setTitle(title()).setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    List<T> temp = new ArrayList<>();
                    for (int i = 0; i < check.length; i++) {
                        if (check[i]) {
                            temp.add(allItem.get(i));
                        }
                    }
                    onSelect(temp);
                }).setMultiChoiceItems(items, check, (dialog, which, isChecked) -> check[which] = isChecked).show();
    }

    private boolean isSelect(T[] select, T now) {
        for (T aSelect : select) {
            if (equals(now, aSelect)) {
                return true;
            }
        }
        return false;
    }
}
