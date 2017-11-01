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

public class TimePopupView extends LinearLayout {
    private static final String TAG = "TimePluginView";

    private LayoutInflater inflater;

    private TextView time;

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

    public TimePopupView(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.plugin_time_popup, null);
        this.addView(linearLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        time = findViewById(R.id.time);
    }

    private void setTime() {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                Date d = new Date();
                String time = DateUtil.dateToString(d, "HH:mm");
                TimePopupView.this.time.setText(time);
            }
        });
    }
}