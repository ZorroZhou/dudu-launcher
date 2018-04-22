package com.wow.carlauncher.activity.set.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppInfoManage;
import com.wow.carlauncher.common.BaseDialog;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.ex.ToastEx;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.dialog.CityDialog;
import com.wow.carlauncher.event.LauncherCityRefreshEvent;
import com.wow.carlauncher.plugin.obd.ObdProtocolEnum;
import com.wow.frame.util.AppUtil;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;
import com.wow.frame.util.ThreadObj;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/22.
 */

public class STimeView extends FrameLayout {
    public static final ObdProtocolEnum[] ALL_OBD_CONTROLLER = {ObdProtocolEnum.YJ_TYB};

    public STimeView(@NonNull Context context) {
        super(context);
        initView();
    }

    public STimeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @ViewInject(R.id.time_plugin_open_app_select)
    private SetView time_plugin_open_app_select;


    @ViewInject(R.id.tianqi_city)
    private SetView tianqi_city;

    private void initView() {
        LinearLayout view = (LinearLayout) View.inflate(getContext(), R.layout.content_set_time, null);
        this.addView(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        x.view().inject(this);

        time_plugin_open_app_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_TIME_PLUGIN_OPEN_APP);
                final List<AppUtil.AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getAppInfos());
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
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
                final CityDialog cityDialog = new CityDialog(getContext());
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
                            ToastEx.self().show("请选择城市");
                        }
                        return false;
                    }
                });
                cityDialog.show();
            }
        });
        tianqi_city.setSummary(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY));
    }
}
