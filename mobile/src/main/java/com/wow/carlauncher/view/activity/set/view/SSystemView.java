package com.wow.carlauncher.view.activity.set.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.view.activity.AboutActivity;
import com.wow.carlauncher.view.activity.set.SetAppSingleSelectOnClickListener;
import com.wow.carlauncher.view.base.BaseView;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SSystemView extends BaseView {

    public SSystemView(@NonNull Context context) {
        super(context);
    }

    public SSystemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_system;
    }

    @ViewInject(R.id.sv_sys_anquan)
    private SetView sv_sys_anquan;

    @ViewInject(R.id.sv_sys_overlay)
    private SetView sv_sys_overlay;

    @ViewInject(R.id.sv_sys_sdk)
    private SetView sv_sys_sdk;


    @ViewInject(R.id.sv_about)
    private SetView sv_about;

    @Override
    protected void initView() {

        sv_sys_overlay.setOnClickListener(new SetAppSingleSelectOnClickListener(getContext()) {
            @Override
            public String getCurr() {
                return null;
            }

            @Override
            public void onSelect(String t) {
                if (Build.VERSION.SDK_INT >= 23) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + t));
                    getActivity().startActivity(intent);
                } else {
                    ToastManage.self().show("这个功能是安卓6.0以上才有的");
                }
            }
        });

        sv_sys_anquan.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            getActivity().startActivity(intent);
        });

        sv_sys_sdk.setOnClickListener(v -> ToastManage.self().show("当前SDK版本是" + Build.VERSION.SDK_INT));

        sv_about.setOnClickListener(v -> getActivity().startActivity(new Intent(getContext(), AboutActivity.class)));
    }

    private Activity getActivity() {
        return (Activity) getContext();
    }
}
