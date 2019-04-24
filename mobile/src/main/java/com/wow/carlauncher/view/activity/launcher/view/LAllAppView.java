package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.appInfo.event.MAppInfoRefreshEvent;
import com.wow.carlauncher.view.activity.launcher.AllAppAdapter;
import com.wow.carlauncher.view.activity.launcher.event.LItemRefreshEvent;
import com.wow.carlauncher.view.base.BaseEXView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.SDATA_DOCK1_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK2_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK3_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK4_CLASS;

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
        if (gridview != null) {
            int psize = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, 3);
            if (psize != 3) {
                psize = 4;
            }
            gridview.setNumColumns(psize);
        }

        this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (gridview.getHeight() > 0) {
                    LAllAppView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                    adapter.setHeight(gridview.getHeight() / 4);
                }
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
        x.task().autoPost(() -> {
            adapter.clear();
            final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getAllAppInfos());

            String packname1 = SharedPreUtil.getSharedPreString(SDATA_DOCK1_CLASS);
            String packname2 = SharedPreUtil.getSharedPreString(SDATA_DOCK2_CLASS);
            String packname3 = SharedPreUtil.getSharedPreString(SDATA_DOCK3_CLASS);
            String packname4 = SharedPreUtil.getSharedPreString(SDATA_DOCK4_CLASS);

            String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_HIDE_APPS);
            List<AppInfo> hides = new ArrayList<>();
            for (AppInfo appInfo : appInfos) {
                if (selectapp.contains("[" + appInfo.clazz + "]")
                        || (CommonUtil.isNotNull(packname1) && packname1.contains(appInfo.clazz))
                        || (CommonUtil.isNotNull(packname2) && packname2.contains(appInfo.clazz))
                        || (CommonUtil.isNotNull(packname3) && packname3.contains(appInfo.clazz))
                        || (CommonUtil.isNotNull(packname4) && packname4.contains(appInfo.clazz))) {
                    hides.add(appInfo);
                }
            }
            appInfos.removeAll(hides);
            adapter.addItems(appInfos);
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LItemRefreshEvent event) {
        if (gridview != null) {
            int psize = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, 3);
            if (psize != 3) {
                psize = 4;
            }
            gridview.setNumColumns(psize);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final MAppInfoRefreshEvent event) {
        loadData();
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
        final AppInfo info = adapter.getItem(position);
        if (info.appMark == AppInfo.MARK_OTHER_APP) {
            final AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(R.layout.dialog_menu_all_app).show();
            dialog.findViewById(R.id.run).setOnClickListener(v -> {
                dialog.dismiss();
                AppInfoManage.self().openApp(info.appMark + CommonData.CONSTANT_APP_PACKAGE_SEPARATE + info.clazz);
            });
            dialog.findViewById(R.id.un).setOnClickListener(v -> {
                dialog.dismiss();
                un(info);
            });
        }
        return true;
    }

    private void un(AppInfo info) {
        Intent deleteIntent = new Intent();
        deleteIntent.setAction(Intent.ACTION_DELETE);
        deleteIntent.setData(Uri.parse("package:" + info.clazz));
        getContext().startActivity(deleteIntent);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
