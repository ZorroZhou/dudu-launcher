package com.wow.carlauncher.view.activity.set.listener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.repertory.db.entiy.SkinInfo;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;
import com.wow.carlauncher.view.activity.set.setItem.SetAppInfo;
import com.wow.carlauncher.view.activity.set.setItem.SetEnum;
import com.wow.carlauncher.view.base.BaseAdapterEx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class SetMultipleSelectView<T extends SetEnum> extends SetBaseView implements View.OnClickListener {

    private String title;
    private SelectAdapter<T> selectAdapter;

    public SetMultipleSelectView(SetActivity context, String title) {
        super(context);
        this.title = title;
    }

    @Override
    protected void initView() {
        ListView list = findViewById(R.id.list);
        this.selectAdapter = new SelectAdapter<>(getActivity());
        list.setAdapter(selectAdapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            if (position < selectAdapter.getCount()) {
                selectAdapter.getItem(position).checked = !selectAdapter.getItem(position).checked;
                selectAdapter.notifyDataSetChanged();
            }
        });
    }

    public abstract Collection<T> getAll();

    public abstract Collection<T> getCurr();

    public abstract boolean onSelect(Collection<T> t);

    public boolean equals(T t1, T t2) {
        if (t1 == null || t2 == null) {
            return false;
        }
        if (t1 instanceof SkinInfo && t2 instanceof SkinInfo) {
            return CommonUtil.equals(((SkinInfo) t1).getMark(), ((SkinInfo) t2).getMark());
        }
        if (t1 instanceof SetAppInfo && t2 instanceof SetAppInfo) {
            String clazz1 = ((SetAppInfo) t1).getAppInfo().clazz.replace("1:", "").replace("2:", "");
            String clazz2 = ((SetAppInfo) t2).getAppInfo().clazz.replace("1:", "").replace("2:", "");
            return CommonUtil.equals(clazz1, clazz2);
        }
        return CommonUtil.equals(t1, t2);
    }

    @Override
    public void onClick(View v) {
        selectAdapter.clear();

        Collection<T> curr = getCurr();
        if (curr == null) {
            curr = new ArrayList<>();
        }
        Collection<T> list = getAll();
        for (T item : list) {
            SetMultipleSelectView.Item<T> ii = new SetMultipleSelectView.Item<>();
            ii.data = item;
            ii.checked = false;
            for (T select : curr) {
                if (equals(ii.data, select)) {
                    ii.checked = true;
                    break;
                }
            }
            selectAdapter.addItem(ii);
        }
        getActivity().addSetView(this);
    }

    private static class Item<T> {
        private T data;
        private boolean checked;
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
        List<T> select = new ArrayList<>();
        for (int i = 0; i < selectAdapter.getCount(); i++) {
            Item<T> item = selectAdapter.getItem(i);
            if (item.checked) {
                select.add(item.data);
            }
        }
        return onSelect(select);
    }


    public static class SelectAdapter<T extends SetEnum> extends BaseAdapterEx<Item<T>> {
        private LayoutInflater inflate;

        public SelectAdapter(Context context) {
            super(context);
            inflate = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.item_set_single_select, viewGroup, false);
            }
            Item<T> model = items.get(position);
            if (!model.equals(convertView.getTag())) {
                ((TextView) convertView.findViewById(R.id.name)).setText(model.data.getName());
                convertView.setTag(model);
            }
            convertView.findViewById(R.id.iv_select).setVisibility(model.checked ? VISIBLE : GONE);
            return convertView;
        }
    }

}
