package com.wow.carlauncher.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.view.base.BaseAdapterEx;

/**
 * Created by 10124 on 2017/10/28.
 */

public class SelectAppAdapter extends BaseAdapterEx<AppInfo> {

    public SelectAppAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.item_list_all_app, viewGroup, false);
        }
        AppInfo model = items.get(position);
        if (!model.equals(convertView.getTag())) {
            ((TextView) convertView.findViewById(R.id.name)).setText(model.name);
            ((ImageView) convertView.findViewById(R.id.icon)).setImageDrawable(AppInfoManage.self().getAppIcon(model.clazz, false));
            convertView.setTag(model);
        }
        return convertView;
    }
}
