package com.wow.carlauncher.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.wow.carlauncher.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.plugin.amapcar.AMapCarPluginListener;
import com.wow.carlauncher.plugin.amapcar.NaviInfo;
import com.wow.carlauncher.plugin.fk.FangkongPlugin;
import com.wow.carlauncher.plugin.fk.FangkongPluginListener;
import com.wow.carlauncher.plugin.music.MusicPlugin;
import com.wow.carlauncher.plugin.music.MusicPluginListener;
import com.wow.frame.util.AppUtil;
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
import com.wow.carlauncher.common.amapWebservice.WebService;
import com.wow.carlauncher.common.amapWebservice.res.WeatherRes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.wow.carlauncher.common.CommonData.*;
import static com.wow.carlauncher.plugin.amapcar.AMapCarConstant.AMAP_PACKAGE;
import static com.wow.carlauncher.plugin.amapcar.AMapCarConstant.ICONS;

public class LauncherActivity extends Activity implements View.OnClickListener, View.OnLongClickListener,
        AMapCarPluginListener,
        MusicPluginListener,
        AMapLocationListener,
        FangkongPluginListener {
    @ViewInject(R.id.item_music)
    private FrameLayout item_music;
    @ViewInject(R.id.item_2)
    private FrameLayout item_info;
    @ViewInject(R.id.item_amap)
    private FrameLayout item_amap;

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

    @ViewInject(R.id.tv_app_appss)
    private TextView tv_app_appss;

    @ViewInject(R.id.iv_fangkong_state)
    private ImageView iv_fangkong_state;

    private Context mContext;

    //高德地图的定位客户端
    private PackageManager pm;

    private AMapLocationClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        pm = getPackageManager();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeReceiver, intentFilter);

        EventBus.getDefault().register(this);

        initView();
        loadDock();
        loadItem();
        loadItemBackground();
        loadLoaction();

        FangkongPlugin.self().addListener(this);
        MusicPlugin.self().addListener(this);
        AMapCarPlugin.self().addListener(this);
    }

    private long lastTime = -1;
    private double lastLat = -1, lastLng = -1;

    private void loadLoaction() {
        locationClient = new AMapLocationClient(getApplicationContext());

        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        locationClient.setLocationListener(this);
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        locationOption.setInterval(100);
        locationOption.setGpsFirst(true);
        locationOption.setNeedAddress(false);
        locationOption.setSensorEnable(true);
        locationClient.setLocationOption(locationOption);
        locationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        try {
            if (aMapLocation.getErrorCode() == AMapLocation.LOCATION_SUCCESS && aMapLocation.getLocationType() == AMapLocation.LOCATION_TYPE_GPS) {
                if (lastLat == -1 || lastTime == -1 || lastLng == -1) {
                    lastLat = aMapLocation.getLatitude();
                    lastLng = aMapLocation.getLongitude();
                    lastTime = System.currentTimeMillis();
                    return;
                }

                int speed = (int) (AMapUtils.calculateLineDistance(new LatLng(lastLat, lastLng), new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())) / ((System.currentTimeMillis() - lastTime) / 1000 / 60 / 60)) / 1000;
                if (speed > 0 && speed < 200) {
                    tv_speed.setText(speed + "");
                }

                lastLat = aMapLocation.getLatitude();
                lastLng = aMapLocation.getLongitude();
                lastTime = System.currentTimeMillis();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        ll_dock1.setOnClickListener(this);
        ll_dock2.setOnClickListener(this);
        ll_dock3.setOnClickListener(this);
        ll_dock4.setOnClickListener(this);
        ll_dock5.setOnClickListener(this);


        findViewById(R.id.ll_fangkong).setOnClickListener(this);
        findViewById(R.id.ll_all_apps).setOnClickListener(this);
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
            case R.id.ll_all_apps: {
                startActivity(new Intent(this, AppMenuActivity.class));
                break;
            }
            case R.id.ll_fangkong: {
                FangkongPlugin.self().connect();
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
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
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
        });
    }

    private void loadItemBackground() {
        try {
            int c = Color.parseColor(SharedPreUtil.getSharedPreString(SDATA_LAUNCHER_ITEM1_BG_COLOR));
            ((GradientDrawable) item_music.getBackground()).setColor(c);
        } catch (Exception e) {
        }

        try {
            int c = Color.parseColor(SharedPreUtil.getSharedPreString(SDATA_LAUNCHER_ITEM2_BG_COLOR));
            ((GradientDrawable) item_info.getBackground()).setColor(c);
        } catch (Exception e) {
        }

        try {
            int c = Color.parseColor(SharedPreUtil.getSharedPreString(SDATA_LAUNCHER_ITEM3_BG_COLOR));
            ((GradientDrawable) item_amap.getBackground()).setColor(c);
        } catch (Exception e) {
        }

        try {
            int c = Color.parseColor(SharedPreUtil.getSharedPreString(SDATA_LAUNCHER_DOCK_BG_COLOR));
            ll_dock.setBackgroundColor(c);
        } catch (Exception e) {
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
        locationClient.stopLocation();
        locationClient.unRegisterLocationListener(this);
        locationClient = null;
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
        tv_dock1.setVisibility(showFlag);
        tv_dock2.setVisibility(showFlag);
        tv_dock3.setVisibility(showFlag);
        tv_dock4.setVisibility(showFlag);
        tv_dock5.setVisibility(showFlag);
    }

    //高德地图的视图
    private View amapController;
    private ImageView amapIcon;
    private LinearLayout amapnavi;
    private TextView amapdis;
    private TextView amaproad;
    private TextView amapmsg;


    private ImageView music_iv_play, music_iv_cover;
    private TextView music_tv_title, music_tv_time;
    private ProgressBar music_pb_music;

    private TextView tv_speed;

    private void loadItem() {
        FrameLayout itemInfo = (FrameLayout) View.inflate(this, R.layout.plugin_car_info, null);
        item_info.addView(itemInfo, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        tv_speed = (TextView) itemInfo.findViewById(R.id.tv_speed);


        View.OnClickListener musicclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.ll_prew: {
                        MusicPlugin.self().pre();
                        break;
                    }
                    case R.id.iv_play: {
                        MusicPlugin.self().playOrPause();
                        break;
                    }
                    case R.id.ll_next: {
                        MusicPlugin.self().next();
                        break;
                    }
                }
            }
        };

        RelativeLayout musicView = (RelativeLayout) View.inflate(this, R.layout.plugin_music_launcher, null);
        item_music.addView(musicView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        music_iv_play = (ImageView) musicView.findViewById(R.id.iv_play);
        music_tv_title = (TextView) musicView.findViewById(R.id.tv_title);
        music_iv_cover = (ImageView) musicView.findViewById(R.id.iv_cover);
        music_tv_time = (TextView) musicView.findViewById(R.id.tv_time);
        music_pb_music = (ProgressBar) musicView.findViewById(R.id.pb_music);

        musicView.findViewById(R.id.iv_play).setOnClickListener(musicclick);
        musicView.findViewById(R.id.ll_prew).setOnClickListener(musicclick);
        musicView.findViewById(R.id.ll_next).setOnClickListener(musicclick);

        //高德地图的界面
        View.OnClickListener amapclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.rl_base: {
                        Intent appIntent = LauncherActivity.this.getPackageManager().getLaunchIntentForPackage(AMAP_PACKAGE);
                        if (appIntent == null) {
                            Toast.makeText(LauncherActivity.this, "没有安装高德地图", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        LauncherActivity.this.startActivity(appIntent);
                        break;
                    }
                    case R.id.btn_go_home: {
                        if (!AppUtil.isInstall(LauncherActivity.this, AMAP_PACKAGE)) {
                            Toast.makeText(LauncherActivity.this, "没有安装高德地图", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        AMapCarPlugin.self().getHome();
                        break;
                    }
                    case R.id.btn_go_company: {
                        if (!AppUtil.isInstall(LauncherActivity.this, AMAP_PACKAGE)) {
                            Toast.makeText(LauncherActivity.this, "没有安装高德地图", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        AMapCarPlugin.self().getComp();
                        break;
                    }
                }
            }
        };

        RelativeLayout amapView = (RelativeLayout) View.inflate(this, R.layout.plugin_amap_launcher, null);
        amapView.findViewById(R.id.rl_base).setOnClickListener(amapclick);
        amapView.findViewById(R.id.btn_go_home).setOnClickListener(amapclick);
        amapView.findViewById(R.id.btn_go_company).setOnClickListener(amapclick);

        amapIcon = (ImageView) amapView.findViewById(R.id.iv_icon);
        amapController = amapView.findViewById(R.id.ll_controller);
        amapnavi = (LinearLayout) amapView.findViewById(R.id.ll_navi);
        amapdis = (TextView) amapView.findViewById(R.id.tv_dis);
        amaproad = (TextView) amapView.findViewById(R.id.tv_road);
        amapmsg = (TextView) amapView.findViewById(R.id.tv_msg);

        item_amap.addView(amapView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void refreshNaviInfo(NaviInfo naviBean) {
        Log.e(TAG, "refreshNaviInfo:" + naviBean);
        switch (naviBean.getType()) {
            case NaviInfo.TYPE_STATE: {
                if (amapController != null) {
                    if (naviBean.getState() == 8 || naviBean.getState() == 10) {
                        amapController.setVisibility(View.GONE);
                        amapnavi.setVisibility(View.VISIBLE);
                    } else if (naviBean.getState() == 9 || naviBean.getState() == 11) {
                        amapController.setVisibility(View.VISIBLE);
                        amapnavi.setVisibility(View.GONE);
                        amapIcon.setImageResource(R.mipmap.ic_amap);
                    }
                }
                break;
            }
            case NaviInfo.TYPE_NAVI: {
                if (amapIcon != null && naviBean.getIcon() - 1 >= 0 && naviBean.getIcon() - 1 < ICONS.length) {
                    amapIcon.setImageResource(ICONS[naviBean.getIcon() - 1]);
                }
                if (amapdis != null && naviBean.getDis() > -1) {
                    if (naviBean.getDis() < 10) {
                        amapdis.setText("现在");
                    } else {
                        if (naviBean.getDis() > 1000) {
                            String msg = naviBean.getDis() / 1000 + "公里后";
                            amapdis.setText(msg);
                        } else {
                            String msg = naviBean.getDis() + "米后";
                            amapdis.setText(msg);
                        }

                    }
                }
                if (amaproad != null && CommonUtil.isNotNull(naviBean.getWroad())) {
                    amaproad.setText(naviBean.getWroad());
                }
                if (amapmsg != null && naviBean.getRemainTime() > -1 && naviBean.getRemainDis() > -1) {
                    if (naviBean.getRemainTime() == 0 || naviBean.getRemainDis() == 0) {
                        amapmsg.setText("到达");
                    } else {
                        String msg = "剩余" + new BigDecimal(naviBean.getRemainDis() / 1000f).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue() + "公里  " +
                                naviBean.getRemainTime() / 60 + "分钟";
                        amapmsg.setText(msg);
                    }
                }
                break;
            }
        }
    }

    public void refreshInfo(final String title, final String artist) {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                if (music_tv_title != null) {
                    if (CommonUtil.isNotNull(title)) {
                        music_tv_title.setText(title);
                    } else {
                        music_tv_title.setText("标题");
                    }
                }
            }
        });
    }

    public void refreshProgress(final int curr_time, final int total_time) {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                if (music_pb_music != null && curr_time > 0 && total_time > 0) {
                    music_pb_music.setProgress(curr_time);
                    music_pb_music.setMax(total_time);
                }

                if (music_tv_time != null) {
                    int tt = total_time / 1000;
                    int cc = curr_time / 1000;
                    music_tv_time.setText(cc + ":" + tt);
                }
            }
        });
    }

    public void refreshCover(final Bitmap cover) {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                if (cover != null) {
                    music_iv_cover.setImageBitmap(cover);
                } else {

                }
            }
        });
    }

    public void refreshState(final boolean run) {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                if (music_iv_play != null) {
                    if (run) {
                        music_iv_play.setImageResource(R.mipmap.ic_pause);
                    } else {
                        music_iv_play.setImageResource(R.mipmap.ic_play);
                    }
                }
            }
        });
    }

    @Override
    public void connect(boolean success) {
        if (success) {
            Toast.makeText(getApplication(), "方控连接成功", Toast.LENGTH_LONG).show();
            iv_fangkong_state.setImageResource(R.mipmap.fanglong_connect);
        } else {
            iv_fangkong_state.setImageResource(R.mipmap.fanglong_disconnect);
        }
        System.out.println("连接状态:" + success);
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
