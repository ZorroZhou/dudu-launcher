package com.wow.carlauncher.view.activity.driving.coolBlack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.manage.trip.TripManage;
import com.wow.carlauncher.view.base.BaseEBusView;
import com.wow.frame.util.DateUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;
import java.util.Locale;

/**
 * Created by 10124 on 2018/5/11.
 */

public class CoolBlackView extends BaseEBusView {
    public CoolBlackView(@NonNull Context context) {
        super(context);
        addContent(R.layout.content_driving_cool_black);
    }

    public CoolBlackView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addContent(R.layout.content_driving_cool_black);
    }

    @ViewInject(R.id.tv_time)
    private TextView tv_time;

    @ViewInject(R.id.tv_date)
    private TextView tv_date;

    @ViewInject(R.id.tv_trip_time)
    private TextView tv_trip_time;

    @ViewInject(R.id.tv_driver_distance)
    private TextView tv_driver_distance;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final MTimeSecondEvent event) {
        this.tv_date.setText(DateUtil.dateToString(new Date(), "yyyy-MM-dd"));
        this.tv_time.setText(DateUtil.dateToString(new Date(), "HH:mm:ss"));
        if (TripManage.self().isTripStart()) {
            this.tv_trip_time.setText(DateUtil.formatDuring(System.currentTimeMillis() - TripManage.self().getTropStartTime()));
            this.tv_driver_distance.setText(String.format(Locale.CHINA, "%.1f", (double) TripManage.self().getTropMileage() / 1000d));
        } else {
            this.tv_trip_time.setText("00:00:00");
            this.tv_driver_distance.setText("0");
        }
    }
}

