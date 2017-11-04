package com.wow.carlauncher.plugin.time;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.WeatherIconUtil;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.DateUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.time.event.PEventTimeClock;
import com.wow.carlauncher.plugin.time.event.PEventTimeWeather;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.x;

import java.util.Date;

/**
 * Created by 10124 on 2017/10/28.
 */

public class TimeLauncherView extends LinearLayout {
    private static final String TAG = "TimePluginView";

    private LayoutInflater inflater;

    private TextView time, date, week, tv_tianqi, tv_local;
    private ImageView iv_tianqi;

    @Subscribe
    public void onEventMainThread(PEventTimeWeather event) {
        if (CommonUtil.isNotNull(event.location)) {
            tv_local.setText(event.location);
        }

        if (event.weather != null) {
            tv_local.setText(event.location);
            tv_tianqi.setText(event.weather.getWeather() + "  " + event.weather.getTemperature() + "℃");
            iv_tianqi.setImageResource(WeatherIconUtil.getWeatherResId(event.weather.getWeather()));
        }
    }

    @Subscribe
    public void onEventMainThread(PEventTimeClock event) {
        setTime();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
        setTime();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    public TimeLauncherView(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.plugin_time_lanncher, null);
        this.addView(linearLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        week = findViewById(R.id.week);
        tv_tianqi = findViewById(R.id.tv_tianqi);
        tv_local = findViewById(R.id.tv_local);

        iv_tianqi = findViewById(R.id.iv_tianqi);

        linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_TIME_PLUGIN_OPEN_APP);
                if (CommonUtil.isNotNull(selectapp)) {
                    Intent appIntent = getContext().getPackageManager().getLaunchIntentForPackage(selectapp);
                    if (appIntent != null) {
                        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(appIntent);
                        return;
                    }
                }
                Toast.makeText(getContext(), "没有选择APP", Toast.LENGTH_SHORT);
            }
        });
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
                TimeLauncherView.this.time.setText(time);
                TimeLauncherView.this.date.setText(date);
                TimeLauncherView.this.week.setText(week);
            }
        });
    }
}