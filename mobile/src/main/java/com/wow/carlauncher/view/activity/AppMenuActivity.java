package com.wow.carlauncher.view.activity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.appInfo.event.MAppInfoRefreshEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.view.adapter.AllAppAdapter;
import com.wow.carlauncher.view.base.BaseActivity;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class AppMenuActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private AllAppAdapter adapter;

    @ViewInject(R.id.gridview)
    private GridView gridview;

    private PackageManager pm;

    @Override
    public void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContent(R.layout.activity_all_app);
        adapter = new AllAppAdapter(this);
        pm = getPackageManager();
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        setTitle("全部应用");

        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);
        gridview.setOnItemLongClickListener(this);

        setBackground(WallpaperManager.getInstance(this).getDrawable());
    }

    @Override
    public void loadData() {
        x.task().run(new Runnable() {
            @Override
            public void run() {
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
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AppInfo info = adapter.getItem(i);
        AppInfoManage.self().openApp(info.appMark + CommonData.CONSTANT_APP_PACKAGE_SEPARATE + info.clazz);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final AppInfo info = adapter.getItem(position);
        if (info.appMark == AppInfo.MARK_OTHER_APP) {
            final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(R.layout.dialog_menu_all_app).show();
            dialog.findViewById(R.id.run).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    AppInfoManage.self().openApp(info.appMark + CommonData.CONSTANT_APP_PACKAGE_SEPARATE + info.clazz);
                    finish();
                }
            });
            dialog.findViewById(R.id.un).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    un(info);
                }
            });
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ToastManage.self().show("卸载成功");
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final MAppInfoRefreshEvent event) {
        loadData();
    }

    private void un(AppInfo info) {
        Intent deleteIntent = new Intent();
        deleteIntent.setAction(Intent.ACTION_DELETE);
        deleteIntent.setData(Uri.parse("package:" + info.clazz));
        startActivityForResult(deleteIntent, 0);
    }

}
