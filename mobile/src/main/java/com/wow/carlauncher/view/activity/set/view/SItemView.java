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
import android.view.View;
import android.view.WindowManager;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ThreadObj;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.console.ConsolePlugin;
import com.wow.carlauncher.ex.plugin.console.ConsoleProtoclEnum;
import com.wow.carlauncher.ex.plugin.music.MusicControllerEnum;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.view.activity.launcher.ItemEnum;
import com.wow.carlauncher.view.activity.launcher.ItemModel;
import com.wow.carlauncher.view.activity.launcher.ItemTransformer;
import com.wow.carlauncher.view.activity.launcher.event.LCityRefreshEvent;
import com.wow.carlauncher.view.activity.launcher.event.LItemRefreshEvent;
import com.wow.carlauncher.view.activity.launcher.event.LPageTransformerChangeEvent;
import com.wow.carlauncher.view.activity.set.LauncherItemAdapter;
import com.wow.carlauncher.view.activity.set.SetEnumOnClickListener;
import com.wow.carlauncher.view.activity.set.SetSwitchOnClickListener;
import com.wow.carlauncher.view.activity.set.event.SEventRefreshAmapPlugin;
import com.wow.carlauncher.view.base.BaseEXView;
import com.wow.carlauncher.view.dialog.CityDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_AMAP_PLUGIN;
import static com.wow.carlauncher.common.CommonData.APP_WIDGET_HOST_ID;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_AMAP_PLUGIN;
import static com.wow.carlauncher.common.CommonData.SDATA_CONSOLE_MARK;
import static com.wow.carlauncher.common.CommonData.SDATA_LAUNCHER_ITEM_TRAN;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_CONTROLLER;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SItemView extends BaseEXView {
    private static final ConsoleProtoclEnum[] ALL_CONSOLES = {ConsoleProtoclEnum.SYSTEM, ConsoleProtoclEnum.NWD};

    public SItemView(@NonNull Context context) {
        super(context);
    }

    public SItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_item;
    }

    @ViewInject(R.id.sv_plugin_select)
    private SetView sv_plugin_select;

    @ViewInject(R.id.sv_music_inside_cover)
    private SetView sv_music_inside_cover;

    @ViewInject(R.id.tianqi_city)
    private SetView tianqi_city;

    @ViewInject(R.id.sv_gaode_chajian)
    private SetView sv_gaode_chajian;

    @ViewInject(R.id.sv_use_navc_popup)
    private SetView sv_use_navc_popup;

    @ViewInject(R.id.sv_console)
    private SetView sv_console;

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

        sv_plugin_select.setSummary(MusicControllerEnum.getById(SharedPreUtil.getInteger(SDATA_MUSIC_CONTROLLER, MusicControllerEnum.SYSMUSIC.getId())).getName());
        sv_plugin_select.setOnClickListener(new SetEnumOnClickListener<MusicControllerEnum>(getContext(), CommonData.MUSIC_CONTROLLER) {

            @Override
            public String title() {
                return "请选择音乐控制协议";
            }

            @Override
            public MusicControllerEnum getCurr() {
                return MusicControllerEnum.getById(SharedPreUtil.getInteger(SDATA_MUSIC_CONTROLLER, MusicControllerEnum.SYSMUSIC.getId()));
            }

            @Override
            public void onSelect(MusicControllerEnum setEnum) {
                SharedPreUtil.saveInteger(SDATA_MUSIC_CONTROLLER, setEnum.getId());
                MusicPlugin.self().setController(setEnum);
                sv_plugin_select.setSummary(setEnum.getName());
            }
        });


        sv_music_inside_cover.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_MUSIC_INSIDE_COVER));
        sv_music_inside_cover.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_MUSIC_INSIDE_COVER, true));

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
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(final SEventRefreshAmapPlugin event) {
        sv_gaode_chajian.setSummary("已选择,ID为:" + SharedPreUtil.getInteger(APP_WIDGET_AMAP_PLUGIN, 0));
    }
}
