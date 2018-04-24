package com.wow.carlauncher.activity.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.activity.set.SetActivity;
import com.wow.carlauncher.event.EventWifiState;
import com.wow.carlauncher.plugin.fk.event.PFkEventConnect;
import com.wow.carlauncher.plugin.obd.evnet.PObdEventConnect;
import com.wow.frame.util.NetWorkUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by 10124 on 2018/4/22.
 */

public class LPromptView extends LBaseView implements View.OnClickListener {


    public LPromptView(@NonNull Context context) {
        super(context);
        initView();
    }

    public LPromptView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @ViewInject(R.id.iv_carinfo_tp)
    private ImageView iv_carinfo_tp;

    @ViewInject(R.id.iv_obd)
    private ImageView iv_obd;

    @ViewInject(R.id.iv_fk)
    private ImageView iv_fk;

    @ViewInject(R.id.iv_wifi)
    private ImageView iv_wifi;

    @ViewInject(R.id.iv_set)
    private ImageView iv_set;

    private void initView() {
        LinearLayout view = (LinearLayout) View.inflate(getContext(), R.layout.content_l_prompt, null);
        this.addView(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        x.view().inject(this);

        iv_set.setOnClickListener(this);
        iv_wifi.setOnClickListener(this);


        if (NetWorkUtil.isWifiConnected(getContext())) {
            iv_wifi.setVisibility(VISIBLE);
        } else {
            iv_wifi.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_set: {
                getActivity().startActivity(new Intent(getContext(), SetActivity.class));
                break;
            }
            case R.id.iv_wifi: {
                getActivity().startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); //直接进入手机中的wifi网络设置界面
                break;
            }
        }
    }

    private Activity getActivity() {
        return (Activity) getContext();
    }

    @Subscribe
    public void onEventMainThread(final PFkEventConnect event) {
        if (event.isConnected()) {
            iv_obd.setVisibility(VISIBLE);
        } else {
            iv_obd.setVisibility(INVISIBLE);
        }
    }

    @Subscribe
    public void onEventMainThread(final PObdEventConnect event) {
        if (event.isConnected()) {
            iv_fk.setVisibility(VISIBLE);
        } else {
            iv_fk.setVisibility(INVISIBLE);
        }
    }

    @Subscribe
    public void onEventMainThread(final EventWifiState event) {
        if (event.isUsable()) {
            iv_wifi.setVisibility(VISIBLE);
        } else {
            iv_wifi.setVisibility(INVISIBLE);
        }
    }
}
