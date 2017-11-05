package com.wow.carlauncher.plugin.amapcar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.AppUtil;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.plugin.IPlugin;
import com.wow.carlauncher.plugin.PluginManage;

import java.util.HashMap;
import java.util.Map;

import static com.wow.carlauncher.plugin.amapcar.AMapCarConstant.*;

/**
 * Created by 10124 on 2017/11/4.
 */

public class AMapCarPlugin implements IPlugin, View.OnClickListener {
    private PluginManage pluginManage;
    private Context context;
    private RelativeLayout launcherView;

    public AMapCarPlugin(Context context) {
        this.context = context;
    }

    private void initLauncherView(View view) {
        view.findViewById(R.id.rl_base).setOnClickListener(this);
        view.findViewById(R.id.btn_search).setOnClickListener(this);
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
                new AlertDialog.Builder(context)
                        .setTitle("请输入地址信息")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(R.layout.dialog_input)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String text = ((EditText) ((AlertDialog) dialog).findViewById(R.id.et_input)).getText().toString();
                                dialog.dismiss();

                                if (CommonUtil.isNotNull(text)) {
                                    Map<String, Object> param = new HashMap<>();
                                    param.put(REQUEST_SEARCH_KEYWORDS, text);
                                    sendReceiver(REQUEST_SEARCH, null);
                                } else {
                                    Toast.makeText(context, "请输入目的地", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
            case R.id.btn_go_home: {
                if (!AppUtil.isInstall(context, AMAP_PACKAGE)) {
                    Toast.makeText(context, "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }

    @Override
    public void setPluginManage(PluginManage pluginManage) {
        this.pluginManage = pluginManage;
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

    }

    private void sendReceiver(int key, Map<String, Object> param) {
        Intent intent = new Intent();
        intent.setAction(SEND_ACTION);
        intent.putExtra(KEY_TYPE, key);
        intent.putExtra("SOURCE_APP", "车机优化");
        if (param != null) {
            for (String k : param.keySet()) {
                Object value = param.get(k);
                if (value instanceof Integer) {
                    intent.putExtra(k, (Integer) value);
                } else if (value instanceof Double) {
                    intent.putExtra(k, (Double) value);
                } else if (value instanceof String) {
                    intent.putExtra(k, (String) value);
                }
            }
        }
        context.sendBroadcast(intent);
    }

    private BroadcastReceiver amapReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RECEIVE_ACTION)) {
                int key = intent.getIntExtra(KEY_TYPE, -1);
                switch (key) {
                    case RESPONSE_DISTRICT: {
                        intent.getStringExtra(RESPONSE_DISTRICT_PRVINCE_NAME);
                        intent.getStringExtra(RESPONSE_DISTRICT_CITY_NAME);
                        break;
                    }
                }
            }
        }
    };
}
