package com.wow.carlauncher.view.activity.launcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.view.base.BaseAdapterEx;

/**
 * Created by 10124 on 2017/10/28.
 */

public class AllAppAdapter extends BaseAdapterEx<AppInfo> {
    private LayoutInflater inflate;
    private int height = 100;

    public void setHeight(int height) {
        this.height = height;
        this.notifyDataSetChanged();
    }

    public AllAppAdapter(Context context) {
        super(context);
        inflate = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.grid_item_launcher_all_app, viewGroup, false);
        }
        boolean needUpdateIcon = false;
        if (!ThemeManage.self().getTheme().equals(convertView.getTag(R.string.tag_key_launcher_all_all_item_theme))) {
            convertView.setTag(R.string.tag_key_launcher_all_all_item_theme, ThemeManage.self().getTheme());
            convertView.findViewById(R.id.ll_base).setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_cell_bg));
            convertView.findViewById(R.id.line1).setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_line3));
            ((TextView) convertView.findViewById(R.id.name)).setTextColor(ThemeManage.self().getCurrentThemeColor(R.color.l_msg));
            needUpdateIcon = true;
        }
        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
        if (layoutParams.height != height) {
            layoutParams.height = height;
            convertView.setLayoutParams(layoutParams);
        }

        AppInfo model = items.get(position);
        if (!model.equals(convertView.getTag())) {
            ((TextView) convertView.findViewById(R.id.name)).setText(model.name);
            convertView.setTag(model);
            needUpdateIcon = true;
        }
        if (needUpdateIcon) {
            ((ImageView) convertView.findViewById(R.id.icon)).setImageDrawable(AppInfoManage.self().getIcon(model.clazz));
        }
        return convertView;
    }
}
