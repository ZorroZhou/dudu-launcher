package com.wow.carlauncher.view.activity.set.view;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.console.ConsolePlugin;
import com.wow.carlauncher.ex.plugin.console.ConsoleProtoclEnum;
import com.wow.carlauncher.view.activity.launcher.event.LCityRefreshEvent;
import com.wow.carlauncher.view.activity.launcher.event.LDockLabelShowChangeEvent;
import com.wow.carlauncher.view.activity.set.SetAppMultipleSelectOnClickListener;
import com.wow.carlauncher.view.activity.set.SetEnumOnClickListener;
import com.wow.carlauncher.view.activity.set.SetSwitchOnClickListener;
import com.wow.carlauncher.view.activity.set.event.SEventRefreshAmapPlugin;
import com.wow.carlauncher.view.base.BaseEXView;
import com.wow.carlauncher.view.dialog.CityDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_AMAP_PLUGIN;
import static com.wow.carlauncher.common.CommonData.APP_WIDGET_HOST_ID;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_AMAP_PLUGIN;
import static com.wow.carlauncher.common.CommonData.SDATA_CONSOLE_MARK;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SAppView extends BaseEXView {

    private static final ConsoleProtoclEnum[] ALL_CONSOLES = {ConsoleProtoclEnum.SYSTEM, ConsoleProtoclEnum.NWD};

    public SAppView(@NonNull Context context) {
        super(context);
    }

    public SAppView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_app;
    }

    @ViewInject(R.id.sv_apps_hides)
    private SetView sv_apps_hides;

    @ViewInject(R.id.sv_console)
    private SetView sv_console;

    @ViewInject(R.id.sv_launcher_show_dock_label)
    private SetView sv_launcher_show_dock_label;


    @ViewInject(R.id.tianqi_city)
    private SetView tianqi_city;

    @ViewInject(R.id.sv_key_listener)
    private SetView sv_key_listener;


    @ViewInject(R.id.sv_gaode_chajian)
    private SetView sv_gaode_chajian;

    @ViewInject(R.id.sv_use_navc_popup)
    private SetView sv_use_navc_popup;


    private boolean showKey;
    private AppWidgetHost appWidgetHost;

    @Override
    protected void initView() {
        appWidgetHost = new AppWidgetHost(getContext(), APP_WIDGET_HOST_ID);


        sv_use_navc_popup.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_USE_NAVI));
        sv_use_navc_popup.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_USE_NAVI, false));


        int amapPluginId = SharedPreUtil.getInteger(APP_WIDGET_AMAP_PLUGIN, 0);
        if (amapPluginId > 0) {
            sv_gaode_chajian.setSummary("已选择,ID为:" + amapPluginId);
        } else {
            sv_gaode_chajian.setSummary("未选择");
        }

        sv_gaode_chajian.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int widgetId = appWidgetHost.allocateAppWidgetId();
                Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
                pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                ((Activity) getContext()).startActivityForResult(pickIntent, REQUEST_SELECT_AMAP_PLUGIN);
            }
        });


        sv_key_listener.setOnClickListener(v -> {
            showKey = true;
            new AlertDialog.Builder(getContext()).setTitle("开启").setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    showKey = false;
                }
            }).setPositiveButton("关闭", null).show();
        });


        final String[] itemsNum = {
                "3个", "4个"
        };


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

        sv_console.setSummary("控制协议：" + ConsoleProtoclEnum.getById(SharedPreUtil.getInteger(SDATA_CONSOLE_MARK, ConsoleProtoclEnum.SYSTEM.getId())).getName());
        sv_console.setOnClickListener(new SetEnumOnClickListener<ConsoleProtoclEnum>(getContext(), ALL_CONSOLES) {
            @Override
            public String title() {
                return "请选择系统控制协议";
            }

            @Override
            public ConsoleProtoclEnum getCurr() {
                return ConsoleProtoclEnum.getById(SharedPreUtil.getInteger(SDATA_CONSOLE_MARK, ConsoleProtoclEnum.SYSTEM.getId()));
            }

            @Override
            public void onSelect(ConsoleProtoclEnum setEnum) {
                SharedPreUtil.saveInteger(SDATA_CONSOLE_MARK, setEnum.getId());
                sv_console.setSummary("控制协议：" + setEnum.getName());
                ConsolePlugin.self().loadConsole();
            }
        });

        sv_launcher_show_dock_label.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW) {
            @Override
            public void newValue(boolean value) {
                EventBus.getDefault().post(new LDockLabelShowChangeEvent(value));
            }
        });
        sv_launcher_show_dock_label.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW, true));

        tianqi_city.setOnClickListener(view -> {
            final CityDialog cityDialog = new CityDialog(getContext());
            cityDialog.setOkclickListener(dialog -> {
                if (CommonUtil.isNotNull(cityDialog.getCityName()) && CommonUtil.isNotNull(cityDialog.getDistrictName())) {
                    SharedPreUtil.saveString(CommonData.SDATA_WEATHER_DISTRICT, cityDialog.getDistrictName());
                    SharedPreUtil.saveString(CommonData.SDATA_WEATHER_SHI, cityDialog.getCityName());
                    cityDialog.dismiss();
                    EventBus.getDefault().post(new LCityRefreshEvent());
                    tianqi_city.setSummary(cityDialog.getDistrictName());
                    return true;
                } else {
                    ToastManage.self().show("请选择城市");
                    return false;
                }
            });
            cityDialog.show();
        });
        tianqi_city.setSummary(SharedPreUtil.getString(CommonData.SDATA_WEATHER_DISTRICT));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (showKey) {
            ToastManage.self().show("KEY_CODE:" + keyCode);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final SEventRefreshAmapPlugin event) {
        sv_gaode_chajian.setSummary("已选择,ID为:" + SharedPreUtil.getInteger(APP_WIDGET_AMAP_PLUGIN, 0));
    }
}
