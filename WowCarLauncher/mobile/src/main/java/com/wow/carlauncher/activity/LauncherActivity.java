package com.wow.carlauncher.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.plugin.amap.WebService;
import com.wow.carlauncher.plugin.amap.res.WeatherRes;
import com.wow.carlauncher.popupWindow.PopupWin;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_set: {
                startActivity(new Intent(this, SetActivity.class));
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
