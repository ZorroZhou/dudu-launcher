package com.wow.carlauncher.view.activity.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.WeatherIconUtil;
import com.wow.carlauncher.ex.manage.time.event.MTimeMinuteEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.amapWebservice.WebService;
import com.wow.carlauncher.repertory.amapWebservice.res.WeatherRes;
import com.wow.carlauncher.ex.manage.time.event.MTime30MinuteEvent;
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

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LWeatherView extends BaseEBusView {

    @ViewInject(R.id.tv_tianqi)
    private TextView tv_tianqi;

    @ViewInject(R.id.tv_wendu1)
    private TextView tv_wendu1;


    @ViewInject(R.id.tv_kongqq)
    private TextView tv_kongqq;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.iv_tianqi)
    private ImageView iv_tianqi;

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

    private void refreshWeather() {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                if (Strings.isNullOrEmpty(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY))) {
                    tv_title.setText("点击设置城市");
                } else {
                    tv_title.setText(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY));
                }
                tv_tianqi.setText("");
                tv_wendu1.setText("");

                if (!Strings.isNullOrEmpty(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY))) {
                    WebService.getWeatherInfo(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY), new WebService.CommonCallback<WeatherRes>() {
                        @Override
                        public void callback(WeatherRes res) {
                            System.out.println(res);
                            if (Integer.valueOf(1).equals(res.getStatus()) && res.getLives().size() > 0) {
                                iv_tianqi.setImageResource(WeatherIconUtil.getWeatherResId(res.getLives().get(0).getWeather()));

                                tv_wendu1.setText(res.getLives().get(0).getTemperature() + "℃");
                                tv_tianqi.setText(res.getLives().get(0).getWeather());
                                // String feng;
                                String wd = res.getLives().get(0).getWinddirection();
                                Log.e(TAG, "callback: " + wd);
//                                if (wd.equals("东北") || wd.equals("东") || wd.equals("东南") || wd.equals("南") || wd.equals("西南") || wd.equals("西") || wd.equals("西北") || wd.equals("北")) {
//                                    feng = wd + "风: ";
//                                } else {
//                                    feng = "风力: ";
//                                }
                                //tv_feng.setText(feng + res.getLives().get(0).getWindpower() + "级");
                                tv_kongqq.setText("湿度: " + res.getLives().get(0).getHumidity());
                                //tv_tianqi2.setText(feng + res.getLives().get(0).getWindpower() + "级  空气湿度:" + res.getLives().get(0).getHumidity());
                            } else {
                                //tv_tianqi.setText("请检查网络");
                            }
                        }
                    });
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LEventCityRefresh event) {
        refreshWeather();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MTimeMinuteEvent event) {
        refreshWeather();
    }


}
