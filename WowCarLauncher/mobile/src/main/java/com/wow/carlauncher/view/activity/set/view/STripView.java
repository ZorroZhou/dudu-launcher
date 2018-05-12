package com.wow.carlauncher.view.activity.set.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.view.base.BaseView;
import com.wow.carlauncher.view.popupWindow.event.PEventFSRefresh;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;

import static com.wow.carlauncher.common.CommonData.TAG;

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


    @ViewInject(R.id.sv_trip_start_mark)
    private SetView sv_trip_start_mark;


    private void initView() {
        addContent(R.layout.content_set_trip);

        sv_trip_start_mark.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_TRIP_START_WITH_APP, true);
                } else {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_TRIP_START_WITH_APP, false);
                }
            }
        });
        sv_trip_start_mark.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_TRIP_START_WITH_APP, true));
    }
}
