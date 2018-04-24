package com.wow.carlauncher.activity.launcher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wow.carlauncher.activity.AppMenuActivity;
import com.wow.carlauncher.activity.AppSelectActivity;
import com.wow.carlauncher.activity.launcher.view.LDockView;
import com.wow.carlauncher.activity.set.SetActivity;
import com.wow.carlauncher.common.ex.ToastEx;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.DateUtil;
import com.wow.frame.util.SharedPreUtil;
import com.wow.carlauncher.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.wow.carlauncher.common.CommonData.*;

public class LauncherActivity extends Activity implements View.OnClickListener, View.OnLongClickListener {

    @ViewInject(R.id.time)
    private TextView time;

    @ViewInject(R.id.ll_dock)
    private LDockView ll_dock;

    private Context mContext;

    private PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
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

        //findViewById(R.id.iv_set).setOnClickListener(this);
        findViewById(R.id.ll_time).setOnClickListener(this);

        ll_dock.findViewById(R.id.ll_dock1).setOnLongClickListener(this);
        ll_dock.findViewById(R.id.ll_dock2).setOnLongClickListener(this);
        ll_dock.findViewById(R.id.ll_dock3).setOnLongClickListener(this);
        ll_dock.findViewById(R.id.ll_dock4).setOnLongClickListener(this);
        ll_dock.findViewById(R.id.ll_dock5).setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View view) {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_time: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_TIME_PLUGIN_OPEN_APP);
                if (!CommonUtil.isNull(packname)) {
                    Intent appIntent = pm.getLaunchIntentForPackage(packname);
                    if (appIntent != null) {
                        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(appIntent);
                    } else {
                        ToastEx.self().show("APP丢失");
                    }
                }
                break;
            }
//            case R.id.iv_set: {
//                startActivity(new Intent(this, SetActivity.class));
//                break;
//            }
        }
    }

    private Timer timer;
    private int yifenzhong = 1000 * 60;

    private void startTimer() {
        Log.e(TAG, "startTimer: ");
        stopTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                setTime();
            }
        }, yifenzhong - System.currentTimeMillis() % yifenzhong, yifenzhong);
        setTime();
    }

    private void setTime() {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                Date d = new Date();
                LauncherActivity.this.time.setText(DateUtil.dateToString(d, "HH:mm   " + DateUtil.getWeekOfDate(d) + " yyyy/MM/dd "));
            }
        });
    }

    private void stopTimer() {
        if (timer != null) {
            Log.e(TAG, "stopTimer: ");
            timer.cancel();
            timer = null;
        }
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
    protected void onStart() {
        super.onStart();
        startTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
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
