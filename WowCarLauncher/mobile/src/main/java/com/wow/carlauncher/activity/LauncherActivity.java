package com.wow.carlauncher.activity;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.DateUtil;
import com.wow.frame.util.SharedPreUtil;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.WeatherIconUtil;
import com.wow.carlauncher.event.LauncherCityRefreshEvent;
import com.wow.carlauncher.event.LauncherDockLabelShowChangeEvent;
import com.wow.carlauncher.event.LauncherItemBackgroundRefreshEvent;
import com.wow.carlauncher.event.LauncherItemRefreshEvent;
import com.wow.carlauncher.plugin.LauncherPluginEnum;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.popupWindow.ConsoleWin;
import com.wow.carlauncher.common.amapWebservice.WebService;
import com.wow.carlauncher.common.amapWebservice.res.WeatherRes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.wow.carlauncher.common.CommonData.*;

public class LauncherActivity extends Activity implements View.OnClickListener, View.OnLongClickListener {
    @ViewInject(R.id.item_1)
    private FrameLayout item_1;
    @ViewInject(R.id.item_2)
    private FrameLayout item_2;
    @ViewInject(R.id.item_3)
    private FrameLayout item_3;

    @ViewInject(R.id.time)
    private TextView time;

    @ViewInject(R.id.date)
    private TextView date;

    @ViewInject(R.id.week)
    private TextView week;

    @ViewInject(R.id.tv_tianqi)
    private TextView tv_tianqi;

    @ViewInject(R.id.tv_tianqi2)
    private TextView tv_tianqi2;

    @ViewInject(R.id.iv_tianqi)
    private ImageView iv_tianqi;

    @ViewInject(R.id.ll_dock)
    private LinearLayout ll_dock;

    @ViewInject(R.id.ll_dock1)
    private LinearLayout ll_dock1;
    @ViewInject(R.id.iv_dock1)
    private ImageView iv_dock1;
    @ViewInject(R.id.tv_dock1)
    private TextView tv_dock1;

    @ViewInject(R.id.ll_dock2)
    private LinearLayout ll_dock2;
    @ViewInject(R.id.iv_dock2)
    private ImageView iv_dock2;
    @ViewInject(R.id.tv_dock2)
    private TextView tv_dock2;

    @ViewInject(R.id.ll_dock3)
    private LinearLayout ll_dock3;
    @ViewInject(R.id.iv_dock3)
    private ImageView iv_dock3;
    @ViewInject(R.id.tv_dock3)
    private TextView tv_dock3;

    @ViewInject(R.id.ll_dock4)
    private LinearLayout ll_dock4;
    @ViewInject(R.id.iv_dock4)
    private ImageView iv_dock4;
    @ViewInject(R.id.tv_dock4)
    private TextView tv_dock4;

    @ViewInject(R.id.ll_dock5)
    private LinearLayout ll_dock5;
    @ViewInject(R.id.iv_dock5)
    private ImageView iv_dock5;
    @ViewInject(R.id.tv_dock5)
    private TextView tv_dock5;

    @ViewInject(R.id.ll_dock6)
    private LinearLayout ll_dock6;
    @ViewInject(R.id.iv_dock6)
    private ImageView iv_dock6;
    @ViewInject(R.id.tv_dock6)
    private TextView tv_dock6;

    @ViewInject(R.id.tv_app_appss)
    private TextView tv_app_appss;

    @ViewInject(R.id.tv_controller)
    private TextView tv_controller;

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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        pm = getPackageManager();
        wallManager = WallpaperManager.getInstance(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeReceiver, intentFilter);

        EventBus.getDefault().register(this);

        initView();
        loadDock();
        loadItem();
        loadItemBackground();
        Log.e(TAG, "onCreate: !!!!!!!!!!!" + this);
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

        findViewById(R.id.ll_all_apps).setOnClickListener(this);
        findViewById(R.id.ll_controller).setOnClickListener(this);
        findViewById(R.id.iv_set).setOnClickListener(this);
        findViewById(R.id.ll_time).setOnClickListener(this);
    }


    private void loadDock() {
        String packname1 = SharedPreUtil.getSharedPreString(SDATA_DOCK1_CLASS);
        if (CommonUtil.isNotNull(packname1)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname1, 0);
                iv_dock1.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock1.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                showTip("dock1加载失败:" + e.getMessage());
                SharedPreUtil.saveSharedPreString(SDATA_DOCK1_CLASS, null);
            }
        }
        String packname2 = SharedPreUtil.getSharedPreString(SDATA_DOCK2_CLASS);
        if (CommonUtil.isNotNull(packname2)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname2, 0);
                iv_dock2.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock2.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                showTip("dock2加载失败:" + e.getMessage());
                SharedPreUtil.saveSharedPreString(SDATA_DOCK2_CLASS, null);
            }
        }

        String packname3 = SharedPreUtil.getSharedPreString(SDATA_DOCK3_CLASS);
        if (CommonUtil.isNotNull(packname3)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname3, 0);
                iv_dock3.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock3.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                showTip("dock3加载失败:" + e.getMessage());
                SharedPreUtil.saveSharedPreString(SDATA_DOCK3_CLASS, null);
            }
        }

        String packname4 = SharedPreUtil.getSharedPreString(SDATA_DOCK4_CLASS);
        if (CommonUtil.isNotNull(packname4)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname4, 0);
                iv_dock4.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock4.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                showTip("dock4加载失败:" + e.getMessage());
                SharedPreUtil.saveSharedPreString(SDATA_DOCK4_CLASS, null);
            }
        }

        String packname5 = SharedPreUtil.getSharedPreString(SDATA_DOCK5_CLASS);
        if (CommonUtil.isNotNull(packname5)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname5, 0);
                iv_dock5.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock5.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                showTip("dock5加载失败:" + e.getMessage());
                SharedPreUtil.saveSharedPreString(SDATA_DOCK5_CLASS, null);
            }
        }

        String packname6 = SharedPreUtil.getSharedPreString(SDATA_DOCK6_CLASS);
        if (CommonUtil.isNotNull(packname6)) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packname6, 0);
                iv_dock6.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));
                tv_dock6.setText(packageInfo.applicationInfo.loadLabel(pm));
            } catch (Exception e) {
                showTip("dock5加载失败:" + e.getMessage());
                SharedPreUtil.saveSharedPreString(SDATA_DOCK6_CLASS, null);
            }
        }
        dockLabelShow(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW, true));
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
            case R.id.ll_time: {
                String packname = SharedPreUtil.getSharedPreString(SDATA_TIME_PLUGIN_OPEN_APP);
                if (!CommonUtil.isNull(packname)) {
                    Intent appIntent = pm.getLaunchIntentForPackage(packname);
                    if (appIntent != null) {
                        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(appIntent);
                    } else {
                        showTip("APP丢失");
                    }
                }
                break;
            }
            case R.id.ll_controller: {
                ConsoleWin.self().show();
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

    private Timer timer;
    private int weatherUpdateInterval = 30;
    private int yifenzhong = 1000 * 60;

    private void startTimer() {
        Log.e(TAG, "startTimer: ");
        stopTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                setTime();
                if (weatherUpdateInterval == 30) {
                    weatherUpdateInterval = 0;
                    refreshWeather();
                }
                weatherUpdateInterval++;
            }
        }, yifenzhong - System.currentTimeMillis() % yifenzhong, yifenzhong);
        setTime();
        refreshWeather();
    }

    private void stopTimer() {
        if (timer != null) {
            Log.e(TAG, "stopTimer: ");
            timer.cancel();
            timer = null;
        }
    }

    private void refreshWeather() {
        Log.e(TAG, "refreshWeather: !!!!!!!!!!");
        if (CommonUtil.isNotNull(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY))) {
            WebService.getWeatherInfo(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY), new WebService.CommonCallback<WeatherRes>() {
                @Override
                public void callback(WeatherRes res) {
                    if (Integer.valueOf(1).equals(res.getStatus()) && res.getLives().size() > 0) {
                        tv_tianqi.setText(res.getLives().get(0).getWeather() + "  " + res.getLives().get(0).getTemperature() + "℃");
                        iv_tianqi.setImageResource(WeatherIconUtil.getWeatherResId(res.getLives().get(0).getWeather()));
                        String feng;
                        String wd = res.getLives().get(0).getWinddirection();
                        Log.e(TAG, "callback: " + wd);
                        if (wd.equals("东北") || wd.equals("东") || wd.equals("东南") || wd.equals("南") || wd.equals("西南") || wd.equals("西") || wd.equals("西北") || wd.equals("北")) {
                            feng = wd + "风:";
                        } else {
                            feng = "风力:";
                        }
                        tv_tianqi2.setText(feng + res.getLives().get(0).getWindpower() + "级  空气湿度:" + res.getLives().get(0).getHumidity());
                    } else {
                        tv_tianqi.setText("请检查网络");
                    }
                }
            });
        } else {
            tv_tianqi.setText("请预先设置城市");
            tv_tianqi2.setText("点击设置-时间和天气设置-天气定位进行设置");
        }
    }

    private void loadItemBackground() {
        try {
            int c = Color.parseColor(SharedPreUtil.getSharedPreString(SDATA_LAUNCHER_ITEM1_BG_COLOR));
            ((GradientDrawable) item_1.getBackground()).setColor(c);
        } catch (Exception e) {
            item_1.setBackgroundResource(R.drawable.l_item_bg_black);
        }

        try {
            int c = Color.parseColor(SharedPreUtil.getSharedPreString(SDATA_LAUNCHER_ITEM2_BG_COLOR));
            ((GradientDrawable) item_2.getBackground()).setColor(c);
        } catch (Exception e) {
            item_2.setBackgroundResource(R.drawable.l_item_bg_black);
        }

        try {
            int c = Color.parseColor(SharedPreUtil.getSharedPreString(SDATA_LAUNCHER_ITEM3_BG_COLOR));
            ((GradientDrawable) item_3.getBackground()).setColor(c);
        } catch (Exception e) {
            item_3.setBackgroundResource(R.drawable.l_item_bg_black);
        }

        try {
            int c = Color.parseColor(SharedPreUtil.getSharedPreString(SDATA_LAUNCHER_DOCK_BG_COLOR));
            ll_dock.setBackgroundColor(c);
        } catch (Exception e) {
            ll_dock.setBackgroundResource(R.color.launch_dock_bg);
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
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

    private void dockLabelShow(boolean show) {
        int showFlag;
        if (show) {
            showFlag = View.VISIBLE;
        } else {
            showFlag = View.GONE;
        }
        tv_app_appss.setVisibility(showFlag);
        tv_controller.setVisibility(showFlag);
        tv_dock1.setVisibility(showFlag);
        tv_dock2.setVisibility(showFlag);
        tv_dock3.setVisibility(showFlag);
        tv_dock4.setVisibility(showFlag);
        tv_dock5.setVisibility(showFlag);
        tv_dock6.setVisibility(showFlag);
    }

    private void loadItem() {
        View item1 = PluginManage.self().getLauncherPlugin(LauncherPluginEnum.LAUNCHER_ITEM1).getLauncherView();
        if (item1.getParent() != null) {
            ((ViewGroup) item1.getParent()).removeView(item1);
        }
        item_1.removeAllViews();
        item_1.addView(item1, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        View item2 = PluginManage.self().getLauncherPlugin(LauncherPluginEnum.LAUNCHER_ITEM2).getLauncherView();
        if (item2.getParent() != null) {
            ((ViewGroup) item2.getParent()).removeView(item2);
        }
        item_2.removeAllViews();
        item_2.addView(item2, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        View item3 = PluginManage.self().getLauncherPlugin(LauncherPluginEnum.LAUNCHER_ITEM3).getLauncherView();
        if (item3.getParent() != null) {
            ((ViewGroup) item3.getParent()).removeView(item3);
        }
        item_3.removeAllViews();
        item_3.addView(item3, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Subscribe
    public void onEventMainThread(LauncherItemRefreshEvent event) {
        loadItem();
    }

    @Subscribe
    public void onEventMainThread(LauncherCityRefreshEvent event) {
        refreshWeather();
    }

    @Subscribe
    public void onEventMainThread(LauncherDockLabelShowChangeEvent event) {
        dockLabelShow(event.show);
    }

    @Subscribe
    public void onEventMainThread(LauncherItemBackgroundRefreshEvent event) {
        loadItemBackground();
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
