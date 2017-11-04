package com.wow.carlauncher.activity;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_HOST_ID;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_NCM_WIDGET1;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_NCM_WIDGET2;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_QQMUSIC_WIDGET1;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_QQMUSIC_WIDGET2;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_NCM_LANNCHER;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_NCM_POPUP;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_QQMUSIC_LANNCHER;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_QQMUSIC_POPUP;
import static com.wow.carlauncher.plugin.music.MusicControllerEnum.NETEASECLOUD;
import static com.wow.carlauncher.plugin.music.MusicControllerEnum.QQMUSIC;
import static com.wow.carlauncher.plugin.music.MusicControllerEnum.QQMUSICCAR;
import static com.wow.carlauncher.plugin.music.MusicControllerEnum.SYSTEM;

/**
 * Created by 10124 on 2017/10/26.
 */

public class MusicPluginSetActivity extends BaseActivity {
    private static final String TAG = "MusicPluginSetActivity";
    private final static String[] MUSIC_CONTORLLERS = {SYSTEM.getName(), NETEASECLOUD.getName(), QQMUSICCAR.getName(), QQMUSIC.getName()};


    @ViewInject(R.id.music_controller_select)
    private SetView music_controller_select;

    @ViewInject(R.id.ll_p1)
    private LinearLayout ll_p1;

    @ViewInject(R.id.ll_p2)
    private LinearLayout ll_p2;

    @ViewInject(R.id.sv_plugin1)
    private SetView sv_plugin1;

    @ViewInject(R.id.sv_plugin2)
    private SetView sv_plugin2;

    @ViewInject(R.id.save)
    private Button save;


    private MusicControllerEnum nowMusicControllerEnum;

    private AppWidgetHost appWidgetHost;

    @Override
    public void init() {
        setContent(R.layout.activity_music_plugin_set);
        nowMusicControllerEnum = MusicControllerEnum.valueOfId(SharedPreUtil.getSharedPreInteger(CommonData.SDATA_CURRENT_MUSIC_CONTROLLER, SYSTEM.getId()));
    }

    @Override
    public void initView() {
        setTitle("音乐插件设置");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_CURRENT_MUSIC_CONTROLLER, nowMusicControllerEnum.getId());
                PluginManage.music().selectMusicController(nowMusicControllerEnum);
                setResult(RESULT_OK);
                finish();
            }
        });
        sv_plugin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nowMusicControllerEnum.equals(MusicControllerEnum.NETEASECLOUD)) {
                    selectWidgetRequest(REQUEST_SELECT_NCM_WIDGET1);
                } else if (nowMusicControllerEnum.equals(MusicControllerEnum.QQMUSIC)) {
                    selectWidgetRequest(REQUEST_SELECT_QQMUSIC_WIDGET1);
                }
            }
        });
        sv_plugin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nowMusicControllerEnum.equals(MusicControllerEnum.NETEASECLOUD)) {
                    selectWidgetRequest(REQUEST_SELECT_NCM_WIDGET2);
                } else if (nowMusicControllerEnum.equals(MusicControllerEnum.QQMUSIC)) {
                    selectWidgetRequest(REQUEST_SELECT_QQMUSIC_WIDGET2);
                }
            }
        });
        music_controller_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer select = 0;
                int selectId = nowMusicControllerEnum.getId();
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
                        nowMusicControllerEnum = MusicControllerEnum.valueOfName(MUSIC_CONTORLLERS[obj.getObj()]);
                        refreshInfo();
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
        refreshInfo();

        appWidgetHost = new AppWidgetHost(getApplication(), APP_WIDGET_HOST_ID);
    }

    private void refreshInfo() {
        music_controller_select.setSummary(nowMusicControllerEnum.getName());
        if (nowMusicControllerEnum.equals(MusicControllerEnum.NETEASECLOUD) || nowMusicControllerEnum.equals(MusicControllerEnum.QQMUSIC)) {
            ll_p1.setVisibility(View.VISIBLE);
            ll_p2.setVisibility(View.VISIBLE);
        } else {
            ll_p1.setVisibility(View.GONE);
            ll_p2.setVisibility(View.GONE);
        }

        if (nowMusicControllerEnum.equals(MusicControllerEnum.NETEASECLOUD)) {
            int pid = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_POPUP, -1);
            if (pid == -1) {
                String msg = "未选择";
                sv_plugin1.setSummary(msg);
            } else {
                String msg = "已选择：" + pid;
                sv_plugin1.setSummary(msg);
            }

            int lid = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_LANNCHER, -1);
            if (lid == -1) {
                String msg = "未选择";
                sv_plugin2.setSummary(msg);
            } else {
                String msg = "已选择：" + lid;
                sv_plugin2.setSummary(msg);
            }
        } else if (nowMusicControllerEnum.equals(MusicControllerEnum.QQMUSIC)) {
            int pid = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_POPUP, -1);
            if (pid == -1) {
                String msg = "未选择";
                sv_plugin1.setSummary(msg);
            } else {
                String msg = "已选择：" + pid;
                sv_plugin1.setSummary(msg);
            }

            int lid = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_LANNCHER, -1);
            if (lid == -1) {
                String msg = "未选择";
                sv_plugin2.setSummary(msg);
            } else {
                String msg = "已选择：" + lid;
                sv_plugin2.setSummary(msg);
            }
        }
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
                        String msg = "已选择：" + id;
                        sv_plugin1.setSummary(msg);
                    }
                    break;
                }
                case REQUEST_SELECT_NCM_WIDGET2: {
                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
                    if (id != -1) {
                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_LANNCHER, id);
                        String msg = "已选择：" + id;
                        sv_plugin2.setSummary(msg);
                    }
                    break;
                }
                case REQUEST_SELECT_QQMUSIC_WIDGET1: {
                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
                    if (id != -1) {
                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_POPUP, id);
                        String msg = "已选择：" + id;
                        sv_plugin1.setSummary(msg);
                    }
                    break;
                }
                case REQUEST_SELECT_QQMUSIC_WIDGET2: {
                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
                    if (id != -1) {
                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_LANNCHER, id);
                        String msg = "已选择：" + id;
                        sv_plugin2.setSummary(msg);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }
}
