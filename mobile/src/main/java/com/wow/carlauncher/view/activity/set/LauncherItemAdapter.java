package com.wow.carlauncher.view.activity.set;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseAdapterEx;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.view.activity.launcher.ItemModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.wow.carlauncher.view.activity.launcher.ItemEnum.*;

public class LauncherItemAdapter extends BaseAdapterEx<ItemModel> implements View.OnClickListener {
    public LauncherItemAdapter(Context context) {
        super(context);

        List<ItemModel> item = new ArrayList<>();
        item.add(new ItemModel(AMAP,
                SharedPreUtil.getSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_SORT_ + AMAP.getId(), AMAP.getId()),
                SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_LAUNCHER_ITEM_OPEN_ + AMAP.getId(), true)
        ));
        item.add(new ItemModel(MUSIC,
                SharedPreUtil.getSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_SORT_ + MUSIC.getId(), MUSIC.getId()),
                SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_LAUNCHER_ITEM_OPEN_ + MUSIC.getId(), true)
        ));
        item.add(new ItemModel(WEATHER,
                SharedPreUtil.getSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_SORT_ + WEATHER.getId(), WEATHER.getId()),
                SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_LAUNCHER_ITEM_OPEN_ + WEATHER.getId(), true)
        ));
        item.add(new ItemModel(TAIYA,
                SharedPreUtil.getSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_SORT_ + TAIYA.getId(), TAIYA.getId()),
                SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_LAUNCHER_ITEM_OPEN_ + TAIYA.getId(), true)
        ));
        item.add(new ItemModel(OBD,
                SharedPreUtil.getSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_SORT_ + OBD.getId(), OBD.getId()),
                SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_LAUNCHER_ITEM_OPEN_ + OBD.getId(), true)
        ));
        item.add(new ItemModel(TIME,
                SharedPreUtil.getSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_SORT_ + TIME.getId(), TIME.getId()),
                SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_LAUNCHER_ITEM_OPEN_ + TIME.getId(), true)
        ));

        Collections.sort(item, (o1, o2) -> o1.index - o2.index);
        this.items.addAll(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_list_launcher_item, null);
            convertView.findViewById(R.id.btn_up).setOnClickListener(this);
            convertView.findViewById(R.id.btn_down).setOnClickListener(this);
        }
        Object obj = convertView.getTag();
        ItemModel item = getItem(position);
        if (!item.equals(obj)) {
            convertView.setTag(item);
            convertView.findViewById(R.id.btn_up).setTag(item);
            convertView.findViewById(R.id.btn_down).setTag(item);
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
                System.out.println(obj);
                int index = items.indexOf(obj);
                if (index > 0) {
                    ItemModel top = items.get(index - 1);
                    top.index++;
                    obj.index--;
                    Collections.sort(items, (o1, o2) -> o1.index - o2.index);
                    System.out.println(items);
                    notifyDataSetInvalidated();
                }
                break;
            }
            case R.id.btn_down: {
                ItemModel obj = (ItemModel) v.getTag();
                System.out.println(obj);
                int index = items.indexOf(obj);
                if (index < items.size() - 1) {
                    ItemModel bottom = items.get(index + 1);
                    bottom.index--;
                    obj.index++;
                    Collections.sort(items, (o1, o2) -> o1.index - o2.index);
                    System.out.println(items);
                    notifyDataSetInvalidated();
                }
                break;
            }
        }
    }
}
