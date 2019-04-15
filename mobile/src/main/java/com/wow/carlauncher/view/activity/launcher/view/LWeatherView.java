package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.WeatherIconUtil;
import com.wow.carlauncher.ex.manage.location.event.MNewLocationEvent;
import com.wow.carlauncher.ex.manage.time.event.MTimeMinuteEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.amapWebservice.WebService;
import com.wow.carlauncher.repertory.amapWebservice.res.WeatherRes;
import com.wow.carlauncher.view.activity.launcher.event.LEventCityRefresh;
import com.wow.carlauncher.view.base.BaseDialog2;
import com.wow.carlauncher.view.base.BaseEBusView;
import com.wow.carlauncher.view.dialog.CityDialog;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LWeatherView extends BaseEBusView {

    @ViewInject(R.id.tv_tianqi)
    private TextView tv_tianqi;

    @ViewInject(R.id.tv_wendu1)
    private TextView tv_wendu1;


    @ViewInject(R.id.tv_kqsd)
    private TextView tv_kqsd;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.iv_tianqi)
    private ImageView iv_tianqi;

    @ViewInject(R.id.tv_fl)
    private TextView tv_fl;
    @ViewInject(R.id.tv_fx)
    private TextView tv_fx;


    public LWeatherView(@NonNull Context context) {
        super(context);
        initView();
    }

    public LWeatherView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Event(value = {R.id.tv_title})
    private void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_title: {
                final CityDialog cityDialog = new CityDialog(getContext());
                cityDialog.setOkclickListener(new BaseDialog2.OnBtnClickListener() {
                    @Override
                    public boolean onClick(BaseDialog2 dialog) {
                        if (CommonUtil.isNotNull(cityDialog.getmCurrentDistrictName())) {
                            SharedPreUtil.saveSharedPreString(CommonData.SDATA_WEATHER_CITY, cityDialog.getmCurrentDistrictName());
                            cityDialog.dismiss();
                            EventBus.getDefault().post(new LEventCityRefresh());
                            return true;
                        } else {
                            ToastManage.self().show("请选择城市");
                            return false;
                        }
                    }
                });
                cityDialog.show();
                break;
            }
        }
    }

    private void initView() {
        addContent(R.layout.content_l_weather);
    }

    private String city, adcode;

    private long lastUpdate;
    private boolean runninng = false;

    private void refreshWeather(boolean qiangzhi) {
        if (!qiangzhi) {
            if (System.currentTimeMillis() - lastUpdate < 60 * 1000 * 60) {
                return;
            }
        }
        if (runninng) {
            return;
        }
        runninng = true;
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                String chengshi = "";
                //如果定位失败,则使用设置的地理位置
                if (Strings.isNullOrEmpty(adcode)) {
                    if (Strings.isNullOrEmpty(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY))) {
                        tv_title.setText("点击设置城市");
                    } else {
                        tv_title.setText(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY));
                        chengshi = SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY);
                    }
                } else {
                    chengshi = adcode;
                    tv_title.setText(city);
                }

                if (!Strings.isNullOrEmpty(chengshi)) {
                    WebService.getWeatherInfo(chengshi, new WebService.CommonCallback<WeatherRes>() {
                        @Override
                        public void callback(WeatherRes res) {
                            runninng = false;
                            if (Integer.valueOf(1).equals(res.getStatus()) && res.getLives().size() > 0) {
                                lastUpdate = System.currentTimeMillis();
                                WeatherRes.CityWeather cityWeather = res.getLives().get(0);
                                if (cityWeather != null) {
                                    iv_tianqi.setImageResource(WeatherIconUtil.getWeatherResId(cityWeather.getWeather()));
                                    tv_tianqi.setText(cityWeather.getWeather());
                                    tv_wendu1.setText(String.valueOf(cityWeather.getTemperature() + "℃"));
                                    tv_kqsd.setText(String.valueOf(cityWeather.getHumidity()));
                                    tv_fl.setText(String.valueOf(cityWeather.getWindpower()));
                                    tv_fx.setText(String.valueOf(cityWeather.getWinddirection() + "风"));
                                }
                            }
                        }
                    });
                } else {
                    runninng = false;
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LEventCityRefresh event) {
        refreshWeather(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MTimeMinuteEvent event) {
        refreshWeather(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MNewLocationEvent event) {
        boolean frist = false;
        if (this.adcode == null) {
            frist = true;
        }
        this.adcode = event.getAdCode();
        this.city = event.getCity();
        refreshWeather(frist);
    }
}
