package com.wow.carlauncher.view.activity.radios;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseAdapterEx;
import com.ximalaya.ting.android.opensdk.model.live.provinces.Province;

public class ProvinceAdapter extends BaseAdapterEx<Province> {

    public ProvinceAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.item_list_string, null, false);
        }
        Province model = items.get(position);
        if (!model.equals(convertView.getTag())) {
            ((TextView) convertView.findViewById(R.id.string)).setText(model.getProvinceName());
            convertView.setTag(model);
        }
        return convertView;
    }
}
