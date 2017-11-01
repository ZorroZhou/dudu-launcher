package com.wow.carlauncher.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.activity.adapter.AllAppAdapter;
import com.wow.carlauncher.common.BaseActivity;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.CommonUtil.AppInfo;
import com.wow.carlauncher.common.util.SharedPreUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 10124 on 2017/10/26.
 */

public class AppMenuActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private AllAppAdapter adapter;

    @ViewInject(R.id.gridview)
    private GridView gridview;

    private PackageManager pm;

    @Override
    public void init() {
        setContent(R.layout.activity_all_app);
        adapter = new AllAppAdapter(this);
        pm = getPackageManager();
    }

    @Override
    public void initView() {
        setTitle("全部应用");

        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);
        gridview.setOnItemLongClickListener(this);
    }

    @Override
    public void loadData() {
        showLoading("加载中。。。。", new ProgressInterruptListener() {
            @Override
            public void onProgressInterruptListener(ProgressDialog progressDialog) {
                finish();
            }
        });
        x.task().run(new Runnable() {
            @Override
            public void run() {
                List<PackageInfo> packages = pm.getInstalledPackages(0);
                final List<AppInfo> appInfos = CommonUtil.getAllApp(mContext, true);
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_HIDE_APPS);
                List<AppInfo> hides = new ArrayList<>();
                for (AppInfo appInfo : appInfos) {
                    if (selectapp.indexOf("[" + appInfo.packageName + "]") >= 0) {
                        hides.add(appInfo);
                    }
                }
                appInfos.removeAll(hides);

                x.task().autoPost(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addItems(appInfos);
                        hideLoading();
                    }
                });
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AppInfo info = adapter.getItem(i);
        run(info);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final AppInfo info = adapter.getItem(position);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(R.layout.dialog_menu_all_app).show();
        dialog.findViewById(R.id.run).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run(info);
            }
        });
        dialog.findViewById(R.id.un).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                un(info);
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showTip("卸载成功");
        loadData();
    }

    private void run(AppInfo info) {
        Intent appIntent = pm.getLaunchIntentForPackage(info.packageName);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(appIntent);
        finish();
    }

    private void un(AppInfo info) {
        Intent deleteIntent = new Intent();
        deleteIntent.setAction(Intent.ACTION_DELETE);
        deleteIntent.setData(Uri.parse("package:" + info.packageName));
        startActivityForResult(deleteIntent, 0);
    }

}
