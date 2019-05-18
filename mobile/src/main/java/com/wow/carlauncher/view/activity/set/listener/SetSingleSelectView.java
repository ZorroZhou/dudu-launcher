package com.wow.carlauncher.view.activity.set.listener;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.view.activity.set.SetEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class SetSingleSelectView<T extends SetEnum> extends LinearLayout implements View.OnClickListener {
    private Context context;
    private List<Item<T>> allItem;

    private ViewGroup parent;
    private LinearLayout content;
    private TextView textView;
    private T curr;

    public SetSingleSelectView(Context context, ViewGroup parent) {
        super(context);
        this.setOrientation(VERTICAL);

        this.context = context;
        this.parent = parent;
        this.allItem = new ArrayList<>();

        View v = View.inflate(getContext(), R.layout.content_set_select, null);
        this.addView(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        this.textView = v.findViewById(R.id.title);
        this.content = v.findViewById(R.id.content);
        v.findViewById(R.id.ll_top).setOnClickListener(v1 -> {
            parent.setVisibility(VISIBLE);
            ViewGroup viewGroup = (ViewGroup) SetSingleSelectView.this.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(SetSingleSelectView.this);
            }
            if (!CommonUtil.equals(curr, getCurr())) {
                onSelect(curr);
            }
        });

    }


    public abstract Collection<T> getAll();

    public abstract T getCurr();

    public abstract void onSelect(T t);

    public abstract String title();

    public boolean equals(T t1, T t2) {
        return CommonUtil.equals(t1, t2);
    }

    @Override
    public void onClick(View v) {
        if (parent == null) {
            return;
        }
        allItem.clear();
        Collection<T> list = getAll();
        for (T item : list) {
            Item<T> ii = new Item<>();
            ii.data = item;
            allItem.add(ii);
        }
        this.content.removeAllViews();

        this.textView.setText(title());

        curr = getCurr();
        OnClickListener clickListener = v1 -> {
            Item<T> sel = allItem.get((int) v1.getTag());
            if (!CommonUtil.equals(sel.data, curr)) {
                curr = sel.data;
                for (Item<T> item : allItem) {
                    item.view.findViewById(R.id.iv_select).setVisibility(INVISIBLE);
                }
                sel.view.findViewById(R.id.iv_select).setVisibility(VISIBLE);
            }
        };
        for (int i = 0; i < allItem.size(); i++) {
            Item<T> item = allItem.get(i);
            item.view = View.inflate(this.context, R.layout.item_set_single_select, null);
            item.view.setTag(i);
            ((TextView) item.view.findViewById(R.id.name)).setText(item.data.getName());
            if (equals(item.data, curr)) {
                item.view.findViewById(R.id.iv_select).setVisibility(VISIBLE);
            }
            item.view.setOnClickListener(clickListener);
            this.content.addView(item.view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        ViewGroup baseView = (ViewGroup) parent.getParent();
        parent.setVisibility(GONE);
        baseView.addView(this, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private static class Item<T> {
        private T data;
        private View view;
    }
}
