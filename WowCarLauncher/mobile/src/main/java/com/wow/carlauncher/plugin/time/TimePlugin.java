package com.wow.carlauncher.plugin.time;

import android.content.Context;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.wow.carlauncher.plugin.IPlugin;
import com.wow.carlauncher.plugin.amap.WebService;
import com.wow.carlauncher.plugin.amap.res.WeatherRes;
import com.wow.carlauncher.plugin.PopupViewProportion;
import com.wow.carlauncher.plugin.time.event.PEventTimeClock;
import com.wow.carlauncher.plugin.time.event.PEventTimeWeather;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 10124 on 2017/10/26.
 */

public class TimePlugin implements IPlugin, AMapLocationListener {
    protected Context context;

    private TimeLauncherView timePluginView;
    private TimePopupView timePopupView;

    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;

    public TimePlugin(Context context) {
        this.context = context;

        mlocationClient = new AMapLocationClient(context);

        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(1000 * 60 * 20);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();

        startUpdateTime();
    }

    @Override
    public void destroy() {
        this.context = null;
        stopUpdateTime();
    }

    private Timer timer;

    private void startUpdateTime() {
        stopUpdateTime();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                EventBus.getDefault().post(new PEventTimeClock());
            }
        }, (60 * 1000) - System.currentTimeMillis() % (60 * 1000), (60 * 1000));
        EventBus.getDefault().post(new PEventTimeClock());
    }

    private void stopUpdateTime() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public View getLauncherView() {
        if (timePluginView == null) {
            timePluginView = new TimeLauncherView(context);
        }
        return timePluginView;
    }

    @Override
    public PopupViewProportion getPopupViewProportion() {
        return new PopupViewProportion(1, 1.8f);
    }

    @Override
    public View getPopupView() {
        if (timePopupView == null) {
            timePopupView = new TimePopupView(context);
        }
        return timePopupView;
    }

    @Override
    public void onLocationChanged(final AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            EventBus.getDefault().post(new PEventTimeWeather(aMapLocation.getDistrict(), null));
            WebService.getWeatherInfo(aMapLocation.getAdCode(), new WebService.CommonCallback<WeatherRes>() {
                @Override
                public void callback(WeatherRes res) {
                    if (Integer.valueOf(1).equals(res.getStatus()) && res.getLives().size() > 0) {
                        EventBus.getDefault().post(new PEventTimeWeather(aMapLocation.getDistrict(), res.getLives().get(0)));
                    }
                }
            });
        }
    }
}
