package com.wow.carlauncher.view.activity.driving.time;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.DateUtil;
import com.wow.carlauncher.ex.manage.time.event.TMEventSecond;
import com.wow.carlauncher.view.base.BaseView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import butterknife.BindView;

import static com.wow.carlauncher.common.CommonData.MINUTE_MILL;

public class DrivingTimeView extends BaseView {
    public DrivingTimeView(@NonNull Context context) {
        super(context);
    }

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_date)
    TextView tv_date;

    @Override
    protected int getContent() {
        return R.layout.content_driving_time;
    }

    private long cur_min = 0L;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final TMEventSecond event) {
        long time1 = System.currentTimeMillis() / MINUTE_MILL;
        if (time1 != cur_min) {
            cur_min = time1;
            Date d = new Date();
            String time = DateUtil.dateToString(d, "HH:mm");
            if (time.startsWith("0")) {
                time = time.substring(1);
            }
            tv_time.setText(time);
            String date = DateUtil.dateToString(d, "MM月dd日 ");
            if (date.startsWith("0")) {
                date = date.substring(1);
            }
            tv_date.setText(date);
        }
    }
}
