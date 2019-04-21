package com.wow.carlauncher.view.activity;

import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.DateUtil;
import com.wow.carlauncher.view.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 10124 on 2017/11/4.
 */

public class LockActivity extends BaseActivity {
    @ViewInject(R.id.tv_time)
    private TextView tv_time;

    @ViewInject(R.id.ll_bg)
    private LinearLayout ll_bg;

    private Timer timer;

    @Override
    public void init() {
        setContent(R.layout.activity_lock);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void initView() {
        hideTitle();
        ll_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                x.task().autoPost(new Runnable() {
                    @Override
                    public void run() {
                        setTime();
                    }
                });
            }
        }, 60000 - System.currentTimeMillis() % 60000, 60000);
        setTime();
    }

    private void setTime() {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                Date d = new Date();
                String time = DateUtil.dateToString(d, "HH:mm");
                LockActivity.this.tv_time.setText(time);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
