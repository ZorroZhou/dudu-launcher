package com.wow.carlauncher.view.activity.set;

import android.view.View;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.view.activity.set.view.SAppView;
import com.wow.carlauncher.view.activity.set.view.SFangKongView;
import com.wow.carlauncher.view.activity.set.view.SObdView;
import com.wow.carlauncher.view.activity.set.view.SPopupView;
import com.wow.carlauncher.view.activity.set.view.SSystemView;
import com.wow.carlauncher.view.activity.set.view.STimeView;
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

    @ViewInject(R.id.sg_time)
    private SetView sg_time;

    @ViewInject(R.id.sg_system_set)
    private SetView sg_system_set;

    @ViewInject(R.id.sg_fangkong)
    private SetView sg_fangkong;

    @ViewInject(R.id.sg_obd)
    private SetView sg_obd;

    @ViewInject(R.id.ll_app)
    private SAppView ll_app;

    @ViewInject(R.id.ll_fangkong)
    private SFangKongView ll_fangkong;

    @ViewInject(R.id.ll_obd)
    private SObdView ll_obd;

    @ViewInject(R.id.ll_popup)
    private SPopupView ll_popup;

    @ViewInject(R.id.ll_time)
    private STimeView ll_time;

    @ViewInject(R.id.ll_system)
    private SSystemView ll_system;

    @Override
    public void initView() {
        setTitle("设置");
        View.OnClickListener groupClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_app.setVisibility(View.GONE);
                ll_popup.setVisibility(View.GONE);
                ll_time.setVisibility(View.GONE);
                ll_system.setVisibility(View.GONE);
                ll_fangkong.setVisibility(View.GONE);
                ll_obd.setVisibility(View.GONE);

                switch (view.getId()) {
                    case R.id.sg_app: {
                        ll_app.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_popup: {
                        ll_popup.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_time: {
                        ll_time.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_fangkong: {
                        ll_fangkong.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_system_set: {
                        ll_system.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_obd: {
                        ll_obd.setVisibility(View.VISIBLE);
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
        sg_time.setOnClickListener(groupClick);
        sg_system_set.setOnClickListener(groupClick);
        sg_fangkong.setOnClickListener(groupClick);
        sg_obd.setOnClickListener(groupClick);
    }
}
