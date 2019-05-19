package com.wow.carlauncher.view.activity.set.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.content.Intent;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.console.ConsolePlugin;
import com.wow.carlauncher.ex.plugin.console.ConsoleProtoclEnum;
import com.wow.carlauncher.ex.plugin.music.MusicControllerEnum;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.view.activity.launcher.event.LAMapCloseXunhang;
import com.wow.carlauncher.view.activity.launcher.event.LCityRefreshEvent;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;
import com.wow.carlauncher.view.activity.set.event.SEventRefreshAmapPlugin;
import com.wow.carlauncher.view.activity.set.listener.SetSingleSelectView;
import com.wow.carlauncher.view.activity.set.listener.SetSwitchOnClickListener;
import com.wow.carlauncher.view.dialog.CityDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.Collection;

import butterknife.BindView;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_AMAP_PLUGIN;
import static com.wow.carlauncher.common.CommonData.APP_WIDGET_HOST_ID;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_AMAP_PLUGIN;
import static com.wow.carlauncher.common.CommonData.SDATA_CONSOLE_MARK;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_CONTROLLER;

/**
 * Created by 10124 on 2018/4/22.
 */

@SuppressLint("ViewConstructor")
public class SItemView extends SetBaseView {

    public SItemView(SetActivity activity) {
        super(activity);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_item;
    }

    @Override
    public String getName() {
        return "插件设置";
    }

    @BindView(R.id.sv_plugin_select)
    SetView sv_plugin_select;

    @BindView(R.id.sv_music_inside_cover)
    SetView sv_music_inside_cover;

    @BindView(R.id.tianqi_city)
    SetView tianqi_city;

    @BindView(R.id.sv_gaode_chajian)
    SetView sv_gaode_chajian;

    @BindView(R.id.sv_use_navc_popup)
    SetView sv_use_navc_popup;

    @BindView(R.id.sv_console)
    SetView sv_console;

    @BindView(R.id.sv_music_lrc)
    SetView sv_music_lrc;

    @BindView(R.id.sv_amap_xunhang)
    SetView sv_amap_xunhang;

    private AppWidgetHost appWidgetHost;

    @Override
    protected void initView() {
        appWidgetHost = new AppWidgetHost(getContext(), APP_WIDGET_HOST_ID);

        sv_amap_xunhang.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_USE_NAVI_XUNHYANG) {
            @Override
            public void newValue(boolean value) {
                if (!value) {
                    EventBus.getDefault().post(new LAMapCloseXunhang());
                }
            }
        });
        sv_amap_xunhang.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_USE_NAVI_XUNHYANG, false));


        sv_use_navc_popup.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_USE_NAVI_POP));
        sv_use_navc_popup.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_USE_NAVI_POP, false));


        int amapPluginId = SharedPreUtil.getInteger(APP_WIDGET_AMAP_PLUGIN, 0);
        if (amapPluginId > 0) {
            sv_gaode_chajian.setSummary("已选择,ID为:" + amapPluginId);
        } else {
            sv_gaode_chajian.setSummary("未选择");
        }

        sv_gaode_chajian.setOnClickListener(v -> {
            int widgetId = appWidgetHost.allocateAppWidgetId();
            Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
            pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            ((Activity) getContext()).startActivityForResult(pickIntent, REQUEST_SELECT_AMAP_PLUGIN);
        });

        sv_plugin_select.setSummary(MusicControllerEnum.getById(SharedPreUtil.getInteger(SDATA_MUSIC_CONTROLLER, MusicControllerEnum.SYSMUSIC.getId())).getName());
        sv_plugin_select.setOnClickListener(new SetSingleSelectView<MusicControllerEnum>(getActivity(), "请选择音乐控制协议") {
            @Override
            public Collection<MusicControllerEnum> getAll() {
                return Arrays.asList(CommonData.MUSIC_CONTROLLER);
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

        sv_music_lrc.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_MUSIC_USE_LRC));
        sv_music_lrc.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_MUSIC_USE_LRC, true));

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
        sv_console.setOnClickListener(new SetSingleSelectView<ConsoleProtoclEnum>(getActivity(), "请选择系统控制协议") {

            @Override
            public Collection<ConsoleProtoclEnum> getAll() {
                return Arrays.asList(CommonData.CONSOLES_PROTOCL);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final SEventRefreshAmapPlugin event) {
        sv_gaode_chajian.setSummary("已选择,ID为:" + SharedPreUtil.getInteger(APP_WIDGET_AMAP_PLUGIN, 0));
    }
}
