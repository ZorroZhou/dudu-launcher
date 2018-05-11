package com.wow.carlauncher.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.frame.util.AppUtil;
import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseAdapterEx;

/**
 * Created by 10124 on 2017/10/28.
 */

public class AllAppAdapter extends BaseAdapterEx<AppUtil.AppInfo> {
    private LayoutInflater inflate;

    public AllAppAdapter(Context context) {
        super(context);
        inflate = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.item_grid_all_app, viewGroup, false);
        }
        AppUtil.AppInfo model = items.get(position);
        if (!model.equals(convertView.getTag())) {
            ((TextView) convertView.findViewById(R.id.name)).setText(model.name);
            ((ImageView) convertView.findViewById(R.id.icon)).setImageDrawable(model.icon);
            convertView.setTag(model);
        }
        return convertView;
    }
}
