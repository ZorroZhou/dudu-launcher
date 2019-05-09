package com.wow.carlauncher.view.activity.driving;

import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.activity.driving.coolBlack.CoolBlackView;
import com.wow.carlauncher.view.base.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by 10124 on 2018/4/25.
 */

public class DrivingActivity extends BaseActivity {
    private boolean isFront = false;

    @BindView(R.id.content)
    FrameLayout content;

    private DrivingView nowContent;

    @Override
    public void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContent(R.layout.activity_driving);
    }

    @Override
    public void initView() {
        hideTitle();
        loadView();

    }

    private void loadView() {
        nowContent = new CoolBlackView(this);
        content.addView(nowContent, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        nowContent.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(isTaskRoot());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        isFront = true;
        if (nowContent != null) {
            nowContent.setFront(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isFront = false;
        if (nowContent != null) {
            nowContent.setFront(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventConnect event) {
        if (isFront && !event.isConnected()) {
            ToastManage.self().show("OBD失去连接");
            moveTaskToBack(true);
        }
    }
}
