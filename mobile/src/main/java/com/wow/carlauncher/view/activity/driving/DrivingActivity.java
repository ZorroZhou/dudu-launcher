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
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 10124 on 2018/4/25.
 */

public class DrivingActivity extends BaseActivity {
    private final static String TAG = "DrivingActivity";
    private boolean isFront = false;

    @ViewInject(R.id.content)
    private FrameLayout content;

    @Override
    public void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContent(R.layout.activity_driving);
    }

    @Override
    public void initView() {
        hideTitle();
        View nowContent = new CoolBlackView(this);
        content.addView(nowContent, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

//        if (ObdPlugin.self().notConnect()) {
//            ToastManage.self().show("OBD没有连接");
//            moveTaskToBack(isTaskRoot());
//        }
    }

    @Event(value = {R.id.iv_back2})
    private void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_back2: {
                moveTaskToBack(isTaskRoot());
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isFront = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isFront = false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final PObdEventConnect event) {
        if (isFront && !event.isConnected()) {
            ToastManage.self().show("OBD失去连接");
            moveTaskToBack(true);
        }
    }
}
