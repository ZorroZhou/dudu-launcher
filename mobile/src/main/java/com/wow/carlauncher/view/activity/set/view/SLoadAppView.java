package com.wow.carlauncher.view.activity.set.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ThreadObj;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.setItem.SetAppInfo;
import com.wow.carlauncher.view.activity.set.SetBaseView;
import com.wow.carlauncher.view.activity.set.listener.SetSingleSelectView;
import com.wow.carlauncher.view.activity.set.listener.SetSwitchOnClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;

import static com.wow.carlauncher.common.CommonData.SDATA_APP_AUTO_OPEN1;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_AUTO_OPEN2;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_AUTO_OPEN3;
import static com.wow.carlauncher.common.CommonData.SDATA_APP_AUTO_OPEN4;

/**
 * Created by 10124 on 2018/4/22.
 */


@SuppressLint("ViewConstructor")
public class SLoadAppView extends SetBaseView {
    public SLoadAppView(SetActivity activity) {
        super(activity);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_load_app;
    }

    @Override
    public String getName() {
        return "APP联动设置";
    }

    @BindView(R.id.sv_load_use)
    SetView sv_load_use;

    @BindView(R.id.sv_open1)
    SetView sv_open1;
    @BindView(R.id.sv_open2)
    SetView sv_open2;
    @BindView(R.id.sv_open3)
    SetView sv_open3;
    @BindView(R.id.sv_open4)
    SetView sv_open4;
    @BindView(R.id.sv_clear)
    SetView sv_clear;
    @BindView(R.id.sv_back_yanchi)
    SetView sv_back_yanchi;

    @Override
    protected void initView() {
        sv_open1.setOnClickListener(new MyListener(getActivity(), SDATA_APP_AUTO_OPEN1));
        sv_open2.setOnClickListener(new MyListener(getActivity(), SDATA_APP_AUTO_OPEN2));
        sv_open3.setOnClickListener(new MyListener(getActivity(), SDATA_APP_AUTO_OPEN3));
        sv_open4.setOnClickListener(new MyListener(getActivity(), SDATA_APP_AUTO_OPEN4));

        setSTitle(SDATA_APP_AUTO_OPEN1, sv_open1);
        setSTitle(SDATA_APP_AUTO_OPEN2, sv_open2);
        setSTitle(SDATA_APP_AUTO_OPEN3, sv_open3);
        setSTitle(SDATA_APP_AUTO_OPEN4, sv_open4);

        sv_clear.setOnClickListener(v -> {
            SharedPreUtil.saveString(SDATA_APP_AUTO_OPEN1, "");
            SharedPreUtil.saveString(SDATA_APP_AUTO_OPEN2, "");
            SharedPreUtil.saveString(SDATA_APP_AUTO_OPEN3, "");
            SharedPreUtil.saveString(SDATA_APP_AUTO_OPEN4, "");


            setSTitle(SDATA_APP_AUTO_OPEN1, sv_open1);
            setSTitle(SDATA_APP_AUTO_OPEN2, sv_open2);
            setSTitle(SDATA_APP_AUTO_OPEN3, sv_open3);
            setSTitle(SDATA_APP_AUTO_OPEN4, sv_open4);
        });

        sv_load_use.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_APP_AUTO_OPEN_USE));
        sv_load_use.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_APP_AUTO_OPEN_USE, false));

        sv_back_yanchi.setOnClickListener(v -> {
            String[] items = {
                    "1秒", "2秒", "3秒", "4秒", "5秒", "6秒", "7秒", "8秒", "9秒", "10秒"
            };
            int select = SharedPreUtil.getInteger(CommonData.SDATA_APP_AUTO_OPEN_BACK, CommonData.SDATA_APP_AUTO_OPEN_BACK_DF) - 1;
            final ThreadObj<Integer> obj = new ThreadObj<>(select);
            AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreUtil.saveInteger(CommonData.SDATA_APP_AUTO_OPEN_BACK, obj.getObj() + 1);
                    sv_back_yanchi.setSummary(SharedPreUtil.getInteger(CommonData.SDATA_APP_AUTO_OPEN_BACK, CommonData.SDATA_APP_AUTO_OPEN_BACK_DF) + "秒");
                }
            }).setSingleChoiceItems(items, select, (dialog1, which) -> obj.setObj(which)).show();
        });
        sv_back_yanchi.setSummary(SharedPreUtil.getInteger(CommonData.SDATA_APP_AUTO_OPEN_BACK, CommonData.SDATA_APP_AUTO_OPEN_BACK_DF) + "秒");
    }

    private void setSTitle(String key, SetView setView) {
        String xx = SharedPreUtil.getString(key);
        if (CommonUtil.isNotNull(xx)) {
            setView.setSummary(AppInfoManage.self().getName(xx).toString());
        } else {
            setView.setSummary("没有选择");
        }
    }

    class MyListener extends SetSingleSelectView<SetAppInfo> {
        public MyListener(SetActivity context, String key) {
            super(context, "选择一个APP");
            this.key = key;
        }

        private String key;

        @Override
        public Collection<SetAppInfo> getAll() {
            Collection<SetAppInfo> temp = new ArrayList<>();
            final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getOtherAppInfos());
            for (AppInfo appInfo : appInfos) {
                temp.add(new SetAppInfo(appInfo));
            }
            return temp;
        }

        // return SharedPreUtil.getString(key);
        @Override
        public SetAppInfo getCurr() {
            AppInfo appInfo = AppInfoManage.self().getAllAppInfosMap().get(SharedPreUtil.getString(key));
            if (appInfo != null) {
                return new SetAppInfo(appInfo);
            }
            return null;
        }

        @Override
        public boolean equals(SetAppInfo t1, SetAppInfo t2) {
            return t1 != null && t2 != null && CommonUtil.equals(t1.getAppInfo().clazz, t2.getAppInfo().clazz);
        }

        @Override
        public void onSelect(SetAppInfo t) {
            if (key.equals(SDATA_APP_AUTO_OPEN1)) {
                setSTitle(SDATA_APP_AUTO_OPEN1, sv_open1);
            } else if (key.equals(SDATA_APP_AUTO_OPEN2)) {
                setSTitle(SDATA_APP_AUTO_OPEN2, sv_open2);
            } else if (key.equals(SDATA_APP_AUTO_OPEN3)) {
                setSTitle(SDATA_APP_AUTO_OPEN3, sv_open3);
            } else if (key.equals(SDATA_APP_AUTO_OPEN4)) {
                setSTitle(SDATA_APP_AUTO_OPEN4, sv_open4);
            }
        }
    }

}
