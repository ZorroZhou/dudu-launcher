package com.wow.carlauncher.activity.launcher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.wow.carlauncher.activity.AppSelectActivity;
import com.wow.carlauncher.activity.launcher.view.LDockView;
import com.wow.carlauncher.ex.manage.ToastManage;
import com.wow.carlauncher.plugin.console.event.PConsoleEventLightState;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;
import com.wow.carlauncher.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.wow.carlauncher.common.CommonData.*;

public class LauncherActivity extends Activity {
    @ViewInject(R.id.ll_dock)
    private LDockView ll_dock;

    @ViewInject(R.id.fl_bg)
    private FrameLayout fl_bg;

    private PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pm = getPackageManager();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeReceiver, intentFilter);

        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_lanncher);
        x.view().inject(this);
        EventBus.getDefault().register(this);
    }

    @Event(value = {R.id.tv_time})
    private void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.ll_time: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_TIME_PLUGIN_OPEN_APP);
                if (!CommonUtil.isNull(packname)) {
                    Intent appIntent = pm.getLaunchIntentForPackage(packname);
                    if (appIntent != null) {
                        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(appIntent);
                    } else {
                        ToastManage.self().show("APP丢失");
                    }
                }
                break;
            }
        }
    }

    @Event(value = {R.id.ll_dock1, R.id.ll_dock2, R.id.ll_dock3, R.id.ll_dock4, R.id.ll_dock5}, type = View.OnLongClickListener.class)
    private boolean longClickEvent(View view) {
        switch (view.getId()) {
            case R.id.ll_dock1: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK1);
                break;
            }
            case R.id.ll_dock2: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK2);
                break;
            }
            case R.id.ll_dock3: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK3);
                break;
            }
            case R.id.ll_dock4: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK4);
                break;
            }
            case R.id.ll_dock5: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK5);
                break;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK1) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK1_CLASS, packName);
                ll_dock.loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK2) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK2_CLASS, packName);
                ll_dock.loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK3) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK3_CLASS, packName);
                ll_dock.loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK4) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK4_CLASS, packName);
                ll_dock.loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK5) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK5_CLASS, packName);
                ll_dock.loadDock();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(PConsoleEventLightState event) {
        if (event.isOpen()) {
            fl_bg.setBackgroundResource(R.mipmap.bg_l_midnight);
        } else {
            fl_bg.setBackgroundResource(R.mipmap.bg_l_bright);
        }
    }

    @Override
    public void onBackPressed() {

    }

    private BroadcastReceiver homeReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    //表示按了home键,程序到了后台
                    Intent i = new Intent(Intent.ACTION_MAIN, null);
                    i.addCategory(Intent.CATEGORY_HOME);
                    context.startActivity(i);
                }
            }
        }
    };
}
