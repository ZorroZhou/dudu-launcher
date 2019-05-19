package com.wow.carlauncher.view.activity.set.listener;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;
import com.wow.carlauncher.view.activity.set.SetEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class SetSingleSelectView<T extends SetEnum> extends SetBaseView implements View.OnClickListener {
    private List<Item<T>> allItem;

    private LinearLayout content;
    private T curr;
    private String title;

    public SetSingleSelectView(SetActivity context, String title) {
        super(context);
        this.title = title;
        this.allItem = new ArrayList<>();
    }

    @Override
    protected void initView() {
        this.content = findViewById(R.id.content);
    }

    public abstract Collection<T> getAll();

    public abstract T getCurr();

    public abstract void onSelect(T t);

    public boolean equals(T t1, T t2) {
        return CommonUtil.equals(t1, t2);
    }

    @Override
    public void onClick(View v) {
        allItem.clear();
        Collection<T> list = getAll();
        for (T item : list) {
            Item<T> ii = new Item<>();
            ii.data = item;
            allItem.add(ii);
        }
        this.content.removeAllViews();

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
            item.view = View.inflate(getActivity(), R.layout.item_set_single_select, null);
            item.view.setTag(i);
            ((TextView) item.view.findViewById(R.id.name)).setText(item.data.getName());
            if (equals(item.data, curr)) {
                item.view.findViewById(R.id.iv_select).setVisibility(VISIBLE);
            }
            item.view.setOnClickListener(clickListener);
            this.content.addView(item.view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        getActivity().addSetView(this);
    }

    private static class Item<T> {
        private T data;
        private View view;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_select;
    }

    @Override
    public boolean showRight() {
        return true;
    }

    @Override
    public String rightTitle() {
        return "保存";
    }

    @Override
    public boolean rightAction() {
        if (!CommonUtil.equals(curr, getCurr())) {
            onSelect(curr);
        }
        return true;
    }
}
