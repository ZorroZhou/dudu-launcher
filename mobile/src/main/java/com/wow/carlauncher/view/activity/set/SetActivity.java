package com.wow.carlauncher.view.activity.set;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.view.View;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.view.activity.set.event.SEventRefreshAmapPlugin;
import com.wow.carlauncher.view.activity.set.view.SAppView;
import com.wow.carlauncher.view.activity.set.view.SBleDeviceView;
import com.wow.carlauncher.view.activity.set.view.SLoadAppView;
import com.wow.carlauncher.view.activity.set.view.SPopupView;
import com.wow.carlauncher.view.activity.set.view.SSystemView;
import com.wow.carlauncher.view.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_AMAP_PLUGIN;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_AMAP_PLUGIN;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_AMAP_PLUGIN: {
                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
                    if (id != -1) {
                        SharedPreUtil.saveInteger(APP_WIDGET_AMAP_PLUGIN, id);
                        EventBus.getDefault().post(new SEventRefreshAmapPlugin());
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }
}
