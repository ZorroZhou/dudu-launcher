package com.wow.carlauncher.activity.driving;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.activity.driving.view.RevAndWaterTempView;
import com.wow.carlauncher.activity.driving.view.SpeedAndOilView;
import com.wow.carlauncher.common.base.BaseActivity;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.frame.util.DateUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 10124 on 2018/4/25.
 */

public class DrivingActivity extends BaseActivity {


    @ViewInject(R.id.rwtview)
    private RevAndWaterTempView rwtview;

    @ViewInject(R.id.soview)
    private SpeedAndOilView soview;

    @ViewInject(R.id.tv_time)
    private TextView tv_time;

    @Override
    public void init() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContent(R.layout.activity_driving);

        EventBus.getDefault().register(this);
    }

    boolean run = true;
    int rev = 10;
    int speed = 10;

    @Override
    public void initView() {
        hideTitle();

        x.task().run(new Runnable() {
            @Override
            public void run() {
                while (run) {


                    rev = rev + new Random().nextInt(300);
                    if (rev > 10000) {
                        rev = 100;
                    }

                    speed = speed + new Random().nextInt(5);
                    if (speed > 250) {
                        speed = 100;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rwtview.setRev(rev);
                            rwtview.setWaterTemp(60);

                            soview.setSpeed(speed);
                            soview.setOil(90);
                        }
                    });
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        run = false;
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final MTimeSecondEvent event) {
        this.tv_time.setText(DateUtil.dateToString(new Date(), "HH:mm:ss"));
    }

}
