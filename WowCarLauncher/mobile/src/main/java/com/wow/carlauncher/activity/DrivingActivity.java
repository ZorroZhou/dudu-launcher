package com.wow.carlauncher.activity;

import android.widget.ImageView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.base.BaseActivity;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.frame.util.DateUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 10124 on 2018/4/25.
 */

public class DrivingActivity extends BaseActivity {

    @ViewInject(R.id.driving_rev_bg_cursor)
    private ImageView driving_rev_bg_cursor;

    @Override
    public void init() {
        setContent(R.layout.activity_driving);

    }

    @Override
    public void initView() {
        hideTitle();

        EventBus.getDefault().register(this);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        i = i + 0.01F;
                        driving_rev_bg_cursor.setPivotX(driving_rev_bg_cursor.getWidth() / 2);
                        driving_rev_bg_cursor.setPivotY(driving_rev_bg_cursor.getHeight() / 2);//支点在图片中心
                        driving_rev_bg_cursor.setRotation(i);
                    }
                });
            }
        }, 1000, 10);
    }

    float i = 0;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final MTimeSecondEvent event) {

    }

}
