package com.wow.carlauncher.activity;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.view.View;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseActivity;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.plugin.music.MusicControllerEnum;

import org.xutils.view.annotation.ViewInject;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_HOST_ID;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_NCM_WIDGET1;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_NCM_WIDGET2;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_QQMUSIC_WIDGET1;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_QQMUSIC_WIDGET2;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_NCM_WIDGET2;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_NCM_WIDGET1;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_QQMUSIC_WIDGET2;
import static com.wow.carlauncher.common.CommonData.SDATA_MUSIC_PLUGIN_QQMUSIC_WIDGET1;
import static com.wow.carlauncher.plugin.music.MusicControllerEnum.NETEASECLOUD;
import static com.wow.carlauncher.plugin.music.MusicControllerEnum.QQMUSIC;
import static com.wow.carlauncher.plugin.music.MusicControllerEnum.QQMUSICCAR;
import static com.wow.carlauncher.plugin.music.MusicControllerEnum.SYSTEM;

/**
 * Created by 10124 on 2017/10/26.
 */

public class PluginSetActivity extends BaseActivity {
    private static final String TAG = "PluginSetActivity";
    private final static String[] MUSIC_CONTORLLERS = {SYSTEM.getName(), NETEASECLOUD.getName(), QQMUSICCAR.getName(), QQMUSIC.getName()};


    @ViewInject(R.id.ncm_w1)
    private SetView ncm_w1;

    @ViewInject(R.id.ncm_w2)
    private SetView ncm_w2;

    @ViewInject(R.id.qqm_w1)
    private SetView qqm_w1;

    @ViewInject(R.id.qqm_w2)
    private SetView qqm_w2;

    private AppWidgetHost appWidgetHost;

    @Override
    public void init() {
        setContent(R.layout.activity_plugin_set);
    }

    @Override
    public void initView() {
        setTitle("音乐插件设置");
        ncm_w1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectWidgetRequest(REQUEST_SELECT_NCM_WIDGET1);
            }
        });

        int nw1id = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_WIDGET1, -1);
        if (nw1id == -1) {
            String msg = "未选择";
            ncm_w1.setSummary(msg);
        } else {
            String msg = "已选择：" + nw1id;
            ncm_w1.setSummary(msg);
        }

        ncm_w2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectWidgetRequest(REQUEST_SELECT_NCM_WIDGET2);
            }
        });
        int nw2id = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_WIDGET2, -1);
        if (nw2id == -1) {
            String msg = "未选择";
            ncm_w2.setSummary(msg);
        } else {
            String msg = "已选择：" + nw2id;
            ncm_w2.setSummary(msg);
        }

        qqm_w1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectWidgetRequest(REQUEST_SELECT_QQMUSIC_WIDGET1);
            }
        });

        int qqw1id = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_WIDGET1, -1);
        if (qqw1id == -1) {
            String msg = "未选择";
            qqm_w1.setSummary(msg);
        } else {
            String msg = "已选择：" + qqw1id;
            qqm_w1.setSummary(msg);
        }

        qqm_w2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectWidgetRequest(REQUEST_SELECT_QQMUSIC_WIDGET2);
            }
        });

        int qqw2id = SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_WIDGET2, -1);
        if (qqw2id == -1) {
            String msg = "未选择";
            qqm_w2.setSummary(msg);
        } else {
            String msg = "已选择：" + qqw2id;
            qqm_w2.setSummary(msg);
        }

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
                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_WIDGET1, id);
                        String msg = "已选择：" + id;
                        ncm_w1.setSummary(msg);
                    }
                    break;
                }
                case REQUEST_SELECT_NCM_WIDGET2: {
                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
                    if (id != -1) {
                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_WIDGET2, id);
                        String msg = "已选择：" + id;
                        ncm_w2.setSummary(msg);
                    }
                    break;
                }
                case REQUEST_SELECT_QQMUSIC_WIDGET1: {
                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
                    if (id != -1) {
                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_WIDGET1, id);
                        String msg = "已选择：" + id;
                        qqm_w1.setSummary(msg);
                    }
                    break;
                }
                case REQUEST_SELECT_QQMUSIC_WIDGET2: {
                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
                    if (id != -1) {
                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_WIDGET2, id);
                        String msg = "已选择：" + id;
                        qqm_w2.setSummary(msg);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }
}
