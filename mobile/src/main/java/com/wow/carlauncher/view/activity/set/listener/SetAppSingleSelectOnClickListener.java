package com.wow.carlauncher.view.activity.set.listener;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.ThreadObj;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;

import java.util.ArrayList;
import java.util.List;

public abstract class SetAppSingleSelectOnClickListener implements View.OnClickListener {
    private Context context;


    public SetAppSingleSelectOnClickListener(Context context) {
        this.context = context;
    }

    public abstract String getCurr();

    public abstract void onSelect(String t);

    @Override
    public void onClick(View v) {
        String selectapp = getCurr();
        final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getOtherAppInfos());
        String[] items = new String[appInfos.size()];
        int select = -1;
        for (int i = 0; i < items.length; i++) {
            items[i] = appInfos.get(i).name + "(" + appInfos.get(i).clazz + ")";
            if (appInfos.get(i).clazz.equals(selectapp)) {
                select = i;
            }
        }
        LogEx.d(this, "onClick: " + items.length + " " + select);
        final ThreadObj<Integer> obj = new ThreadObj<>(select);
        new AlertDialog.Builder(context).setTitle("请选择APP")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog12, which) -> onSelect(appInfos.get(obj.getObj()).clazz))
                .setSingleChoiceItems(items, select, (dialog1, which) -> obj.setObj(which)).show();
    }
}
