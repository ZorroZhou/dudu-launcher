package com.wow.carlauncher.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseActivity;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.console.ConsoleManage;
import com.wow.carlauncher.common.console.impl.NwdConsoleImpl;
import com.wow.carlauncher.common.console.impl.SysConsoleImpl;
import com.wow.carlauncher.common.util.AppUtil;
import com.wow.carlauncher.common.util.AppUtil.AppInfo;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ThreadObj;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.event.LauncherDockLabelShowChangeEvent;
import com.wow.carlauncher.plugin.LauncherPluginEnum;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.plugin.PluginTypeEnum;
import com.wow.carlauncher.plugin.music.MusicControllerEnum;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.*;
import static com.wow.carlauncher.plugin.music.MusicControllerEnum.*;

/**
 * Created by 10124 on 2017/10/26.
 */

public class SetActivity extends BaseActivity {
    private static final String TAG = "SetActivity";
    private static final String[] CONSOLES = {"系统", "NWD"};
    private static final PluginTypeEnum[] ALL_PLUGINS = {PluginTypeEnum.MUSIC, PluginTypeEnum.NCMUSIC, PluginTypeEnum.CONSOLE, PluginTypeEnum.AMAP};

    @ViewInject(R.id.sv_popup_window_showtype)
    private SetView sv_popup_window_showtype;

    @ViewInject(R.id.sv_popup_window_showapps)
    private SetView sv_popup_window_showapps;

    @ViewInject(R.id.sv_allow_popup_window)
    private SetView sv_allow_popup_window;

    @ViewInject(R.id.time_plugin_open_app_select)
    private SetView time_plugin_open_app_select;

    @ViewInject(R.id.sv_about)
    private SetView sv_about;

    @Override
    public void init() {
        setContent(R.layout.activity_set);
    }

    @Override
    public void initView() {
        setTitle("设置");
        loadSetGroup();
        loadAppSet();
        loadLauncherSet();
        sv_allow_popup_window.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_ALLOW_SHOW, true);
                } else {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_ALLOW_SHOW, false);
                }
            }
        });
        sv_allow_popup_window.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_ALLOW_SHOW, true));

        sv_popup_window_showtype.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_SHOW_TYPE, true);
                } else {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_SHOW_TYPE, false);
                }
            }
        });
        sv_popup_window_showtype.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_SHOW_TYPE, true));

        sv_popup_window_showapps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_POPUP_SHOW_APPS);
                final List<AppInfo> appInfos = AppUtil.getAllApp(mContext);
                String[] items = new String[appInfos.size()];
                final boolean[] checks = new boolean[appInfos.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = appInfos.get(i).name + "(" + appInfos.get(i).packageName + ")";
                    checks[i] = selectapp.contains("[" + appInfos.get(i).packageName + "]");
                }

                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectapp = "";
                        for (int i = 0; i < appInfos.size(); i++) {
                            if (checks[i]) {
                                selectapp = selectapp + "[" + appInfos.get(i).packageName + "];";
                            }
                        }
                        if (selectapp.endsWith(";")) {
                            selectapp = selectapp.substring(0, selectapp.length() - 1);
                        }
                        SharedPreUtil.saveSharedPreString(CommonData.SDATA_POPUP_SHOW_APPS, selectapp);
                    }
                }).setMultiChoiceItems(items, checks, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        Log.e(TAG, "onClick: " + appInfos.get(which).name);
                        checks[which] = isChecked;
                    }
                }).create();
                dialog.show();
            }
        });

        time_plugin_open_app_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_TIME_PLUGIN_OPEN_APP);
                final List<AppInfo> appInfos = AppUtil.getAllApp(mContext);
                String[] items = new String[appInfos.size()];
                int select = -1;
                for (int i = 0; i < items.length; i++) {
                    items[i] = appInfos.get(i).name + "(" + appInfos.get(i).packageName + ")";
                    if (appInfos.get(i).packageName.equals(selectapp)) {
                        select = i;
                    }
                }
                Log.e(TAG, "onClick: " + items.length + " " + select);
                final ThreadObj<Integer> obj = new ThreadObj<>(select);
                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreUtil.saveSharedPreString(CommonData.SDATA_TIME_PLUGIN_OPEN_APP, appInfos.get(obj.getObj()).packageName);
                    }
                }).setSingleChoiceItems(items, select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        obj.setObj(which);
                    }
                }).create();
                dialog.show();
            }
        });

        sv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AboutActivity.class));
            }
        });
    }

    @ViewInject(R.id.sg_app)
    private SetView sg_app;
    @ViewInject(R.id.sg_launcher)
    private SetView sg_launcher;
    @ViewInject(R.id.sg_popup)
    private SetView sg_popup;
    @ViewInject(R.id.sg_time)
    private SetView sg_time;
    @ViewInject(R.id.sg_help)
    private SetView sg_help;

    @ViewInject(R.id.ll_app)
    private LinearLayout ll_app;
    @ViewInject(R.id.ll_launcher)
    private LinearLayout ll_launcher;
    @ViewInject(R.id.ll_popup)
    private LinearLayout ll_popup;
    @ViewInject(R.id.ll_time)
    private LinearLayout ll_time;
    @ViewInject(R.id.ll_help)
    private LinearLayout ll_help;

    private void loadSetGroup() {
        View.OnClickListener groupClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_app.setVisibility(View.GONE);
                ll_launcher.setVisibility(View.GONE);
                ll_popup.setVisibility(View.GONE);
                ll_time.setVisibility(View.GONE);
                ll_help.setVisibility(View.GONE);
                switch (view.getId()) {
                    case R.id.sg_app: {
                        ll_app.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_launcher: {
                        ll_launcher.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_popup: {
                        ll_popup.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_time: {
                        ll_time.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_help: {
                        ll_help.setVisibility(View.VISIBLE);
                        break;
                    }
                    default: {
                        ll_app.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        sg_app.setOnClickListener(groupClick);
        sg_launcher.setOnClickListener(groupClick);
        sg_popup.setOnClickListener(groupClick);
        sg_time.setOnClickListener(groupClick);
        sg_help.setOnClickListener(groupClick);
    }

    @ViewInject(R.id.sv_plugin_set)
    private SetView sv_plugin_set;

    @ViewInject(R.id.sv_wall_set)
    private SetView sv_wall_set;

    @ViewInject(R.id.sv_apps_hides)
    private SetView sv_apps_hides;

    @ViewInject(R.id.sv_console)
    private SetView sv_console;

    @ViewInject(R.id.sv_launcher_show_dock_label)
    private SetView sv_launcher_show_dock_label;

    private void loadAppSet() {
        sv_plugin_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, PluginSetActivity.class));
            }
        });
        sv_wall_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTip("请使用第三方看图软件直接将图片设置为安卓壁纸即可");
            }
        });
        sv_apps_hides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_HIDE_APPS);
                final List<AppInfo> appInfos = AppUtil.getAllApp(mContext);
                String[] items = new String[appInfos.size()];
                final boolean[] checks = new boolean[appInfos.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = appInfos.get(i).name + "(" + appInfos.get(i).packageName + ")";
                    checks[i] = selectapp.contains("[" + appInfos.get(i).packageName + "]");
                }

                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectapp = "";
                        for (int i = 0; i < appInfos.size(); i++) {
                            if (checks[i]) {
                                selectapp = selectapp + "[" + appInfos.get(i).packageName + "];";
                            }
                        }
                        if (selectapp.endsWith(";")) {
                            selectapp = selectapp.substring(0, selectapp.length() - 1);
                        }
                        SharedPreUtil.saveSharedPreString(CommonData.SDATA_HIDE_APPS, selectapp);
                    }
                }).setMultiChoiceItems(items, checks, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        Log.e(TAG, "onClick: " + appInfos.get(which).name);
                        checks[which] = isChecked;
                    }
                }).create();
                dialog.show();
            }
        });

        sv_console.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int select = SharedPreUtil.getSharedPreInteger(SDATA_CONSOLE_MARK, SysConsoleImpl.MARK);
                final ThreadObj<Integer> obj = new ThreadObj<>(select);
                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择控制器").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (obj.getObj()) {
                            case SysConsoleImpl.MARK: {
                                ConsoleManage.self().setConsole(new SysConsoleImpl(mContext));
                                break;
                            }
                            case NwdConsoleImpl.MARK: {
                                ConsoleManage.self().setConsole(new NwdConsoleImpl(mContext));
                                break;
                            }
                        }
                    }
                }).setSingleChoiceItems(CONSOLES, select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        obj.setObj(which);
                    }
                }).create();
                dialog.show();
            }
        });
        sv_console.setSummary(CONSOLES[SharedPreUtil.getSharedPreInteger(SDATA_CONSOLE_MARK, SysConsoleImpl.MARK)]);

        sv_launcher_show_dock_label.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW, true);
                    EventBus.getDefault().post(new LauncherDockLabelShowChangeEvent(true));
                } else {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW, false);
                    EventBus.getDefault().post(new LauncherDockLabelShowChangeEvent(false));
                }
            }
        });
        sv_launcher_show_dock_label.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW, true));
    }

    @ViewInject(R.id.sv_launcher_item1)
    private SetView sv_launcher_item1;

    @ViewInject(R.id.sv_launcher_item2)
    private SetView sv_launcher_item2;

    @ViewInject(R.id.sv_launcher_item3)
    private SetView sv_launcher_item3;

    private void loadLauncherSet() {
        PluginTypeEnum p1 = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginTypeEnum.MUSIC.getId()));
        sv_launcher_item1.setSummary("桌面左边框框使用的插件：" + p1.getName());
        sv_launcher_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PluginTypeEnum p = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginTypeEnum.MUSIC.getId()));
                final PluginTypeEnum[] show = getLauncherPluginType(p);
                String[] items = new String[show.length];
                int select = 0;
                for (int i = 0; i < show.length; i++) {
                    items[i] = show[i].getName();
                    if (show[i].equals(p)) {
                        select = i;
                    }
                }
                final ThreadObj<Integer> obj = new ThreadObj<>(select);
                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择插件").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PluginManage.self().setLauncherPlugin(LauncherPluginEnum.LAUNCHER_ITEM1, show[obj.getObj()]);
                        sv_launcher_item1.setSummary("桌面中间框框使用的插件：" + show[obj.getObj()].getName());
                    }
                }).setSingleChoiceItems(items, select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        obj.setObj(which);
                    }
                }).create();
                dialog.show();
            }
        });

        PluginTypeEnum p2 = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginTypeEnum.AMAP.getId()));
        sv_launcher_item2.setSummary("桌面中间框框使用的插件：" + p2.getName());
        sv_launcher_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PluginTypeEnum p = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginTypeEnum.AMAP.getId()));
                final PluginTypeEnum[] show = getLauncherPluginType(p);
                String[] items = new String[show.length];
                int select = 0;
                for (int i = 0; i < show.length; i++) {
                    items[i] = show[i].getName();
                    if (show[i].equals(p)) {
                        select = i;
                    }
                }
                final ThreadObj<Integer> obj = new ThreadObj<>(select);
                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择插件").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PluginManage.self().setLauncherPlugin(LauncherPluginEnum.LAUNCHER_ITEM2, show[obj.getObj()]);
                    }
                }).setSingleChoiceItems(items, select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        obj.setObj(which);
                    }
                }).create();
                dialog.show();
            }
        });

        PluginTypeEnum p3 = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginTypeEnum.CONSOLE.getId()));
        sv_launcher_item3.setSummary("桌面右边框框使用的插件：" + p3.getName());
        sv_launcher_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PluginTypeEnum p = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginTypeEnum.CONSOLE.getId()));
                final PluginTypeEnum[] show = getLauncherPluginType(p);
                String[] items = new String[show.length];
                int select = 0;
                for (int i = 0; i < show.length; i++) {
                    items[i] = show[i].getName();
                    if (show[i].equals(p)) {
                        select = i;
                    }
                }
                final ThreadObj<Integer> obj = new ThreadObj<>(select);
                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择插件").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PluginManage.self().setLauncherPlugin(LauncherPluginEnum.LAUNCHER_ITEM3, show[obj.getObj()]);
                    }
                }).setSingleChoiceItems(items, select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        obj.setObj(which);
                    }
                }).create();
                dialog.show();
            }
        });
    }

    private PluginTypeEnum[] getLauncherPluginType(PluginTypeEnum contain) {
        List<PluginTypeEnum> ps = new ArrayList<>();
        PluginTypeEnum p1 = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, PluginTypeEnum.MUSIC.getId()));
        PluginTypeEnum p2 = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginTypeEnum.AMAP.getId()));
        PluginTypeEnum p3 = PluginTypeEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginTypeEnum.CONSOLE.getId()));

        for (PluginTypeEnum p : ALL_PLUGINS) {
            if (p.equals(p1) && !p.equals(contain)) {
                continue;
            }
            if (p.equals(p2) && !p.equals(contain)) {
                continue;
            }
            if (p.equals(p3) && !p.equals(contain)) {
                continue;
            }
            ps.add(p);
        }
        return ps.toArray(new PluginTypeEnum[ps.size()]);
    }
}
