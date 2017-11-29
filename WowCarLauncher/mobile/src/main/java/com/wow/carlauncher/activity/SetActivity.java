package com.wow.carlauncher.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wow.carAssistant.packet.response.common.GetAppUpdateRes;
import com.wow.carlauncher.common.view.FlikerProgressBar;
import com.wow.carlauncher.webservice.service.CommonService;
import com.wow.frame.repertory.remote.WebServiceManage;
import com.wow.frame.repertory.remote.WebTask;
import com.wow.frame.repertory.remote.callback.SCallBack;
import com.wow.frame.repertory.remote.callback.SProgressCallback;
import com.wow.frame.util.AndroidUtil;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppInfoManage;
import com.wow.carlauncher.common.BaseActivity;
import com.wow.carlauncher.common.BaseDialog;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.console.ConsoleManage;
import com.wow.carlauncher.common.console.impl.NwdConsoleImpl;
import com.wow.carlauncher.common.console.impl.SysConsoleImpl;
import com.wow.frame.util.AppUtil.AppInfo;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.dialog.CityDialog;
import com.wow.carlauncher.dialog.InputDialog;
import com.wow.carlauncher.event.LauncherCityRefreshEvent;
import com.wow.carlauncher.event.LauncherDockLabelShowChangeEvent;
import com.wow.carlauncher.event.LauncherItemBackgroundRefreshEvent;
import com.wow.carlauncher.event.PopupIsFullScreenRefreshEvent;
import com.wow.carlauncher.plugin.LauncherPluginEnum;
import com.wow.carlauncher.plugin.PluginEnum;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.popupWindow.PopupWin;
import com.wow.frame.util.ThreadObj;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.*;
import static com.wow.carlauncher.plugin.PluginEnum.*;

public class SetActivity extends BaseActivity {
    private static final String[] CONSOLES = {"系统", "NWD"};

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
        loadPopupSet();
        loadTimeSet();
        loadHelpSet();
        loadSystemSet();
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
    @ViewInject(R.id.sg_system_set)
    private SetView sg_system_set;

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
    @ViewInject(R.id.ll_system_set)
    private LinearLayout ll_system_set;

    private void loadSetGroup() {
        View.OnClickListener groupClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_app.setVisibility(View.GONE);
                ll_launcher.setVisibility(View.GONE);
                ll_popup.setVisibility(View.GONE);
                ll_time.setVisibility(View.GONE);
                ll_help.setVisibility(View.GONE);
                ll_system_set.setVisibility(View.GONE);
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
                    case R.id.sg_system_set: {
                        ll_system_set.setVisibility(View.VISIBLE);
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
        sg_system_set.setOnClickListener(groupClick);
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
                final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getAppInfos());
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

    @ViewInject(R.id.sv_launcher_item1_bg)
    private SetView sv_launcher_item1_bg;

    @ViewInject(R.id.sv_launcher_item2_bg)
    private SetView sv_launcher_item2_bg;

    @ViewInject(R.id.sv_launcher_item3_bg)
    private SetView sv_launcher_item3_bg;

    @ViewInject(R.id.sv_launcher_dock_bg)
    private SetView sv_launcher_dock_bg;

    private void loadLauncherSet() {
        sv_launcher_dock_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputDialog(SetActivity.this)
                        .setTitle("请输入颜色ARGB值:")
                        .setBtn1("取消", null)
                        .setBtn2("确定", new BaseDialog.OnBtnClickListener() {
                            @Override
                            public boolean onClick(BaseDialog dialog) {
                                String text = ((EditText) dialog.findViewById(R.id.et_input)).getText().toString();
                                SharedPreUtil.saveSharedPreString(SDATA_LAUNCHER_DOCK_BG_COLOR, text);
                                sv_launcher_dock_bg.setSummary(text);
                                EventBus.getDefault().post(new LauncherItemBackgroundRefreshEvent());
                                return true;
                            }
                        }).show();
            }
        });
        sv_launcher_dock_bg.setSummary(SharedPreUtil.getSharedPreString(SDATA_LAUNCHER_DOCK_BG_COLOR));

        sv_launcher_item1_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputDialog(SetActivity.this)
                        .setTitle("请输入颜色ARGB值:")
                        .setBtn1("取消", null)
                        .setBtn2("确定", new BaseDialog.OnBtnClickListener() {
                            @Override
                            public boolean onClick(BaseDialog dialog) {
                                String text = ((EditText) dialog.findViewById(R.id.et_input)).getText().toString();
                                SharedPreUtil.saveSharedPreString(SDATA_LAUNCHER_ITEM1_BG_COLOR, text);
                                sv_launcher_item1_bg.setSummary(text);
                                EventBus.getDefault().post(new LauncherItemBackgroundRefreshEvent());
                                return true;
                            }
                        }).show();
            }
        });
        sv_launcher_item1_bg.setSummary(SharedPreUtil.getSharedPreString(SDATA_LAUNCHER_ITEM1_BG_COLOR));

        sv_launcher_item2_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputDialog(SetActivity.this)
                        .setTitle("请输入颜色ARGB值:")
                        .setBtn1("取消", null)
                        .setBtn2("确定", new BaseDialog.OnBtnClickListener() {
                            @Override
                            public boolean onClick(BaseDialog dialog) {
                                String text = ((EditText) dialog.findViewById(R.id.et_input)).getText().toString();
                                SharedPreUtil.saveSharedPreString(SDATA_LAUNCHER_ITEM2_BG_COLOR, text);
                                sv_launcher_item2_bg.setSummary(text);
                                EventBus.getDefault().post(new LauncherItemBackgroundRefreshEvent());
                                return true;
                            }
                        }).show();
            }
        });
        sv_launcher_item2_bg.setSummary(SharedPreUtil.getSharedPreString(SDATA_LAUNCHER_ITEM2_BG_COLOR));

        sv_launcher_item3_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputDialog(SetActivity.this)
                        .setTitle("请输入颜色ARGB值:")
                        .setBtn1("取消", null)
                        .setBtn2("确定", new BaseDialog.OnBtnClickListener() {
                            @Override
                            public boolean onClick(BaseDialog dialog) {
                                String text = ((EditText) dialog.findViewById(R.id.et_input)).getText().toString();
                                SharedPreUtil.saveSharedPreString(SDATA_LAUNCHER_ITEM3_BG_COLOR, text);
                                sv_launcher_item3_bg.setSummary(text);
                                EventBus.getDefault().post(new LauncherItemBackgroundRefreshEvent());
                                return true;
                            }
                        }).show();
            }
        });
        sv_launcher_item3_bg.setSummary(SharedPreUtil.getSharedPreString(SDATA_LAUNCHER_ITEM3_BG_COLOR));

        PluginEnum p1 = PluginEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, SYSMUSIC.getId()));
        sv_launcher_item1.setSummary("桌面左边框框使用的插件：" + p1.getName());
        sv_launcher_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PluginEnum p = PluginEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, SYSMUSIC.getId()));
                final PluginEnum[] show = getLauncherPluginType(p);
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
                        sv_launcher_item1.setSummary("桌面左边框框使用的插件：" + show[obj.getObj()].getName());
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

        PluginEnum p2 = PluginEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginEnum.AMAP.getId()));
        sv_launcher_item2.setSummary("桌面中间框框使用的插件：" + p2.getName());
        sv_launcher_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PluginEnum p = PluginEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginEnum.AMAP.getId()));
                final PluginEnum[] show = getLauncherPluginType(p);
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
                        sv_launcher_item2.setSummary("桌面中间框框使用的插件：" + show[obj.getObj()].getName());
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

        PluginEnum p3 = PluginEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginEnum.CONSOLE.getId()));
        sv_launcher_item3.setSummary("桌面右边框框使用的插件：" + p3.getName());
        sv_launcher_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PluginEnum p = PluginEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginEnum.CONSOLE.getId()));
                final PluginEnum[] show = getLauncherPluginType(p);
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
                        sv_launcher_item3.setSummary("桌面右边框框使用的插件：" + show[obj.getObj()].getName());
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

    private PluginEnum[] getLauncherPluginType(PluginEnum contain) {
        List<PluginEnum> ps = new ArrayList<>();
        PluginEnum p1 = PluginEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM1_PLUGIN, SYSMUSIC.getId()));
        PluginEnum p2 = PluginEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM2_PLUGIN, PluginEnum.AMAP.getId()));
        PluginEnum p3 = PluginEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_ITEM3_PLUGIN, PluginEnum.CONSOLE.getId()));

        for (PluginEnum p : ALL_PLUGINS) {
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
        return ps.toArray(new PluginEnum[ps.size()]);
    }

    @ViewInject(R.id.sv_allow_popup_window)
    private SetView sv_allow_popup_window;

    @ViewInject(R.id.sv_popup_showapps_sysmusic)
    private SetView sv_popup_showapps_sysmusic;

    @ViewInject(R.id.sv_popup_showapps_ncmusic)
    private SetView sv_popup_showapps_ncmusic;

    @ViewInject(R.id.sv_popup_showapps_qqmusic)
    private SetView sv_popup_showapps_qqmusic;

    @ViewInject(R.id.sv_popup_showapps_qqmusiccar)
    private SetView sv_popup_showapps_qqmusiccar;

    @ViewInject(R.id.sv_popup_showapps_amap)
    private SetView sv_popup_showapps_amap;

    @ViewInject(R.id.sv_popup_window_showapps)
    private SetView sv_popup_window_showapps;

    @ViewInject(R.id.sv_popup_window_showtype)
    private SetView sv_popup_window_showtype;

    @ViewInject(R.id.sv_popup_showapps_jidoumusic)
    private SetView sv_popup_showapps_jidoumusic;

    @ViewInject(R.id.sv_popup_full_screen)
    private SetView sv_popup_full_screen;

    @ViewInject(R.id.sv_popup_window_size)
    private SetView sv_popup_window_size;

    @ViewInject(R.id.sv_popup_showapps_pamusic)
    private SetView sv_popup_showapps_pamusic;

    @ViewInject(R.id.sv_popup_showapps_nwdmusic)
    private SetView sv_popup_showapps_nwdmusic;


    private void loadPopupSet() {
        sv_popup_window_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择APP").setItems(POPUP_SIZE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_POPUP_SIZE, i);
                        PopupWin.self().setRank(i + 1);
                    }
                }).create();
                dialog.show();
            }
        });
        sv_popup_window_size.setSummary(POPUP_SIZE[SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_SIZE, 1)]);

        sv_popup_full_screen.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                Log.e(TAG, "onValueChange: " + newValue);
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, true);
                } else {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, false);
                }
                EventBus.getDefault().post(new PopupIsFullScreenRefreshEvent());
            }
        });
        sv_popup_full_screen.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, true));

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

        sv_popup_showapps_sysmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading("载入中", null);
                setPopupPluginShowApp(SYSMUSIC, sv_popup_showapps_sysmusic);
            }
        });
        int p1 = getPopupPluginShowAppCount(SYSMUSIC);
        sv_popup_showapps_sysmusic.setSummary(p1 == 0 ? "不使用" : p1 + "个APP使用");

        sv_popup_showapps_ncmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading("载入中", null);
                setPopupPluginShowApp(NCMUSIC, sv_popup_showapps_ncmusic);
            }
        });
        int p2 = getPopupPluginShowAppCount(NCMUSIC);
        sv_popup_showapps_ncmusic.setSummary(p2 == 0 ? "不使用" : p2 + "个APP使用");


        sv_popup_showapps_qqmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading("载入中", null);
                setPopupPluginShowApp(QQMUSIC, sv_popup_showapps_qqmusic);
            }
        });
        int p3 = getPopupPluginShowAppCount(QQMUSIC);
        sv_popup_showapps_qqmusic.setSummary(p3 == 0 ? "不使用" : p3 + "个APP使用");


        sv_popup_showapps_qqmusiccar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading("载入中", null);
                setPopupPluginShowApp(QQCARMUSIC, sv_popup_showapps_qqmusiccar);
            }
        });
        int p4 = getPopupPluginShowAppCount(QQCARMUSIC);
        sv_popup_showapps_qqmusiccar.setSummary(p4 == 0 ? "不使用" : p4 + "个APP使用");


        sv_popup_showapps_amap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading("载入中", null);
                setPopupPluginShowApp(AMAP, sv_popup_showapps_amap);
            }
        });
        int p5 = getPopupPluginShowAppCount(AMAP);
        sv_popup_showapps_amap.setSummary(p5 == 0 ? "不使用" : p5 + "个APP使用");

        sv_popup_showapps_jidoumusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading("载入中", null);
                setPopupPluginShowApp(JIDOUMUSIC, sv_popup_showapps_jidoumusic);
            }
        });
        int p6 = getPopupPluginShowAppCount(JIDOUMUSIC);
        sv_popup_showapps_jidoumusic.setSummary(p6 == 0 ? "不使用" : p6 + "个APP使用");

        sv_popup_showapps_pamusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading("载入中", null);
                setPopupPluginShowApp(POWERAMPMUSIC, sv_popup_showapps_pamusic);
            }
        });
        int p7 = getPopupPluginShowAppCount(POWERAMPMUSIC);
        sv_popup_showapps_pamusic.setSummary(p7 == 0 ? "不使用" : p7 + "个APP使用");

        sv_popup_showapps_nwdmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading("载入中", null);
                setPopupPluginShowApp(NWDMUSIC, sv_popup_showapps_nwdmusic);
            }
        });
        int p8 = getPopupPluginShowAppCount(NWDMUSIC);
        sv_popup_showapps_nwdmusic.setSummary(p8 == 0 ? "不使用" : p7 + "个APP使用");

        sv_popup_window_showapps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_POPUP_SHOW_APPS);
                final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getAppInfos());
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

    }

    private int getPopupPluginShowAppCount(final PluginEnum pluginTypeEnum) {
        String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_POPUP_PLUGIN_SHOW_APPS + pluginTypeEnum.getId());
        if (CommonUtil.isNull(selectapp)) {
            return 0;
        } else {
            return selectapp.split(";").length;
        }
    }

    private void setPopupPluginShowApp(final PluginEnum popupPluginEnum, final SetView setView) {
        x.task().run(new Runnable() {
            @Override
            public void run() {
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_POPUP_PLUGIN_SHOW_APPS + popupPluginEnum.getId());
                final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getAppInfos());
                String[] items = new String[appInfos.size()];
                final boolean[] checks = new boolean[appInfos.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = appInfos.get(i).name + "(" + appInfos.get(i).packageName + ")";
                    checks[i] = selectapp.contains("[" + appInfos.get(i).packageName + "]");
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectapp = "";
                        List<String> apps = new ArrayList<>();
                        for (int i = 0; i < appInfos.size(); i++) {
                            if (checks[i]) {
                                selectapp = selectapp + "[" + appInfos.get(i).packageName + "];";
                                apps.add(appInfos.get(i).packageName);
                            }
                        }
                        if (selectapp.endsWith(";")) {
                            selectapp = selectapp.substring(0, selectapp.length() - 1);
                        }
                        setView.setSummary(apps.size() == 0 ? "不使用" : apps.size() + "个APP使用");
                        PluginManage.self().setPopupPluginShowApps(popupPluginEnum, apps);
                        SharedPreUtil.saveSharedPreString(CommonData.SDATA_POPUP_PLUGIN_SHOW_APPS + popupPluginEnum.getId(), selectapp);
                    }
                }).setMultiChoiceItems(items, checks, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        Log.e(TAG, "onClick: " + appInfos.get(which).name);
                        checks[which] = isChecked;
                    }
                });

                x.task().autoPost(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        builder.create().show();
                    }
                });
            }
        });
    }

    @ViewInject(R.id.time_plugin_open_app_select)
    private SetView time_plugin_open_app_select;


    @ViewInject(R.id.tianqi_city)
    private SetView tianqi_city;

    private void loadTimeSet() {
        time_plugin_open_app_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_TIME_PLUGIN_OPEN_APP);
                final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getAppInfos());
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

        tianqi_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CityDialog cityDialog = new CityDialog(mContext);
                cityDialog.setOkListener(new BaseDialog.OnBtnClickListener() {
                    @Override
                    public boolean onClick(BaseDialog dialog) {
                        Log.e(TAG, "onClick: " + cityDialog.getmCurrentDistrictName());
                        if (CommonUtil.isNotNull(cityDialog.getmCurrentDistrictName())) {
                            SharedPreUtil.saveSharedPreString(CommonData.SDATA_WEATHER_CITY, cityDialog.getmCurrentDistrictName());
                            dialog.dismiss();
                            EventBus.getDefault().post(new LauncherCityRefreshEvent());
                            tianqi_city.setSummary(cityDialog.getmCurrentDistrictName());
                        } else {
                            showTip("请选择城市");
                        }
                        return false;
                    }
                });
                cityDialog.show();
            }
        });
        tianqi_city.setSummary(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY));
    }

    @ViewInject(R.id.sv_about)
    private SetView sv_about;

    @ViewInject(R.id.sv_money)
    private SetView sv_money;

    @ViewInject(R.id.sv_get_newest)
    private SetView sv_get_newest;

    private WebTask<File> downloadUpdataTask;

    private ProgressInterruptListener downloadUpdataTaskCancel = new ProgressInterruptListener() {
        @Override
        public void onProgressInterruptListener(ProgressDialog progressDialog) {
            if (downloadUpdataTask != null) {
                downloadUpdataTask.cancelTask();
            }
        }
    };

    private void loadHelpSet() {
        sv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AboutActivity.class));
            }
        });

        sv_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = new ImageView(mContext);
                imageView.setImageResource(R.mipmap.money);
                AlertDialog dialog = new AlertDialog.Builder(mContext).setView(imageView).setTitle("支持我吧!").setPositiveButton("确定", null).create();
                dialog.show();
            }
        });

        sv_get_newest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebServiceManage.getService(CommonService.class).checkUpdate().setCallback(new SCallBack<GetAppUpdateRes>() {
                    @Override
                    public void callback(boolean isok, String msg, final GetAppUpdateRes res) {
                        if (isok) {
                            final String version = AndroidUtil.getAppVersionCode(mContext);
                            if (TextUtils.isEmpty(version)) {
                                showTip("获取当前版本号失败");
                                return;
                            }
                            if (res.getVersionCode() == null) {
                                showTip("没有新版本发布");
                                return;
                            }
                            if (res.getVersionCode() > Integer.parseInt(version)) {
                                BaseDialog baseDialog = new BaseDialog(mContext);
                                baseDialog.setGravityCenter();
                                baseDialog.setTitle("版本更新");
                                baseDialog.setMessage("最新版本为" + res.getVersionName() + ",是否更新?\n" + res.getNewMessage());
                                baseDialog.setBtn1("下载", new BaseDialog.OnBtnClickListener() {
                                    @Override
                                    public boolean onClick(BaseDialog dialog) {
                                        dialog.dismiss();
                                        final String savePath = Environment.getExternalStorageDirectory() + "/" + res.getVersionName() + ".apk";
                                        downloadUpdataTask = WebServiceManage.getService(CommonService.class).getAppUpdateFile(savePath);
                                        showLoading("开始下载!", downloadUpdataTaskCancel);
                                        downloadUpdataTask.setCallback(new SProgressCallback<File>() {
                                            @Override
                                            public void onProgress(float progress) {
                                                if (progress > 0 && progress < 1)
                                                    downloadResult(progress, savePath, null);
                                            }

                                            @Override
                                            public void callback(boolean isok, String msg, File res) {
                                                if (isok) {
                                                    downloadResult(1f, savePath, savePath);
                                                } else {
                                                    downloadResult(-1f, savePath, msg);
                                                }
                                            }
                                        });
                                        return true;
                                    }
                                });
                                baseDialog.setBtn2("取消", null);
                                baseDialog.show();
                            } else {
                                showTip("已是最新版本");
                            }

                        } else {
                            showTip("错误:" + msg);
                        }
                    }
                });
            }
        });
    }

    public void downloadResult(float progress, String path, String msg) {
        if (progress == -1) {
            showTip(msg);
            return;
        }
        if (progress >= 0 && progress < 1) {
            showLoading("已经下载:" + (int) (progress * 100) + "%", downloadUpdataTaskCancel);
        }
        if (progress == 1) {
            showTip("下载成功");
            downloadUpdataTask = null;
            hideLoading();
            Intent intent = new Intent();
            //执行动作
            intent.setAction(Intent.ACTION_VIEW);
            //执行的数据类型
            Uri data = Uri.fromFile(new File(path + ".tmp"));
            intent.setDataAndType(data, "application/vnd.android.package-archive");
            startActivity(intent);
        }
    }

    @ViewInject(R.id.sv_sys_anquan)
    private SetView sv_sys_anquan;

    @ViewInject(R.id.sv_sys_overlay)
    private SetView sv_sys_overlay;

    @ViewInject(R.id.sv_sys_sdk)
    private SetView sv_sys_sdk;

    private void loadSystemSet() {
        sv_sys_overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getAppInfos());
                    String[] items = new String[appInfos.size()];
                    for (int i = 0; i < items.length; i++) {
                        items[i] = appInfos.get(i).name + "(" + appInfos.get(i).packageName + ")";
                    }

                    AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + appInfos.get(which).packageName));
                            startActivity(intent);
                        }
                    }).create();
                    dialog.show();
                } else {
                    showTip("这个功能是安卓6.0以上才有的");
                }
            }
        });

        sv_sys_anquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                startActivity(intent);
            }
        });

        sv_sys_sdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTip("当前SDK版本是" + Build.VERSION.SDK_INT);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadUpdataTask != null)
            downloadUpdataTask.cancelTask();
    }
}
