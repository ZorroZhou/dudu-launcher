package com.wow.carlauncher.view.activity.set.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.view.activity.driving.DrivingViewEnum;
import com.wow.carlauncher.view.activity.set.SetEnumOnClickListener;
import com.wow.carlauncher.view.activity.set.event.SAEventRefreshDriving;
import com.wow.carlauncher.view.base.BaseView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

import static com.wow.carlauncher.common.CommonData.SDATA_DRIVING_VIEW;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SDrivingView extends BaseView {

    public SDrivingView(@NonNull Context context) {
        super(context);
    }

    public SDrivingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_driving;
    }

    @BindView(R.id.sv_driving_type)
    SetView sv_driving_type;

    @Override
    protected void initView() {
        sv_driving_type.setSummary(DrivingViewEnum.getById(SharedPreUtil.getInteger(SDATA_DRIVING_VIEW, DrivingViewEnum.BLACK.getId())).getName());
        sv_driving_type.setOnClickListener(new SetEnumOnClickListener<DrivingViewEnum>(getContext(), CommonData.DRIVING_VIEW) {
            @Override
            public String title() {
                return "请选择首页切换动画";
            }

            @Override
            public DrivingViewEnum getCurr() {
                return DrivingViewEnum.getById(SharedPreUtil.getInteger(SDATA_DRIVING_VIEW, DrivingViewEnum.BLACK.getId()));
            }

            @Override
            public void onSelect(DrivingViewEnum setEnum) {
                SharedPreUtil.saveInteger(SDATA_DRIVING_VIEW, setEnum.getId());
                sv_driving_type.setSummary(setEnum.getName());
                EventBus.getDefault().post(new SAEventRefreshDriving());
            }
        });
    }
}
