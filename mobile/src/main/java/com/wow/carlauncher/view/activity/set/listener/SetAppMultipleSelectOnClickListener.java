package com.wow.carlauncher.view.activity.set.listener;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;

import java.util.ArrayList;
import java.util.List;

public abstract class SetAppMultipleSelectOnClickListener implements View.OnClickListener {
    private Context context;

    public SetAppMultipleSelectOnClickListener(Context context) {
        this.context = context;
    }

    public abstract String getCurr();

    public abstract void onSelect(String t);

    @Override
    public void onClick(View v) {
        String selectapp = getCurr();
        final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getOtherAppInfos());
        String[] items = new String[appInfos.size()];
        final boolean[] checks = new boolean[appInfos.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = appInfos.get(i).name + "(" + appInfos.get(i).clazz + ")";
            checks[i] = selectapp.contains("[" + appInfos.get(i).clazz + "]");
        }

        new AlertDialog.Builder(context).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", (dialog12, which) -> {
            String selectapp1 = "";
            for (int i = 0; i < appInfos.size(); i++) {
                if (checks[i]) {
                    selectapp1 = selectapp1 + "[" + appInfos.get(i).clazz + "];";
                }
            }
            if (selectapp1.endsWith(";")) {
                selectapp1 = selectapp1.substring(0, selectapp1.length() - 1);
            }
            onSelect(selectapp1);
        }).setMultiChoiceItems(items, checks, (dialog1, which, isChecked) -> checks[which] = isChecked).show();
    }
}
