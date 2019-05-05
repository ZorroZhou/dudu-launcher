package com.wow.carlauncher.view.clickListener;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.view.adapter.PicSelectAdapter;

public abstract class PicSelectOnClickListener<T extends PicSelectAdapter.PicModel> implements View.OnClickListener {
    private Context context;
    private T[] allItem;

    public PicSelectOnClickListener(Context context, T[] allItem) {
        this.context = context;
        this.allItem = allItem;
    }

    public abstract T getCurr();

    public abstract void onSelect(T t);

    public abstract String title();

    @Override
    public void onClick(View v) {
        PicSelectAdapter<T> picSelectAdapter = new PicSelectAdapter<>(context);
        int select = -1;
        for (int i = 0; i < allItem.length; i++) {
            picSelectAdapter.addItem(allItem[i]);
            if (allItem[i].equals(getCurr())) {
                select = i;
            }
        }
        picSelectAdapter.setSelect(select);
        GridView gridView = new GridView(context);
        gridView.setAdapter(picSelectAdapter);
        gridView.setNumColumns(2);
        gridView.setPadding(ViewUtils.dip2px(context, 5), ViewUtils.dip2px(context, 5), ViewUtils.dip2px(context, 5), ViewUtils.dip2px(context, 5));
        gridView.setOnItemClickListener((parent, view, position, id) -> picSelectAdapter.setSelect(position));
        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle(title()).setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    if (!allItem[picSelectAdapter.getSelect()].equals(getCurr())) {
                        onSelect(allItem[picSelectAdapter.getSelect()]);
                    }
                }).setView(gridView).show();
        Window window = alertDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = ViewUtils.dip2px(context, 520);//定义宽度
            alertDialog.getWindow().setAttributes(lp);
        }
    }
}
