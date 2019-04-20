package com.wow.carlauncher.view.activity.set.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.ThreadObj;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.ThemeManage.ThemeMode;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.console.ConsolePlugin;
import com.wow.carlauncher.ex.plugin.console.ConsoleProtoclEnum;
import com.wow.carlauncher.ex.plugin.music.MusicControllerEnum;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.view.activity.launcher.ItemEnum;
import com.wow.carlauncher.view.activity.launcher.ItemModel;
import com.wow.carlauncher.view.activity.launcher.event.LCityRefreshEvent;
import com.wow.carlauncher.view.activity.launcher.event.LDockLabelShowChangeEvent;
import com.wow.carlauncher.view.activity.launcher.event.LItemRefreshEvent;
import com.wow.carlauncher.view.activity.set.LauncherItemAdapter;
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

import java.util.List;

import static com.wow.carlauncher.common.CommonData.SDATA_APP_THEME;
import static com.wow.carlauncher.common.CommonData.SDATA_CONSOLE_MARK;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_CONTROLLER;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SAppView extends BaseView {

    private static final ConsoleProtoclEnum[] ALL_CONSOLES = {ConsoleProtoclEnum.SYSTEM, ConsoleProtoclEnum.NWD};

    private static final ThemeMode[] THEME_MODEL = {ThemeMode.SHIJIAN, ThemeMode.DENGGUANG, ThemeMode.BAISE, ThemeMode.HEISE};

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

    @ViewInject(R.id.sv_launcher_item_sort)
    private SetView sv_launcher_item_sort;

    @ViewInject(R.id.sv_launcher_item_sort_re)
    private SetView sv_launcher_item_sort_re;

    @ViewInject(R.id.sv_launcher_item_num)
    private SetView sv_launcher_item_num;


    @Override
    protected void initView() {
        final String[] itemsNum = {
                "3个", "4个"
        };
        sv_launcher_item_num.setSummary(SharedPreUtil.getSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, 3) + "个");
        sv_launcher_item_num.setOnClickListener(v -> {
            int select = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, 3) - 3;
            final ThreadObj<Integer> obj = new ThreadObj<>(select);
            AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("请选择首页的插件数量").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog queren = new AlertDialog.Builder(getContext()).setTitle("警告!").setNegativeButton("取消", null).setPositiveButton("确定", (dialog2, which2) -> {
                        SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_NUM, obj.getObj() + 3);
                        sv_launcher_item_num.setSummary(itemsNum[obj.getObj()]);
                        EventBus.getDefault().post(new LItemRefreshEvent());
                    }).setMessage("是否确认更改,会导致桌面插件重新加载").create();
                    queren.show();
                }
            }).setSingleChoiceItems(itemsNum, select, (dialog12, which) -> obj.setObj(which)).create();
            dialog.show();
        });

        sv_launcher_item_sort_re.setOnClickListener(v -> {
            AlertDialog queren = new AlertDialog.Builder(getContext()).setTitle("警告!").setNegativeButton("取消", null).setPositiveButton("确定", (dialog2, which2) -> {
                for (ItemEnum item : CommonData.LAUNCHER_ITEMS) {
                    SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_SORT_ + item.getId(), item.getId());
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_LAUNCHER_ITEM_OPEN_ + item.getId(), true);
                }
                EventBus.getDefault().post(new LItemRefreshEvent());
            }).setMessage("是否确认更改,会导致桌面插件重新加载").create();
            queren.show();
        });

        sv_launcher_item_sort.setOnClickListener(v -> {
            final LauncherItemAdapter adapter = new LauncherItemAdapter(getContext());
            AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("调整插件顺序").setNegativeButton("取消", null).setPositiveButton("确定", (dialog1, which1) -> {
                AlertDialog queren = new AlertDialog.Builder(getContext()).setTitle("警告!").setNegativeButton("取消", null).setPositiveButton("确定", (dialog2, which2) -> {
                    List<ItemModel> items = adapter.getItems();
                    for (ItemModel item : items) {
                        SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_LAUNCHER_ITEM_SORT_ + item.info.getId(), item.index);
                        SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_LAUNCHER_ITEM_OPEN_ + item.info.getId(), item.check);
                    }
                    EventBus.getDefault().post(new LItemRefreshEvent());
                }).setMessage("是否确认更改,会导致桌面插件重新加载").create();
                queren.show();
            }).setAdapter(adapter, null).create();
            dialog.show();

            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = (int) (ViewUtils.getDisplayMetriocs(getContext()).widthPixels * 0.8);//定义宽度
            dialog.getWindow().setAttributes(lp);
        });

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
        sv_plugin_select.setOnClickListener(new SetEnumOnClickListener<MusicControllerEnum>(getContext(), CommonData.MUSIC_CONTROLLER) {
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

        sv_console.setSummary("控制协议：" + ConsoleProtoclEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_CONSOLE_MARK, ConsoleProtoclEnum.SYSTEM.getId())).getName());
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
                EventBus.getDefault().post(new LDockLabelShowChangeEvent(value));
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
        tianqi_city.setSummary(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY));
    }
}
