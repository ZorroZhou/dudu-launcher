package com.wow.carlauncher.view.activity.set;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.wow.carlauncher.common.util.ThreadObj;

public abstract class SetEnumOnClickListener<T extends SetEnum> implements View.OnClickListener {
    private Context context;
    private T[] allItem;

    public SetEnumOnClickListener(Context context, T[] allItem) {
        this.context = context;
        this.allItem = allItem;
    }

    public abstract T getCurr();

    public abstract void onSelect(T t);

    @Override
    public void onClick(View v) {
        String[] items = new String[allItem.length];
        int select = 0;
        for (int i = 0; i < allItem.length; i++) {
            items[i] = allItem[i].getName();
            if (allItem[i].equals(getCurr())) {
                select = i;
            }
        }
        final ThreadObj<Integer> obj = new ThreadObj<>(select);

        AlertDialog dialog = new AlertDialog.Builder(context).setTitle("请选择插件").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onSelect(allItem[obj.getObj()]);
            }
        }).setSingleChoiceItems(items, select, (dialog1, which) -> obj.setObj(which)).show();
    }
}
