package com.wow.carlauncher.view.activity.set.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.view.base.BaseView;
import com.wow.frame.util.SharedPreUtil;
import com.wow.frame.util.ThreadObj;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SLoadAppView extends BaseView {
    public SLoadAppView(@NonNull Context context) {
        super(context);
    }

    public SLoadAppView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_load_app;
    }

    @ViewInject(R.id.sv_load_use)
    private SetView sv_load_use;

    @ViewInject(R.id.sv_open1)
    private SetView sv_open1;
    @ViewInject(R.id.sv_open2)
    private SetView sv_open2;
    @ViewInject(R.id.sv_open3)
    private SetView sv_open3;
    @ViewInject(R.id.sv_open4)
    private SetView sv_open4;

    @ViewInject(R.id.sv_back_yanchi)
    private SetView sv_back_yanchi;

    protected void initView(Bundle savedInstanceState) {


        sv_open1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectApp(CommonData.SDATA_APP_AUTO_OPEN1);
            }
        });
        sv_open2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectApp(CommonData.SDATA_APP_AUTO_OPEN2);
            }
        });
        sv_open3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectApp(CommonData.SDATA_APP_AUTO_OPEN3);
            }
        });
        sv_open4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectApp(CommonData.SDATA_APP_AUTO_OPEN4);
            }
        });
        sv_load_use.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_APP_AUTO_OPEN_USE, true);
                } else {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_APP_AUTO_OPEN_USE, false);
                }
            }
        });
        sv_load_use.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_APP_AUTO_OPEN_USE, false));

        sv_back_yanchi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] items = {
                        "1秒", "2秒", "3秒", "4秒", "5秒", "6秒", "7秒", "8秒", "9秒", "10秒"
                };
                int select = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_APP_AUTO_OPEN_BACK, 5) - 1;
                final ThreadObj<Integer> obj = new ThreadObj<>(select);
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_APP_AUTO_OPEN_BACK, obj.getObj() + 1);
                        sv_back_yanchi.setSummary(SharedPreUtil.getSharedPreInteger(CommonData.SDATA_APP_AUTO_OPEN_BACK, 5) + "秒");
                    }
                }).setSingleChoiceItems(items, select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        obj.setObj(which);
                    }
                }).create();
                dialog.show();
            }
        });
        sv_back_yanchi.setSummary(SharedPreUtil.getSharedPreInteger(CommonData.SDATA_APP_AUTO_OPEN_BACK, 5) + "秒");
    }

    private void openSelectApp(final String key) {
        String selectapp = SharedPreUtil.getSharedPreString(key);
        final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getOtherAppInfos());
        String[] items = new String[appInfos.size()];
        int select = -1;
        for (int i = 0; i < items.length; i++) {
            items[i] = appInfos.get(i).name + "(" + appInfos.get(i).clazz + ")";
            if (appInfos.get(i).clazz.equals(selectapp)) {
                select = i;
            }
        }
        final ThreadObj<Integer> obj = new ThreadObj<>(select);
        AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreUtil.saveSharedPreString(key, appInfos.get(obj.getObj()).clazz);
            }
        }).setSingleChoiceItems(items, select, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                obj.setObj(which);
            }
        }).create();
        dialog.show();
    }

}
