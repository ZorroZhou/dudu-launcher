package com.wow.carlauncher.view.activity.set.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.ThemeManage.ThemeMode;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.console.ConsolePlugin;
import com.wow.carlauncher.ex.plugin.console.ConsoleProtoclEnum;
import com.wow.carlauncher.ex.plugin.music.MusicControllerEnum;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.view.activity.launcher.event.LEventCityRefresh;
import com.wow.carlauncher.view.activity.launcher.event.LauncherDockLabelShowChangeEvent;
import com.wow.carlauncher.view.activity.set.SetAppMultipleSelectOnClickListener;
import com.wow.carlauncher.view.activity.set.SetAppSingleSelectOnClickListener;
import com.wow.carlauncher.view.activity.set.SetSwitchOnClickListener;
import com.wow.carlauncher.view.activity.set.SetEnumOnClickListener;
import com.wow.carlauncher.view.base.BaseView;
import com.wow.carlauncher.view.dialog.CityDialog;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;

import static com.wow.carlauncher.common.CommonData.SDATA_APP_THEME;
import static com.wow.carlauncher.common.CommonData.SDATA_CONSOLE_MARK;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_CONTROLLER;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SAppView extends BaseView {
    public static final MusicControllerEnum[] ALL_MUSIC_CONTROLLER = {MusicControllerEnum.SYSMUSIC,
            MusicControllerEnum.QQCARMUSIC,
            MusicControllerEnum.JIDOUMUSIC,
            MusicControllerEnum.NWDMUSIC};

    public static final ConsoleProtoclEnum[] ALL_CONSOLES = {ConsoleProtoclEnum.SYSTEM, ConsoleProtoclEnum.NWD};

    public static final ThemeMode[] THEME_MODEL = {ThemeMode.SHIJIAN, ThemeMode.DENGGUANG, ThemeMode.BAISE, ThemeMode.HEISE};


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

    @ViewInject(R.id.sv_plugin_select)
    private SetView sv_plugin_select;

    @ViewInject(R.id.sv_wall_set)
    private SetView sv_wall_set;

    @ViewInject(R.id.sv_apps_hides)
    private SetView sv_apps_hides;

    @ViewInject(R.id.sv_console)
    private SetView sv_console;

    @ViewInject(R.id.sv_launcher_show_dock_label)
    private SetView sv_launcher_show_dock_label;

    @ViewInject(R.id.time_plugin_open_app_select)
    private SetView time_plugin_open_app_select;


    @ViewInject(R.id.tianqi_city)
    private SetView tianqi_city;

    @ViewInject(R.id.sv_plugin_theme)
    private SetView sv_plugin_theme;

    @Override
    protected void initView() {
        sv_plugin_theme.setSummary(ThemeMode.getById(SharedPreUtil.getSharedPreInteger(SDATA_APP_THEME, ThemeMode.SHIJIAN.getId())).getName());
        sv_plugin_theme.setOnClickListener(new SetEnumOnClickListener<ThemeMode>(getContext(), THEME_MODEL) {
            @Override
            public ThemeMode getCurr() {
                return ThemeMode.getById(SharedPreUtil.getSharedPreInteger(SDATA_APP_THEME, ThemeMode.SHIJIAN.getId()));
            }

            @Override
            public void onSelect(ThemeMode setEnum) {
                SharedPreUtil.saveSharedPreInteger(SDATA_APP_THEME, setEnum.getId());
                sv_plugin_theme.setSummary(setEnum.getName());
                ThemeManage.self().refreshTheme();
            }
        });


        sv_plugin_select.setSummary(MusicControllerEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_CONTROLLER, MusicControllerEnum.SYSMUSIC.getId())).getName());
        sv_plugin_select.setOnClickListener(new SetEnumOnClickListener<MusicControllerEnum>(getContext(), ALL_MUSIC_CONTROLLER) {
            @Override
            public MusicControllerEnum getCurr() {
                return MusicControllerEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_CONTROLLER, MusicControllerEnum.SYSMUSIC.getId()));
            }

            @Override
            public void onSelect(MusicControllerEnum setEnum) {
                SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_CONTROLLER, setEnum.getId());
                MusicPlugin.self().setController(setEnum);
                sv_plugin_select.setSummary(setEnum.getName());
            }
        });

        sv_wall_set.setOnClickListener(view -> ToastManage.self().show("请使用第三方看图软件直接将图片设置为安卓壁纸即可"));
        sv_apps_hides.setOnClickListener(new SetAppMultipleSelectOnClickListener(getContext()) {
            @Override
            public String getCurr() {
                return SharedPreUtil.getSharedPreString(CommonData.SDATA_HIDE_APPS);
            }

            @Override
            public void onSelect(String t) {
                SharedPreUtil.saveSharedPreString(CommonData.SDATA_HIDE_APPS, t);
            }
        });

        ConsoleProtoclEnum c1 = ConsoleProtoclEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_CONSOLE_MARK, ConsoleProtoclEnum.SYSTEM.getId()));
        sv_console.setSummary("控制协议：" + c1.getName());
        sv_console.setOnClickListener(new SetEnumOnClickListener<ConsoleProtoclEnum>(getContext(), ALL_CONSOLES) {
            @Override
            public ConsoleProtoclEnum getCurr() {
                return ConsoleProtoclEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_CONSOLE_MARK, ConsoleProtoclEnum.SYSTEM.getId()));
            }

            @Override
            public void onSelect(ConsoleProtoclEnum setEnum) {
                SharedPreUtil.saveSharedPreInteger(SDATA_CONSOLE_MARK, setEnum.getId());
                sv_plugin_select.setSummary("控制协议：" + setEnum.getName());
                ConsolePlugin.self().loadConsole();
            }
        });

        sv_launcher_show_dock_label.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW) {
            @Override
            public void newValue(boolean value) {
                EventBus.getDefault().post(new LauncherDockLabelShowChangeEvent(value));
            }
        });

        sv_launcher_show_dock_label.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW, true));
        time_plugin_open_app_select.setOnClickListener(new SetAppSingleSelectOnClickListener(getContext()) {
            @Override
            public String getCurr() {
                return SharedPreUtil.getSharedPreString(CommonData.SDATA_TIME_PLUGIN_OPEN_APP);
            }

            @Override
            public void onSelect(String t) {
                SharedPreUtil.saveSharedPreString(CommonData.SDATA_TIME_PLUGIN_OPEN_APP, t);
            }
        });

        tianqi_city.setOnClickListener(view -> {
            final CityDialog cityDialog = new CityDialog(getContext());
            cityDialog.setOkclickListener(dialog -> {
                if (CommonUtil.isNotNull(cityDialog.getDistrictName())) {
                    SharedPreUtil.saveSharedPreString(CommonData.SDATA_WEATHER_CITY, cityDialog.getDistrictName());
                    cityDialog.dismiss();
                    EventBus.getDefault().post(new LEventCityRefresh());
                    tianqi_city.setSummary(cityDialog.getDistrictName());
                    return true;
                } else {
                    ToastManage.self().show("请选择城市");
                    return false;
                }
            });
            cityDialog.show();
        });
        tianqi_city.setSummary(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY));
    }
}
