package com.wow.carlauncher.view.activity.set;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseAdapterEx;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.view.activity.launcher.ItemEnum;
import com.wow.carlauncher.view.activity.launcher.ItemModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LauncherItemAdapter extends BaseAdapterEx<ItemModel> implements View.OnClickListener {

    public LauncherItemAdapter(Context context) {
        super(context);

        List<ItemModel> item = new ArrayList<>();
        for (ItemEnum itemEnum : CommonData.LAUNCHER_ITEMS) {
            item.add(new ItemModel(itemEnum,
                    SharedPreUtil.getSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_SORT_ + itemEnum.getId(), itemEnum.getId()),
                    SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_LAUNCHER_ITEM_OPEN_ + itemEnum.getId(), true)
            ));
        }

        Collections.sort(item, (o1, o2) -> o1.index - o2.index);
        this.items.addAll(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_list_launcher_item, null);
            convertView.findViewById(R.id.btn_up).setOnClickListener(this);
            convertView.findViewById(R.id.btn_down).setOnClickListener(this);
            ((CheckBox) convertView.findViewById(R.id.cb_use)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ItemModel obj = (ItemModel) buttonView.getTag();
                    obj.check = isChecked;
                }
            });
        }
        Object obj = convertView.getTag();
        ItemModel item = getItem(position);
        if (!item.equals(obj)) {
            convertView.setTag(item);
            convertView.findViewById(R.id.btn_up).setTag(item);
            convertView.findViewById(R.id.btn_down).setTag(item);
            convertView.findViewById(R.id.cb_use).setTag(item);

            ((TextView) convertView.findViewById(R.id.tv_name)).setText(item.info.getName());
            ((CheckBox) convertView.findViewById(R.id.cb_use)).setChecked(item.check);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_up: {
                ItemModel obj = (ItemModel) v.getTag();
                int index = items.indexOf(obj);
                if (index > 0) {
                    ItemModel top = items.get(index - 1);
                    top.index++;
                    obj.index--;
                    Collections.sort(items, (o1, o2) -> o1.index - o2.index);
                    notifyDataSetInvalidated();
                }
                break;
            }
            case R.id.btn_down: {
                ItemModel obj = (ItemModel) v.getTag();
                int index = items.indexOf(obj);
                if (index < items.size() - 1) {
                    ItemModel bottom = items.get(index + 1);
                    bottom.index--;
                    obj.index++;
                    Collections.sort(items, (o1, o2) -> o1.index - o2.index);
                    notifyDataSetInvalidated();
                }
                break;
            }
        }
    }
}
