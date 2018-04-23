package com.wow.carlauncher.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.WeatherIconUtil;
import com.wow.carlauncher.common.amapWebservice.WebService;
import com.wow.carlauncher.common.amapWebservice.res.WeatherRes;
import com.wow.carlauncher.activity.launcher.event.LauncherCityRefreshEvent;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LWeatherView extends FrameLayout {
    @ViewInject(R.id.iv_tianqi)
    private ImageView iv_tianqi;

    @ViewInject(R.id.tv_tianqi)
    private TextView tv_tianqi;

    @ViewInject(R.id.tv_wendu)
    private TextView tv_wendu;

    @ViewInject(R.id.tv_feng)
    private TextView tv_feng;

    @ViewInject(R.id.tv_shidu)
    private TextView tv_shidu;

    public LWeatherView(@NonNull Context context) {
        super(context);
        initView();
    }

    public LWeatherView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        RelativeLayout view = (RelativeLayout) View.inflate(getContext(), R.layout.plugin_weather_launcher, null);
        this.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        x.view().inject(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
        startTimer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
        stopTimer();
    }

    private Timer timer;
    private int sanshifenzhong = 1000 * 60 * 30;

    private void startTimer() {
        Log.e(TAG, "startTimer: ");
        stopTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshWeather();
            }
        }, sanshifenzhong - System.currentTimeMillis() % sanshifenzhong, sanshifenzhong);
        refreshWeather();
    }

    private void stopTimer() {
        if (timer != null) {
            Log.e(TAG, "stopTimer: ");
            timer.cancel();
            timer = null;
        }
    }

    private void refreshWeather() {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                if (CommonUtil.isNotNull(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY))) {
                    WebService.getWeatherInfo(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY), new WebService.CommonCallback<WeatherRes>() {
                        @Override
                        public void callback(WeatherRes res) {
                            System.out.println(res);
                            if (Integer.valueOf(1).equals(res.getStatus()) && res.getLives().size() > 0) {
                                iv_tianqi.setImageResource(WeatherIconUtil.getWeatherResId(res.getLives().get(0).getWeather()));

                                tv_wendu.setText(res.getLives().get(0).getTemperature() + "℃");
                                tv_tianqi.setText(res.getLives().get(0).getWeather());
                                String feng;
                                String wd = res.getLives().get(0).getWinddirection();
                                Log.e(TAG, "callback: " + wd);
                                if (wd.equals("东北") || wd.equals("东") || wd.equals("东南") || wd.equals("南") || wd.equals("西南") || wd.equals("西") || wd.equals("西北") || wd.equals("北")) {
                                    feng = wd + "风: ";
                                } else {
                                    feng = "风力: ";
                                }
                                tv_feng.setText(feng + res.getLives().get(0).getWindpower() + "级");
                                tv_shidu.setText("空气湿度: " + res.getLives().get(0).getHumidity());
                                //tv_tianqi2.setText(feng + res.getLives().get(0).getWindpower() + "级  空气湿度:" + res.getLives().get(0).getHumidity());
                            } else {
                                //tv_tianqi.setText("请检查网络");
                            }
                        }
                    });
                } else {
                    tv_wendu.setText("请预先设置城市");
                    tv_tianqi.setText("点击设置-时间和天气设置-天气定位进行设置");
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(LauncherCityRefreshEvent event) {
        refreshWeather();
    }
}
