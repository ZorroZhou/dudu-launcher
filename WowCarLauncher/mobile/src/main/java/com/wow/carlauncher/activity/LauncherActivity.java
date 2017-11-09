package com.wow.carlauncher.activity;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.LocationManage;
import com.wow.carlauncher.common.WeatherIconUtil;
import com.wow.carlauncher.common.util.AppUtil;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.DateUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.webservice.WebService;
import com.wow.carlauncher.webservice.res.WeatherRes;
import com.wow.carlauncher.popupWindow.PopupWin;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.wow.carlauncher.common.CommonData.*;

public class LauncherActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "LanncherActivity";

    @ViewInject(R.id.item_1)
    private LinearLayout item_1;
    @ViewInject(R.id.item_2)
    private LinearLayout item_2;
    @ViewInject(R.id.item_3)
    private LinearLayout item_3;

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

    @ViewInject(R.id.rl_quick)
    private RelativeLayout rl_quick;

    @ViewInject(R.id.fl_bg)
    private FrameLayout fl_bg;

    private Context mContext;

    //高德地图的定位客户端
    private PackageManager pm;
    private WallpaperManager wallManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        init();
        initView();

    }

    public void init() {
        pm = getPackageManager();
        wallManager = WallpaperManager.getInstance(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        intentFilter.addAction(Intent.ACTION_WALLPAPER_CHANGED);
        registerReceiver(homeReceiver, intentFilter);
    }

    public void initView() {
        setContentView(R.layout.activity_lanncher);
        x.view().inject(this);
        //这里要先添加事件，要不然ID重复，会导致读取的子view错误
        ll_dock1.setOnLongClickListener(this);
        ll_dock2.setOnLongClickListener(this);
        ll_dock3.setOnLongClickListener(this);
        ll_dock4.setOnLongClickListener(this);
        ll_dock5.setOnLongClickListener(this);
        ll_dock6.setOnLongClickListener(this);

        ll_dock1.setOnClickListener(this);
        ll_dock2.setOnClickListener(this);
        ll_dock3.setOnClickListener(this);
        ll_dock4.setOnClickListener(this);
        ll_dock5.setOnClickListener(this);
        ll_dock6.setOnClickListener(this);
        rl_quick.setOnClickListener(this);

        findViewById(R.id.ll_all_apps).setOnClickListener(this);
        findViewById(R.id.iv_up1).setOnClickListener(this);
        findViewById(R.id.iv_up2).setOnClickListener(this);
        findViewById(R.id.iv_set).setOnClickListener(this);
        findViewById(R.id.btn_vu).setOnClickListener(this);
        findViewById(R.id.btn_vd).setOnClickListener(this);
        findViewById(R.id.btn_jy).setOnClickListener(this);
        findViewById(R.id.btn_close_screen).setOnClickListener(this);


        if (PluginManage.self().music().getLauncherView().getParent() != null) {
            ((ViewGroup) PluginManage.self().music().getLauncherView().getParent()).removeView(PluginManage.self().music().getLauncherView());
        }
        item_1.addView(PluginManage.self().music().getLauncherView(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        if (PluginManage.self().amapCar().getLauncherView().getParent() != null) {
            ((ViewGroup) PluginManage.self().amapCar().getLauncherView().getParent()).removeView(PluginManage.self().amapCar().getLauncherView());
        }
        item_2.addView(PluginManage.self().amapCar().getLauncherView(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        if (PluginManage.self().controller().getLauncherView().getParent() != null) {
            ((ViewGroup) PluginManage.self().controller().getLauncherView().getParent()).removeView(PluginManage.self().controller().getLauncherView());
        }
        item_3.addView(PluginManage.self().controller().getLauncherView(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        loadDock();
        checkAppState();
        checkPermission();
        setWall();
        LocationManage.self().addLocationListener(aMapLocationListener);
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
            Log.e(TAG, "loadDock: " + packname6);
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
            case R.id.ll_dock6: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK6);
                break;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close_screen: {
                rl_quick.setVisibility(View.GONE);
                startActivity(new Intent(this, LockActivity.class));
                break;
            }

            case R.id.btn_jy: {
                AppUtil.sendKeyCode(KeyEvent.KEYCODE_VOLUME_MUTE);
                break;
            }
            case R.id.btn_vu: {
                AppUtil.sendKeyCode(KeyEvent.KEYCODE_VOLUME_UP);
                break;
            }
            case R.id.btn_vd: {
                AppUtil.sendKeyCode(KeyEvent.KEYCODE_VOLUME_DOWN);
                break;
            }
            case R.id.rl_quick: {
                rl_quick.setVisibility(View.GONE);
                break;
            }
            case R.id.iv_up1: {
                rl_quick.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.iv_up2: {
                rl_quick.setVisibility(View.VISIBLE);
                break;
            }
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
                            //其实这个判断没什么卵用，但是不加会有警告
                            if (Build.VERSION.SDK_INT >= 23) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                startActivity(intent);
                            }
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
                PopupWin.self().checkShowApp(AppUtil.getForegroundApp(mContext));
            }
        }, 1000 - System.currentTimeMillis() % 1000, 1000);
    }

    private void setWall() {
        if (fl_bg != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                fl_bg.setBackground(wallManager.getDrawable());
            } else {
                fl_bg.setBackgroundDrawable(wallManager.getDrawable());
            }
        }
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
    protected void onResume() {
        super.onResume();
        PluginManage.self().setCurrentActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
        LocationManage.self().removeLocationListener(aMapLocationListener);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onBackPressed() {

    }

    private static Toast toast = null;

    private void showTip(final String msg) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
                } else {
                    toast.cancel();
                    toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
                }
                toast.show();
            }
        });
    }

    private void setTime() {
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
    private BroadcastReceiver homeReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        //String SYSTEM_HOME_KEY_LONG = "recentapps";

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
            } else if (intent.getAction().equals(Intent.ACTION_WALLPAPER_CHANGED)) {
                setWall();
            }
        }
    };

}
