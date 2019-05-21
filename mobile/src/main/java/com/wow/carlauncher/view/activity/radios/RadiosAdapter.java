package com.wow.carlauncher.view.activity.radios;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.view.base.BaseAdapterEx;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;

public class RadiosAdapter extends BaseAdapterEx<Radio> {

    public RadiosAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.item_list_radios, null, false);
        }
        Radio model = items.get(position);
        if (!model.equals(convertView.getTag())) {
            ImageManage.self().loadImage(model.getCoverUrlSmall(), convertView.findViewById(R.id.iv_cover));
            ((TextView) convertView.findViewById(R.id.tv_name)).setText(model.getRadioName());
            ((TextView) convertView.findViewById(R.id.tv_name2)).setText(model.getRadioDesc());
            convertView.setTag(model);
        }
        return convertView;
    }
}
