package com.wow.carlauncher.view.activity.driving;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.manage.trip.TripManage;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.ObdTask;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.activity.driving.coolBlack.CoolBlackView;
import com.wow.carlauncher.view.base.BaseActivity;
import com.wow.frame.util.DateUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;
import java.util.Locale;

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

        TripManage.self().setDrivingShow(true);

        if (!ObdPlugin.self().isConnect()) {
            ToastManage.self().show("OBD没有连接");
            moveTaskToBack(isTaskRoot());
        }
    }

    @Event(value = {R.id.iv_back2})
    private void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_back2: {
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确定推出驾驶界面吗?")
                        .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!ObdPlugin.self().isConnect()) {
            ToastManage.self().show("OBD没有连接");
            moveTaskToBack(isTaskRoot());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TripManage.self().setDrivingShow(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final PObdEventConnect event) {
        if (isFront && !event.isConnected()) {
            ToastManage.self().show("OBD失去连接");
            moveTaskToBack(true);
        }
    }
}
