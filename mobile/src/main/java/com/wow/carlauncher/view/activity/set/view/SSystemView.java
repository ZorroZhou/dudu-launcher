package com.wow.carlauncher.view.activity.set.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.view.activity.AboutActivity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SSystemView extends FrameLayout {

    public SSystemView(@NonNull Context context) {
        super(context);
        initView();
    }

    public SSystemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @ViewInject(R.id.sv_sys_anquan)
    private SetView sv_sys_anquan;

    @ViewInject(R.id.sv_sys_overlay)
    private SetView sv_sys_overlay;

    @ViewInject(R.id.sv_sys_sdk)
    private SetView sv_sys_sdk;


    @ViewInject(R.id.sv_about)
    private SetView sv_about;

    @ViewInject(R.id.sv_money)
    private SetView sv_money;

    private void initView() {
        LinearLayout view = (LinearLayout) View.inflate(getContext(), R.layout.content_set_system, null);
        this.addView(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        x.view().inject(this);

        sv_sys_overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getOtherAppInfos());
                    String[] items = new String[appInfos.size()];
                    for (int i = 0; i < items.length; i++) {
                        items[i] = appInfos.get(i).name + "(" + appInfos.get(i).clazz + ")";
                    }

                    AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + appInfos.get(which).clazz));
                            getActivity().startActivity(intent);
                        }
                    }).create();
                    dialog.show();
                } else {
                    ToastManage.self().show("这个功能是安卓6.0以上才有的");
                }
            }
        });

        sv_sys_anquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                getActivity().startActivity(intent);
            }
        });

        sv_sys_sdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastManage.self().show("当前SDK版本是" + Build.VERSION.SDK_INT);
            }
        });

        sv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), AboutActivity.class));
            }
        });

        sv_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(R.mipmap.money);
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(imageView).setTitle("支持我吧!").setPositiveButton("确定", null).create();
                dialog.show();
            }
        });
    }

    private Activity getActivity() {
        return (Activity) getContext();
    }
}
