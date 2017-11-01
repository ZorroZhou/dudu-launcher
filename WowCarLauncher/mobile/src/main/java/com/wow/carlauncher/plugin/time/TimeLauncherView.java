package com.wow.carlauncher.plugin.time;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.DateUtil;
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

    @Subscribe
    public void onEventMainThread(PEventTimeWeather event) {
        if (CommonUtil.isNotNull(event.location)) {
            tv_local.setText(event.location);
        }

        if (event.weather != null) {
            tv_local.setText(event.location);
            tv_tianqi.setText(event.weather.getWeather() + "  " + event.weather.getTemperature() + "℃");
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