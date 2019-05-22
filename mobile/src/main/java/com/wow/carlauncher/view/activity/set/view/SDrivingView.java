package com.wow.carlauncher.view.activity.set.view;

import android.annotation.SuppressLint;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.view.activity.driving.AutoDrivingEnum;
import com.wow.carlauncher.view.activity.driving.DrivingViewEnum;
import com.wow.carlauncher.view.activity.launcher.event.LItemRefreshEvent;
import com.wow.carlauncher.view.activity.set.SetActivity;
import com.wow.carlauncher.view.activity.set.SetBaseView;
import com.wow.carlauncher.view.activity.set.event.SAEventRefreshDriving;
import com.wow.carlauncher.view.activity.set.listener.SetNumSelectView;
import com.wow.carlauncher.view.activity.set.listener.SetSingleSelectView;
import com.wow.carlauncher.view.activity.set.listener.SetSwitchOnClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.Collection;

import butterknife.BindView;

import static com.wow.carlauncher.common.CommonData.SDATA_AUTO_TO_DRIVING_TYPE;
import static com.wow.carlauncher.common.CommonData.SDATA_DRIVING_VIEW;

/**
 * Created by 10124 on 2018/4/22.
 */
@SuppressLint("ViewConstructor")
public class SDrivingView extends SetBaseView {
    public SDrivingView(SetActivity activity) {
        super(activity);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_driving;
    }

    @Override
    public String getName() {
        return "驾驶界面设置";
    }

    @BindView(R.id.sv_driving_type)
    SetView sv_driving_type;

    @BindView(R.id.sv_auto_to_driving)
    SetView sv_auto_to_driving;

    @BindView(R.id.sv_auto_to_driving_time)
    SetView sv_auto_to_driving_time;

    @BindView(R.id.sv_auto_to_driving_type)
    SetView sv_auto_to_driving_type;

    @BindView(R.id.sv_driving_animation)
    SetView sv_driving_animation;


    @Override
    protected void initView() {
        sv_driving_type.setSummary(DrivingViewEnum.getById(SharedPreUtil.getInteger(SDATA_DRIVING_VIEW, DrivingViewEnum.BLACK.getId())).getName());
        sv_driving_type.setOnClickListener(new SetSingleSelectView<DrivingViewEnum>(getActivity(), "请选择首页切换动画") {
            @Override
            public Collection<DrivingViewEnum> getAll() {
                return Arrays.asList(CommonData.DRIVING_VIEW);
            }

            @Override
            public DrivingViewEnum getCurr() {
                return DrivingViewEnum.getById(SharedPreUtil.getInteger(SDATA_DRIVING_VIEW, DrivingViewEnum.BLACK.getId()));
            }

            @Override
            public boolean onSelect(DrivingViewEnum setEnum) {
                SharedPreUtil.saveInteger(SDATA_DRIVING_VIEW, setEnum.getId());
                sv_driving_type.setSummary(setEnum.getName());
                EventBus.getDefault().post(new SAEventRefreshDriving());
                return true;
            }
        });

        sv_driving_animation.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_DRIVING_VIEW_ABUNATION));
        sv_driving_animation.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_DRIVING_VIEW_ABUNATION, true));

        sv_auto_to_driving.setOnValueChangeListener(new SetSwitchOnClickListener(CommonData.SDATA_AUTO_TO_DRIVING));
        sv_auto_to_driving.setChecked(SharedPreUtil.getBoolean(CommonData.SDATA_AUTO_TO_DRIVING, false));

        sv_auto_to_driving_type.setSummary(AutoDrivingEnum.getById(SharedPreUtil.getInteger(SDATA_AUTO_TO_DRIVING_TYPE, AutoDrivingEnum.TIME.getId())).getName());
        sv_auto_to_driving_type.setOnClickListener(new SetSingleSelectView<AutoDrivingEnum>(getActivity(), "选择自动跳转方式") {
            @Override
            public Collection<AutoDrivingEnum> getAll() {
                return Arrays.asList(CommonData.AUTO_DRIVING_TYPES);
            }

            @Override
            public AutoDrivingEnum getCurr() {
                return AutoDrivingEnum.getById(SharedPreUtil.getInteger(SDATA_AUTO_TO_DRIVING_TYPE, AutoDrivingEnum.TIME.getId()));
            }

            @Override
            public boolean onSelect(AutoDrivingEnum setEnum) {
                SharedPreUtil.saveInteger(SDATA_AUTO_TO_DRIVING_TYPE, setEnum.getId());
                sv_auto_to_driving_type.setSummary(setEnum.getName());
                return true;
            }
        });

        sv_auto_to_driving_time.setSummary(SharedPreUtil.getInteger(CommonData.SDATA_AUTO_TO_DRIVING_TIME, 60) + "秒");
        sv_auto_to_driving_time.setOnClickListener(new SetNumSelectView(getActivity(), "返回桌面的延迟时间", "秒", 20, 600, 10) {
            @Override
            public Integer getCurr() {
                return SharedPreUtil.getInteger(CommonData.SDATA_AUTO_TO_DRIVING_TIME, 60);
            }

            @Override
            public void onSelect(Integer t, String ss) {
                SharedPreUtil.saveInteger(CommonData.SDATA_AUTO_TO_DRIVING_TIME, t);
                sv_auto_to_driving_time.setSummary(ss);
                EventBus.getDefault().post(new LItemRefreshEvent());
            }
        });
    }
}
