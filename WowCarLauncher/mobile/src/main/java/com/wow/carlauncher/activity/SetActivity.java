package com.wow.carlauncher.activity;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseActivity;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.CommonUtil.AppInfo;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ThreadObj;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.plugin.music.MusicControllerEnum;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import static com.wow.carlauncher.common.CommonData.*;
import static com.wow.carlauncher.plugin.music.MusicControllerEnum.*;

/**
 * Created by 10124 on 2017/10/26.
 */

public class SetActivity extends BaseActivity {
    private static final String TAG = "SetActivity";
    private final static String[] MUSIC_CONTORLLERS = {SYSTEM.getName(), NETEASECLOUD.getName(), QQMUSICCAR.getName(), QQMUSIC.getName()};

    @ViewInject(R.id.sv_popup_window_showtype)
    private SetView sv_popup_window_showtype;

    @ViewInject(R.id.sv_popup_window_showapps)
    private SetView sv_popup_window_showapps;

    @ViewInject(R.id.sv_allow_popup_window)
    private SetView sv_allow_popup_window;

    @ViewInject(R.id.music_controller_select)
    private SetView music_controller_select;

    @ViewInject(R.id.sv_apps_hides)
    private SetView sv_apps_hides;

    @ViewInject(R.id.time_plugin_open_app_select)
    private SetView time_plugin_open_app_select;

    private AppWidgetHost appWidgetHost;

    private TextView tv_select_widget1, tv_select_widget2;

    @Override
    public void init() {
        setContent(R.layout.activity_set);
    }

    @Override
    public void initView() {
        setTitle("设置");
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
                final List<AppInfo> appInfos = CommonUtil.getAllApp(mContext);
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

        music_controller_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer select = 0;
                int selectId = SharedPreUtil.getSharedPreInteger(CommonData.SDATA_CURRENT_MUSIC_CONTROLLER, SYSTEM.getId());
                for (int i = 0; i < MUSIC_CONTORLLERS.length; i++) {
                    if (MusicControllerEnum.valueOfName(MUSIC_CONTORLLERS[i]).getId() == selectId) {
                        select = i;
                    }
                }
                final ThreadObj<Integer> obj = new ThreadObj<>(select);
                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择音乐控制器").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final MusicControllerEnum controllerEnum = MusicControllerEnum.valueOfName(MUSIC_CONTORLLERS[obj.getObj()]);
                        if (controllerEnum.equals(MusicControllerEnum.NETEASECLOUD)) {
                            View content = View.inflate(mContext, R.layout.dialog_netease_cloud_music_widget_select, null);
                            tv_select_widget1 = content.findViewById(R.id.widget1);
                            tv_select_widget2 = content.findViewById(R.id.widget2);

                            tv_select_widget1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectWidgetRequest(REQUEST_SELECT_NCM_WIDGET1);
                                }
                            });
                            tv_select_widget2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectWidgetRequest(REQUEST_SELECT_NCM_WIDGET2);
                                }
                            });

                            int id1 = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_POPUP, -1);
                            if (id1 != -1) {
                                String msg = "1*4小组件，选择ID：" + id1;
                                tv_select_widget1.setText(msg);
                            }

                            int id2 = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_LANNCHER, -1);
                            if (id2 != -1) {
                                String msg = "2*4小组件，选择ID：" + id1;
                                tv_select_widget2.setText(msg);
                            }


                            AlertDialog wangyixuanze = new AlertDialog.Builder(mContext).setTitle("请选择网易云音乐控件").setView(content)
                                    .setNegativeButton("取消", null)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int id1 = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_POPUP, -1);
                                            int id2 = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_LANNCHER, -1);
                                            if (id1 != -1 && id2 != -1) {
                                                music_controller_select.setSummary(MUSIC_CONTORLLERS[obj.getObj()]);
                                                SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_CURRENT_MUSIC_CONTROLLER, controllerEnum.getId());
                                                PluginManage.music().selectMusicController(controllerEnum);
                                            } else {
                                                showTip("没有选择组件");
                                            }
                                        }
                                    }).create();
                            wangyixuanze.show();
                        } else if (controllerEnum.equals(MusicControllerEnum.QQMUSIC)) {
                            View content = View.inflate(mContext, R.layout.dialog_qq_music_widget_select, null);
                            tv_select_widget1 = content.findViewById(R.id.widget1);
                            tv_select_widget2 = content.findViewById(R.id.widget2);

                            tv_select_widget1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectWidgetRequest(REQUEST_SELECT_QQMUSIC_WIDGET1);
                                }
                            });
                            tv_select_widget2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectWidgetRequest(REQUEST_SELECT_QQMUSIC_WIDGET2);
                                }
                            });

                            int id1 = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_POPUP, -1);
                            if (id1 != -1) {
                                String msg = "1*4小组件，选择ID：" + id1;
                                tv_select_widget1.setText(msg);
                            }

                            int id2 = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_LANNCHER, -1);
                            if (id2 != -1) {
                                String msg = "2*4小组件，选择ID：" + id1;
                                tv_select_widget2.setText(msg);
                            }


                            AlertDialog wangyixuanze = new AlertDialog.Builder(mContext).setTitle("请选择QQ音乐控件").setView(content)
                                    .setNegativeButton("取消", null)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int id1 = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_POPUP, -1);
                                            int id2 = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_LANNCHER, -1);
                                            if (id1 != -1 && id2 != -1) {
                                                music_controller_select.setSummary(MUSIC_CONTORLLERS[obj.getObj()]);
                                                SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_CURRENT_MUSIC_CONTROLLER, controllerEnum.getId());
                                                PluginManage.music().selectMusicController(controllerEnum);
                                            } else {
                                                showTip("没有选择组件");
                                            }
                                        }
                                    }).create();
                            wangyixuanze.show();
                        } else {
                            music_controller_select.setSummary(MUSIC_CONTORLLERS[obj.getObj()]);
                            SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_CURRENT_MUSIC_CONTROLLER, controllerEnum.getId());
                            PluginManage.music().selectMusicController(controllerEnum);
                        }
                    }
                }).setSingleChoiceItems(MUSIC_CONTORLLERS, select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        obj.setObj(which);
                    }
                }).create();
                dialog.show();
            }
        });

        music_controller_select.setSummary(MusicControllerEnum.valueOfId(SharedPreUtil.getSharedPreInteger(CommonData.SDATA_CURRENT_MUSIC_CONTROLLER, SYSTEM.getId())).getName());


        sv_apps_hides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_HIDE_APPS);
                final List<AppInfo> appInfos = CommonUtil.getAllApp(mContext);
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

        time_plugin_open_app_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_TIME_PLUGIN_OPEN_APP);
                final List<AppInfo> appInfos = CommonUtil.getAllApp(mContext);
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


        appWidgetHost = new AppWidgetHost(getApplication(), APP_WIDGET_HOST_ID);
    }

    private void selectWidgetRequest(int request) {
        int widgetId = appWidgetHost.allocateAppWidgetId();
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        startActivityForResult(pickIntent, request);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_NCM_WIDGET1: {
                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
                    if (id != -1) {
                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_POPUP, id);
                        String msg = "1*4小组件，选择ID：" + id;
                        tv_select_widget1.setText(msg);
                    }
                    break;
                }
                case REQUEST_SELECT_NCM_WIDGET2: {
                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
                    if (id != -1) {
                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_LANNCHER, id);
                        String msg = "2*4小组件，选择ID：" + id;
                        tv_select_widget2.setText(msg);
                    }
                    break;
                }
                case REQUEST_SELECT_QQMUSIC_WIDGET1: {
                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
                    if (id != -1) {
                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_POPUP, id);
                        String msg = "1*4小组件，选择ID：" + id;
                        tv_select_widget1.setText(msg);
                    }
                    break;
                }
                case REQUEST_SELECT_QQMUSIC_WIDGET2: {
                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
                    if (id != -1) {
                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_LANNCHER, id);
                        String msg = "2*4小组件，选择ID：" + id;
                        tv_select_widget2.setText(msg);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }
}
