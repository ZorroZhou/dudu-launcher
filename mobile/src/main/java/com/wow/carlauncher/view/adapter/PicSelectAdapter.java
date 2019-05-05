package com.wow.carlauncher.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
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

public class PicSelectAdapter<T extends PicSelectAdapter.PicModel> extends BaseAdapterEx<T> {
    private LayoutInflater inflate;

    public PicSelectAdapter(Context context) {
        super(context);
        inflate = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.item_pic_select, viewGroup, false);
        }
        PicModel model = items.get(position);
        if (!model.equals(convertView.getTag())) {
            ((TextView) convertView.findViewById(R.id.name)).setText(model.getName());
            ((ImageView) convertView.findViewById(R.id.icon)).setImageResource(model.getPicRes());
            convertView.setTag(model);
        }
        View selectView = convertView.findViewById(R.id.select);
        selectView.setVisibility(View.GONE);
        if (select == position) {
            selectView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private int select = -1;

    public void setSelect(int select) {
        this.select = select;
        notifyDataSetChanged();
    }

    public int getSelect() {
        return select;
    }

    public interface PicModel {
        String getName();

        int getPicRes();
    }
}
