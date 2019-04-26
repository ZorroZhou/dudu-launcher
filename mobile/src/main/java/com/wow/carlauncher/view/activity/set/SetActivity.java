package com.wow.carlauncher.view.activity.set;

import android.view.View;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.view.activity.set.view.SAppView;
import com.wow.carlauncher.view.activity.set.view.SBleDeviceView;
import com.wow.carlauncher.view.activity.set.view.SLoadAppView;
import com.wow.carlauncher.view.activity.set.view.SPopupView;
import com.wow.carlauncher.view.activity.set.view.SSystemView;
import com.wow.carlauncher.view.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;

public class SetActivity extends BaseActivity {
    @Override
    public void init() {
        setContent(R.layout.activity_set);
    }

    @ViewInject(R.id.sg_app)
    private SetView sg_app;

    @ViewInject(R.id.sg_popup)
    private SetView sg_popup;

    @ViewInject(R.id.sg_system_set)
    private SetView sg_system_set;

    @ViewInject(R.id.sg_load_app)
    private SetView sg_load_app;

    @ViewInject(R.id.sg_ble_device)
    private SetView sg_ble_device;

    @ViewInject(R.id.ll_app)
    private SAppView ll_app;

    @ViewInject(R.id.ll_obd)
    private SBleDeviceView ll_obd;

    @ViewInject(R.id.ll_popup)
    private SPopupView ll_popup;

    @ViewInject(R.id.ll_system)
    private SSystemView ll_system;

    @ViewInject(R.id.ll_load_app)
    private SLoadAppView ll_load_app;

    @Override
    public void initView() {
        setTitle("设置");
        View.OnClickListener groupClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_app.setVisibility(View.GONE);
                ll_popup.setVisibility(View.GONE);
                ll_system.setVisibility(View.GONE);
                ll_obd.setVisibility(View.GONE);
                ll_load_app.setVisibility(View.GONE);

                switch (view.getId()) {
                    case R.id.sg_app: {
                        ll_app.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_popup: {
                        ll_popup.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_system_set: {
                        ll_system.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_ble_device: {
                        ll_obd.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_load_app: {
                        ll_load_app.setVisibility(View.VISIBLE);
                        break;
                    }
                    default: {
                        ll_app.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        sg_app.setOnClickListener(groupClick);
        sg_popup.setOnClickListener(groupClick);
        sg_system_set.setOnClickListener(groupClick);
        sg_ble_device.setOnClickListener(groupClick);
        sg_load_app.setOnClickListener(groupClick);
    }



//    private void selectWidgetRequest(int request) {
//        int widgetId = appWidgetHost.allocateAppWidgetId();
//        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
//        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
//        startActivityForResult(pickIntent, request);
//    }
//
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case REQUEST_SELECT_NCM_WIDGET1: {
//                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
//                    if (id != -1) {
//                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_WIDGET1, id);
//                        String msg = "已选择：" + id;
//                        ncm_w1.setSummary(msg);
//                    }
//                    break;
//                }
//                case REQUEST_SELECT_NCM_WIDGET2: {
//                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
//                    if (id != -1) {
//                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_PLUGIN_NCM_WIDGET2, id);
//                        String msg = "已选择：" + id;
//                        ncm_w2.setSummary(msg);
//                    }
//                    break;
//                }
//                case REQUEST_SELECT_QQMUSIC_WIDGET1: {
//                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
//                    if (id != -1) {
//                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_WIDGET1, id);
//                        String msg = "已选择：" + id;
//                        qqm_w1.setSummary(msg);
//                    }
//                    break;
//                }
//                case REQUEST_SELECT_QQMUSIC_WIDGET2: {
//                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
//                    if (id != -1) {
//                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_PLUGIN_QQMUSIC_WIDGET2, id);
//                        String msg = "已选择：" + id;
//                        qqm_w2.setSummary(msg);
//                    }
//                    break;
//                }
//                default:
//                    break;
//            }
//        }
//    }
}
