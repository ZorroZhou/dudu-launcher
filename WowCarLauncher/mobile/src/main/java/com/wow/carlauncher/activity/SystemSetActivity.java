package com.wow.carlauncher.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseActivity;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.CommonUtil.AppInfo;
import com.wow.carlauncher.common.view.SetView;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by 10124 on 2017/10/26.
 */

public class SystemSetActivity extends BaseActivity {
    private static final String TAG = "SystemSetActivity";
    @ViewInject(R.id.sv_sys_anquan)
    private SetView sv_sys_anquan;

    @ViewInject(R.id.sv_sys_overlay)
    private SetView sv_sys_overlay;

    @Override
    public void init() {
        setContent(R.layout.activity_system_set);
    }

    @Override
    public void initView() {
        setTitle("系统设置");
        sv_sys_overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    final List<AppInfo> appInfos = CommonUtil.getAllApp(mContext);
                    String[] items = new String[appInfos.size()];
                    for (int i = 0; i < items.length; i++) {
                        items[i] = appInfos.get(i).name + "(" + appInfos.get(i).packageName + ")";
                    }

                    AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + appInfos.get(which).packageName));
                            startActivity(intent);
                        }
                    }).create();
                    dialog.show();
                } else {
                    showTip("这个功能是安卓6.0以上才有的");
                }
            }
        });

        sv_sys_anquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                startActivity(intent);
            }
        });
    }
}
