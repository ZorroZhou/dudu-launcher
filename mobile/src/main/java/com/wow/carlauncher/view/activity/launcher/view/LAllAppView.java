package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.view.activity.launcher.AllAppAdapter;
import com.wow.carlauncher.view.base.BaseEXView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class LAllAppView extends BaseEXView implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public LAllAppView(Context context) {
        super(context);
    }

    @Override
    protected int getContent() {
        return R.layout.content_all_app;
    }

    private AllAppAdapter adapter;

    @Override
    protected void initView() {
        adapter = new AllAppAdapter(getContext());

        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);
        gridview.setOnItemLongClickListener(this);

        this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                LAllAppView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                adapter.setHeight(gridview.getHeight() / 4);
                return true;
            }
        });
        loadData();
    }

    @Override
    public void changedTheme(ThemeManage manage) {
        adapter.notifyDataSetChanged();
    }

    private void loadData() {
        x.task().run(() -> {
            adapter.clear();
            final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getAllAppInfos());
            String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_HIDE_APPS);
            List<AppInfo> hides = new ArrayList<>();
            for (AppInfo appInfo : appInfos) {
                if (selectapp.contains("[" + appInfo.clazz + "]")) {
                    hides.add(appInfo);
                }
            }
            appInfos.removeAll(hides);

            x.task().autoPost(new Runnable() {
                @Override
                public void run() {
                    adapter.addItems(appInfos);
                }
            });
        });
    }

    @ViewInject(R.id.gridview)
    private GridView gridview;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AppInfo info = adapter.getItem(position);
        AppInfoManage.self().openApp(info.appMark + CommonData.CONSTANT_APP_PACKAGE_SEPARATE + info.clazz);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}
