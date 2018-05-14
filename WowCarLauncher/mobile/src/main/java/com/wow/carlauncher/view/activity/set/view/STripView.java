package com.wow.carlauncher.view.activity.set.view;

import android.app.Dialog;
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
import com.wow.carlauncher.view.base.BaseDialog;
import com.wow.carlauncher.view.base.BaseView;
import com.wow.carlauncher.view.dialog.InputDialog;
import com.wow.carlauncher.view.popupWindow.event.PEventFSRefresh;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.wow.carlauncher.common.CommonData.*;

/**
 * Created by 10124 on 2018/4/22.
 */

public class STripView extends BaseView {
    public STripView(@NonNull Context context) {
        super(context);
        initView();
    }

    public STripView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    @ViewInject(R.id.sv_auto_open_driving)
    private SetView sv_auto_open_driving;


    @ViewInject(R.id.sv_trip_merge_time)
    private SetView sv_trip_merge_time;


    private void initView() {
        addContent(R.layout.content_set_trip);

        sv_auto_open_driving.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(SDATA_TRIP_AUTO_OPEN_DRIVING, true);
                } else {
                    SharedPreUtil.saveSharedPreBoolean(SDATA_TRIP_AUTO_OPEN_DRIVING, false);
                }
            }
        });
        sv_auto_open_driving.setChecked(SharedPreUtil.getSharedPreBoolean(SDATA_TRIP_AUTO_OPEN_DRIVING, SDATA_TRIP_AUTO_OPEN_DRIVING_DF));

        sv_trip_merge_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final InputDialog inputDialog = new InputDialog(getContext());
                inputDialog.setTitle("输入一个分钟数");
                inputDialog.show();
                inputDialog.setInputString(SharedPreUtil.getSharedPreInteger(SDATA_TRIP_MERGE_TIME, SDATA_TRIP_MERGE_TIME_DF) + "");
                inputDialog.getButton(BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            int xx = Integer.parseInt(inputDialog.getInputString());
                            if (xx < 100) {
                                SharedPreUtil.saveSharedPreInteger(SDATA_TRIP_MERGE_TIME, xx);
                                sv_trip_merge_time.setSummary(getResources().getString(R.string.trip_merge_time_message, SharedPreUtil.getSharedPreInteger(SDATA_TRIP_MERGE_TIME, SDATA_TRIP_MERGE_TIME_DF)));
                                inputDialog.dismiss();
                            } else {
                                ToastManage.self().show("请输入小于100的分钟数");
                            }
                        } catch (Exception e) {
                            ToastManage.self().show("请输入小于100的分钟数");
                        }
                    }
                });
            }
        });
        sv_trip_merge_time.setSummary(getResources().getString(R.string.trip_merge_time_message, SharedPreUtil.getSharedPreInteger(SDATA_TRIP_MERGE_TIME, SDATA_TRIP_MERGE_TIME_DF)));
    }
}
