package com.wow.carlauncher.plugin.amapcar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseDialog;
import com.wow.carlauncher.common.util.AppUtil;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.dialog.InputDialog;
import com.wow.carlauncher.plugin.IPlugin;
import com.wow.carlauncher.plugin.PluginManage;

import org.xutils.x;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.wow.carlauncher.plugin.amapcar.AMapCarConstant.*;

/**
 * Created by 10124 on 2017/11/4.
 */

public class AMapCarPlugin implements IPlugin, View.OnClickListener {
    public static final String TAG = "AMapCarPlugin";

    private PluginManage pluginManage;

    public PluginManage getPluginManage() {
        return pluginManage;
    }

    private Context context;

    public Context getContext() {
        return context;
    }

    private RelativeLayout launcherView;

    private AMapCartReceiver amapReceiver;
    private AMapCartSend amapSend;

    public AMapCartSend getAmapSend() {
        return amapSend;
    }

    public AMapCarPlugin(Context context, PluginManage pluginManage) {
        this.pluginManage = pluginManage;
        this.context = context;
        amapReceiver = new AMapCartReceiver(this);
        amapSend = new AMapCartSend(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_ACTION);
        context.registerReceiver(amapReceiver, intentFilter);
    }

    private void initLauncherView(View view) {
        view.findViewById(R.id.rl_base).setOnClickListener(this);
        view.findViewById(R.id.btn_search).setOnClickListener(this);
        view.findViewById(R.id.btn_go_home).setOnClickListener(this);
        view.findViewById(R.id.btn_go_company).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_base: {
                Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(AMAP_PACKAGE);
                if (appIntent == null) {
                    Toast.makeText(context, "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
                appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(appIntent);
                break;
            }
            case R.id.btn_search: {
                if (!AppUtil.isInstall(context, AMAP_PACKAGE)) {
                    Toast.makeText(context, "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
                new InputDialog(pluginManage.getCurrentActivity())
                        .setBtn1("取消", null)
                        .setBtn2("确定", new BaseDialog.OnBtnClickListener() {
                            @Override
                            public boolean onClick(BaseDialog dialog) {
                                String text = ((EditText) dialog.findViewById(R.id.et_input)).getText().toString();
                                if (CommonUtil.isNotNull(text)) {
                                    amapSend.search(text);
                                } else {
                                    Toast.makeText(context, "请输入目的地", Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            }
                        }).show();
                break;
            }
            case R.id.btn_go_home: {
                if (!AppUtil.isInstall(context, AMAP_PACKAGE)) {
                    Toast.makeText(context, "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
                amapSend.getHome();
                break;
            }
            case R.id.btn_go_company: {
                if (!AppUtil.isInstall(context, AMAP_PACKAGE)) {
                    Toast.makeText(context, "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
                amapSend.getComp();
                break;
            }
        }
    }

    @Override
    public View getLauncherView() {
        if (launcherView == null) {
            launcherView = (RelativeLayout) View.inflate(context, R.layout.plugin_amap_lanncher, null);
            initLauncherView(launcherView);
        }
        return launcherView;
    }

    @Override
    public View getPopupView() {
        return null;
    }

    @Override
    public void destroy() {
        context.unregisterReceiver(amapReceiver);
    }
}
