package com.wow.carlauncher.view.activity;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.view.adapter.SelectAppAdapter;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.view.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.IDATA_APP_MARK;
import static com.wow.carlauncher.common.CommonData.IDATA_APP_PACKAGE_NAME;

/**
 * Created by 10124 on 2017/10/26.
 */

public class AppSelectActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private SelectAppAdapter adapter;

    @ViewInject(R.id.listview)
    private ListView gridview;

    @Override
    public void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getAllAppInfos());
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
        data.putExtra(IDATA_APP_PACKAGE_NAME, info.clazz);
        data.putExtra(IDATA_APP_MARK, info.appMark);
        setResult(RESULT_OK, data);
        finish();
    }
}
