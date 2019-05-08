package com.wow.carlauncher.view.activity.set.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.view.activity.AboutActivity;
import com.wow.carlauncher.view.activity.launcher.event.LDockLabelShowChangeEvent;
import com.wow.carlauncher.view.activity.set.SetAppMultipleSelectOnClickListener;
import com.wow.carlauncher.view.activity.set.SetAppSingleSelectOnClickListener;
import com.wow.carlauncher.view.activity.set.SetSwitchOnClickListener;
import com.wow.carlauncher.view.base.BaseView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

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

    @BindView(R.id.sv_sys_anquan)SetView sv_sys_anquan;

    @BindView(R.id.sv_sys_overlay)SetView sv_sys_overlay;

    @BindView(R.id.sv_sys_sdk)SetView sv_sys_sdk;

    @BindView(R.id.sv_about)SetView sv_about;

    @BindView(R.id.sv_apps_hides)SetView sv_apps_hides;

    @BindView(R.id.sv_launcher_show_dock_label)SetView sv_launcher_show_dock_label;

    @BindView(R.id.sv_key_listener)SetView sv_key_listener;

    @BindView(R.id.sv_open_log)SetView sv_open_log;

    private boolean showKey;
    private BroadcastReceiver nwdKeyTestReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramContext, Intent paramIntent) {
            if ("com.nwd.action.ACTION_KEY_VALUE".equals(paramIntent.getAction())) {
                int key = paramIntent.getByteExtra("extra_key_value", (byte) 0);
                TaskExecutor.self().autoPost(new Runnable() {
                    @Override
                    public void run() {
                        ToastManage.self().show("NWD按键测试触发:" + key);
                    }
                });
            }
        }
    };

    @Override
    protected void initView() {

        sv_open_log.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_LAUNCHER_PROMPT_SHOW) {
            @Override
            public void newValue(boolean value) {
                SharedPreUtil.saveBoolean(CommonData.SDATA_LOG_OPEN, value);
                LogEx.setSaveFile(value);
            }
        });
        sv_open_log.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_LAUNCHER_PROMPT_SHOW, true));

        sv_key_listener.setOnClickListener(v -> {
            showKey = true;
            IntentFilter intentFilter2 = new IntentFilter();
            intentFilter2.addAction("com.nwd.action.ACTION_KEY_VALUE");
            getContext().registerReceiver(nwdKeyTestReceiver, intentFilter2);

            new AlertDialog.Builder(getContext()).setTitle("开启").setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    showKey = false;
                    getContext().unregisterReceiver(nwdKeyTestReceiver);
                }
            }).setPositiveButton("关闭", null).show();
        });

        sv_apps_hides.setOnClickListener(new SetAppMultipleSelectOnClickListener(getContext()) {
            @Override
            public String getCurr() {
                return SharedPreUtil.getString(CommonData.SDATA_HIDE_APPS);
            }

            @Override
            public void onSelect(String t) {
                SharedPreUtil.saveString(CommonData.SDATA_HIDE_APPS, t);
                AppInfoManage.self().refreshShowApp();
            }
        });

        sv_launcher_show_dock_label.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW) {
            @Override
            public void newValue(boolean value) {
                EventBus.getDefault().post(new LDockLabelShowChangeEvent(value));
            }
        });
        sv_launcher_show_dock_label.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW, true));

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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (showKey) {
            ToastManage.self().show("系统按键触发 KEY_CODE:" + keyCode);
        }
        return super.onKeyDown(keyCode, event);
    }
}
