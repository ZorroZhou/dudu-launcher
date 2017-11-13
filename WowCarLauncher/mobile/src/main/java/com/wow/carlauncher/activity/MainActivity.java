package com.wow.carlauncher.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseActivity;

import org.xutils.view.annotation.ViewInject;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.set)
    private Button set;

    @ViewInject(R.id.sys_set)
    private Button sys_set;

    @ViewInject(R.id.test)
    private Button test;

    @Override
    public void init() {
        setContent(R.layout.activity_main);
    }

    @Override
    public void initView() {
        setTitle("车载启动器");
        set.setOnClickListener(this);
        sys_set.setOnClickListener(this);
        test.setOnClickListener(this);

        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            new AlertDialog.Builder(mContext).setTitle("系统提示")
                    .setMessage("APP需要弹出窗口权限！取消后可在APP设置调整！")
                    .setPositiveButton("前往设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //其实这个判断没什么卵用，但是不加会有警告
                            if (Build.VERSION.SDK_INT >= 23) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                startActivity(intent);
                            }
                        }
                    }).setNegativeButton("不在提示", null).show();

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set: {
                startActivity(new Intent(this, SetActivity.class));
                break;
            }
            case R.id.test: {
                startActivity(new Intent(this, LauncherActivity.class));
                finish();
                break;
            }
        }
    }
}
