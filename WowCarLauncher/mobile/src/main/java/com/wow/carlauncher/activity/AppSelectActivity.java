package com.wow.carlauncher.activity;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.activity.adapter.SelectAppAdapter;
import com.wow.carlauncher.common.BaseActivity;
import com.wow.carlauncher.common.util.AppUtil;
import com.wow.carlauncher.common.util.AppUtil.AppInfo;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import static com.wow.carlauncher.common.CommonData.IDATA_PACKAGE_NAME;

/**
 * Created by 10124 on 2017/10/26.
 */

public class AppSelectActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private SelectAppAdapter adapter;

    @ViewInject(R.id.listview)
    private ListView gridview;

    @Override
    public void init() {
        setContent(R.layout.activity_select_app);

        adapter = new SelectAppAdapter(this);
    }

    @Override
    public void initView() {
        setTitle("应用选择");

        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);

        setBackground(WallpaperManager.getInstance(this).getDrawable());
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
                final List<AppInfo> appInfos = AppUtil.getAllApp(mContext);
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
        Intent data = new Intent();
        data.putExtra(IDATA_PACKAGE_NAME, info.packageName);
        setResult(RESULT_OK, data);
        finish();
    }
}
