package com.wow.carlauncher.view.activity.set.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.appInfo.AppInfo;
import com.wow.carlauncher.ex.manage.appInfo.AppInfoManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.console.ConsolePlugin;
import com.wow.carlauncher.ex.plugin.console.ConsoleProtoclEnum;
import com.wow.carlauncher.ex.plugin.music.MusicControllerEnum;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.view.activity.launcher.event.LauncherDockLabelShowChangeEvent;
import com.wow.carlauncher.view.base.BaseView;
import com.wow.frame.util.SharedPreUtil;
import com.wow.frame.util.ThreadObj;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.SDATA_CONSOLE_MARK;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_CONTROLLER;
import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SAppView extends BaseView {
    public static final MusicControllerEnum[] ALL_MUSIC_CONTROLLER = {MusicControllerEnum.SYSMUSIC,
            MusicControllerEnum.QQCARMUSIC,
            MusicControllerEnum.JIDOUMUSIC,
            MusicControllerEnum.NWDMUSIC};

    public static final ConsoleProtoclEnum[] ALL_CONSOLES = {ConsoleProtoclEnum.SYSTEM, ConsoleProtoclEnum.NWD};

    public SAppView(@NonNull Context context) {
        super(context);
        initView();
    }

    public SAppView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
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


    private void initView() {
        addContent(R.layout.content_set_app);

        MusicControllerEnum p1 = MusicControllerEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_CONTROLLER, MusicControllerEnum.SYSMUSIC.getId()));
        sv_plugin_select.setSummary(p1.getName());
        sv_plugin_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicControllerEnum p = MusicControllerEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_CONTROLLER, MusicControllerEnum.SYSMUSIC.getId()));
                final MusicControllerEnum[] show = ALL_MUSIC_CONTROLLER;
                String[] items = new String[show.length];
                int select = 0;
                for (int i = 0; i < show.length; i++) {
                    items[i] = show[i].getName();
                    if (show[i].equals(p)) {
                        select = i;
                    }
                }
                final ThreadObj<Integer> obj = new ThreadObj<>(select);
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("请选择插件").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_CONTROLLER, show[obj.getObj()].getId());
                        MusicPlugin.self().setController(show[obj.getObj()]);
                        sv_plugin_select.setSummary(show[obj.getObj()].getName());
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

        sv_wall_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastManage.self().show("请使用第三方看图软件直接将图片设置为安卓壁纸即可");
            }
        });
        sv_apps_hides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_HIDE_APPS);
                final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getOtherAppInfos());
                String[] items = new String[appInfos.size()];
                final boolean[] checks = new boolean[appInfos.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = appInfos.get(i).name + "(" + appInfos.get(i).clazz + ")";
                    checks[i] = selectapp.contains("[" + appInfos.get(i).clazz + "]");
                }

                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectapp = "";
                        for (int i = 0; i < appInfos.size(); i++) {
                            if (checks[i]) {
                                selectapp = selectapp + "[" + appInfos.get(i).clazz + "];";
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

        ConsoleProtoclEnum c1 = ConsoleProtoclEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_CONSOLE_MARK, ConsoleProtoclEnum.SYSTEM.getId()));
        sv_console.setSummary("控制协议：" + c1.getName());
        sv_console.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConsoleProtoclEnum p = ConsoleProtoclEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_CONSOLE_MARK, ConsoleProtoclEnum.SYSTEM.getId()));
                final ConsoleProtoclEnum[] show = ALL_CONSOLES;
                String[] items = new String[show.length];
                int select = 0;
                for (int i = 0; i < show.length; i++) {
                    items[i] = show[i].getName();
                    if (show[i].equals(p)) {
                        select = i;
                    }
                }
                final ThreadObj<Integer> obj = new ThreadObj<>(select);
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("请选择控制器").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreUtil.saveSharedPreInteger(SDATA_CONSOLE_MARK, show[obj.getObj()].getId());
                        sv_plugin_select.setSummary("控制协议：" + show[obj.getObj()].getName());
                        ConsolePlugin.self().loadConsole();
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

    private Activity getActivity() {
        Context context = getContext();
        if (context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }
}
