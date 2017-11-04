package com.wow.carlauncher.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseActivity;
import com.wow.carlauncher.common.WeatherIconUtil;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.DateUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.plugin.amap.WebService;
import com.wow.carlauncher.plugin.amap.res.WeatherRes;
import com.wow.carlauncher.popupWindow.PopupWin;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.wow.carlauncher.common.CommonData.*;

/**
 * Created by 10124 on 2017/10/26.
 */
public class LauncherActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "LanncherActivity";

    @ViewInject(R.id.item_1)
    private LinearLayout item_1;

    @ViewInject(R.id.iv_set)
    private ImageView iv_set;

    @ViewInject(R.id.time)
    private TextView time;

    @ViewInject(R.id.date)
    private TextView date;

    @ViewInject(R.id.week)
    private TextView week;

    @ViewInject(R.id.tv_tianqi)
    private TextView tv_tianqi;

    @ViewInject(R.id.tv_local)
    private TextView tv_local;

    @ViewInject(R.id.iv_tianqi)
    private ImageView iv_tianqi;

    @ViewInject(R.id.ll_dock1)
    private LinearLayout ll_dock1;
    @ViewInject(R.id.iv_dock1)
    private ImageView iv_dock1;

    @ViewInject(R.id.ll_dock2)
    private LinearLayout ll_dock2;
    @ViewInject(R.id.iv_dock2)
    private ImageView iv_dock2;

    @ViewInject(R.id.ll_dock3)
    private LinearLayout ll_dock3;
    @ViewInject(R.id.iv_dock3)
    private ImageView iv_dock3;

    @ViewInject(R.id.ll_dock4)
    private LinearLayout ll_dock4;
    @ViewInject(R.id.iv_dock4)
    private ImageView iv_dock4;

    @ViewInject(R.id.ll_dock5)
    private LinearLayout ll_dock5;
    @ViewInject(R.id.iv_dock5)
    private ImageView iv_dock5;

    @ViewInject(R.id.ll_dock6)
    private LinearLayout ll_dock6;
    @ViewInject(R.id.iv_dock6)
    private ImageView iv_dock6;

    @ViewInject(R.id.ll_all_apps)
    private LinearLayout ll_all_apps;

    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;
    private PackageManager pm;

    @Override
    public void init() {
        Log.e(TAG, "init!!!!!!!!!!!!!!!!!: " + this);
        setContent(R.layout.activity_lanncher);
        pm = getPackageManager();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        checkPermission();
        checkAppState();
        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));


        mlocationClient = new AMapLocationClient(mContext);

        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(aMapLocationListener);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(1000 * 60 * 20);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();

    }

    @Override
    public void initView() {
        hideTitle();
        if (PluginManage.music().getLauncherView().getParent() != null) {
            ((ViewGroup) PluginManage.music().getLauncherView().getParent()).removeView(PluginManage.music().getLauncherView());
        }
        item_1.addView(PluginManage.music().getLauncherView(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        iv_set.setOnClickListener(this);
        ll_dock1.setOnClickListener(this);
        ll_dock2.setOnClickListener(this);
        ll_dock3.setOnClickListener(this);
        ll_dock4.setOnClickListener(this);
        ll_dock5.setOnClickListener(this);
        ll_dock6.setOnClickListener(this);
        ll_all_apps.setOnClickListener(this);
        iv_set.setOnClickListener(this);

        loadDock();
    }

    private void loadDock() {
        String packname1 = SharedPreUtil.getSharedPreString(SDATA_DOCK1_CLASS);
        if (CommonUtil.isNotNull(packname1)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname1, 0);
                iv_dock1.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
            } catch (Exception e) {
                SharedPreUtil.saveSharedPreString(SDATA_DOCK1_CLASS, null);
            }
        }
        String packname2 = SharedPreUtil.getSharedPreString(SDATA_DOCK2_CLASS);
        if (CommonUtil.isNotNull(packname2)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname2, 0);
                iv_dock2.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
            } catch (Exception e) {
                SharedPreUtil.saveSharedPreString(SDATA_DOCK2_CLASS, null);
            }
        }

        String packname3 = SharedPreUtil.getSharedPreString(SDATA_DOCK3_CLASS);
        if (CommonUtil.isNotNull(packname3)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname3, 0);
                iv_dock3.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
            } catch (Exception e) {
                SharedPreUtil.saveSharedPreString(SDATA_DOCK3_CLASS, null);
            }
        }

        String packname4 = SharedPreUtil.getSharedPreString(SDATA_DOCK4_CLASS);
        if (CommonUtil.isNotNull(packname4)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname4, 0);
                iv_dock4.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
            } catch (Exception e) {
                SharedPreUtil.saveSharedPreString(SDATA_DOCK4_CLASS, null);
            }
        }

        String packname5 = SharedPreUtil.getSharedPreString(SDATA_DOCK5_CLASS);
        if (CommonUtil.isNotNull(packname5)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname5, 0);
                iv_dock5.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
            } catch (Exception e) {
                SharedPreUtil.saveSharedPreString(SDATA_DOCK5_CLASS, null);
            }
        }

        String packname6 = SharedPreUtil.getSharedPreString(SDATA_DOCK6_CLASS);
        if (CommonUtil.isNotNull(packname6)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname6, 0);
                iv_dock6.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
            } catch (Exception e) {
                SharedPreUtil.saveSharedPreString(SDATA_DOCK6_CLASS, null);
            }
        }
    }

    private void openDock(String clazz) {
        Intent appIntent = pm.getLaunchIntentForPackage(clazz);
        if (appIntent != null) {
            appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(appIntent);
        } else {
            showTip("APP丢失");
            loadDock();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_set: {
                startActivity(new Intent(this, SetActivity.class));
                break;
            }
            case R.id.ll_dock1: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK1_CLASS);
                if (CommonUtil.isNull(packname)) {
                    startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK1);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.ll_dock2: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK2_CLASS);
                if (CommonUtil.isNull(packname)) {
                    startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK2);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.ll_dock3: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK3_CLASS);
                if (CommonUtil.isNull(packname)) {
                    startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK3);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.ll_dock4: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK4_CLASS);
                if (CommonUtil.isNull(packname)) {
                    startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK4);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.ll_dock5: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK5_CLASS);
                if (CommonUtil.isNull(packname)) {
                    startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK5);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.ll_dock6: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_DOCK6_CLASS);
                if (CommonUtil.isNull(packname)) {
                    startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK6);
                } else {
                    openDock(packname);
                }
                break;
            }
            case R.id.ll_all_apps: {
                startActivity(new Intent(this, AppMenuActivity.class));
                break;
            }
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            new AlertDialog.Builder(mContext).setTitle("系统提示")
                    .setMessage("APP需要弹出窗口权限！取消后可在APP设置调整！")
                    .setPositiveButton("前往设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            startActivity(intent);
                        }
                    }).setNegativeButton("不在提示", null).show();
        }
    }

    private Timer timer;

    private void checkAppState() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                setTime();
                PopupWin.self().checkShowApp(CommonUtil.getForegroundApp(mContext));
            }
        }, 1000 - System.currentTimeMillis() % 1000, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK1) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK1_CLASS, packName);
                loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK2) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK2_CLASS, packName);
                loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK3) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK3_CLASS, packName);
                loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK4) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK4_CLASS, packName);
                loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK5) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK5_CLASS, packName);
                loadDock();
            }
        }
        if (requestCode == REQUEST_SELECT_APP_TO_DOCK6) {
            if (resultCode == RESULT_OK) {
                String packName = data.getStringExtra(IDATA_PACKAGE_NAME);
                SharedPreUtil.saveSharedPreString(SDATA_DOCK6_CLASS, packName);
                loadDock();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mHomeKeyEventReceiver);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void setTime() {
        Log.e(TAG, "setTime: !!!!!!!!!!!!!!");
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                Date d = new Date();
                String time = DateUtil.dateToString(d, "HH:mm");
                String date = DateUtil.dateToString(d, "yyyy年MM月dd日");
                String week = DateUtil.getWeekOfDate(d);
                LauncherActivity.this.time.setText(time);
                LauncherActivity.this.date.setText(date);
                LauncherActivity.this.week.setText(week);
            }
        });
    }

    private AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(final AMapLocation aMapLocation) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                tv_local.setText(aMapLocation.getDistrict());
                WebService.getWeatherInfo(aMapLocation.getAdCode(), new WebService.CommonCallback<WeatherRes>() {
                    @Override
                    public void callback(WeatherRes res) {
                        if (Integer.valueOf(1).equals(res.getStatus()) && res.getLives().size() > 0) {
                            tv_tianqi.setText(res.getLives().get(0).getWeather() + "  " + res.getLives().get(0).getTemperature() + "℃");
                            iv_tianqi.setImageResource(WeatherIconUtil.getWeatherResId(res.getLives().get(0).getWeather()));
                        }
                    }
                });
            }
        }
    };
    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

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
                } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                    //表示长按home键,显示最近使用的程序列表
                }
            }
        }
    };

}
