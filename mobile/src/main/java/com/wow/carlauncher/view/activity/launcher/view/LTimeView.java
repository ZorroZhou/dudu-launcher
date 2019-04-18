package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.LunarUtil;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.view.base.BaseEBusView;
import com.wow.frame.util.DateUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;

import java.util.Calendar;
import java.util.Date;

import static com.wow.carlauncher.common.CommonData.DAY_MILL;
import static com.wow.carlauncher.common.CommonData.MINUTE_MILL;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LTimeView extends BaseEBusView {


    public LTimeView(@NonNull Context context) {
        super(context);
    }

    public LTimeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_time;
    }

    @Override
    protected void initView() {
        onEvent(null);
    }

    @ViewInject(R.id.tv_shijian)
    private TextView tv_shijian;

    @ViewInject(R.id.tv_week)
    private TextView tv_week;

    @ViewInject(R.id.tv_day)
    private TextView tv_day;

    @ViewInject(R.id.tv_lunar)
    private TextView tv_lunar;

    private long cur_min = 0L;
    private long cur_day = 0L;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MTimeSecondEvent event) {
        long time = System.currentTimeMillis();
        long time1 = time / MINUTE_MILL;
        if (time1 != cur_min) {
            cur_min = time1;
            Date d = new Date();
            if (tv_shijian != null) {
                tv_shijian.setText(DateUtil.dateToString(d, "HH:mm"));
            }
            time1 = time / DAY_MILL;
            if (time1 != cur_day) {
                cur_day = time1;
                if (tv_day != null) {
                    tv_day.setText(DateUtil.dateToString(d, "yyyy年MM月dd日"));
                }
                if (tv_week != null) {
                    tv_week.setText(DateUtil.getWeekOfDate(d));
                }
                if (tv_lunar != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(d);
                    tv_lunar.setText(new LunarUtil(calendar).toString());
                }
            }
        }

    }
}
