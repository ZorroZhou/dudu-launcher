package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.WeatherIconTemp;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.location.event.MNewLocationEvent;
import com.wow.carlauncher.ex.manage.time.event.MTimeMinuteEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.web.amap.AMapWebService;
import com.wow.carlauncher.repertory.web.amap.res.WeatherRes;
import com.wow.carlauncher.view.activity.launcher.event.LCityRefreshEvent;
import com.wow.carlauncher.view.base.BaseEXView;
import com.wow.carlauncher.view.dialog.CityDialog;
import com.wow.carlauncher.view.event.EventNetStateChange;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.BLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.WHITE;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LWeatherView extends BaseEXView {

    public LWeatherView(@NonNull Context context) {
        super(context);
    }

    public LWeatherView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_empty;
    }

    private int layoutId;

    @Override
    public void changedTheme(ThemeManage manage) {
        if (manage.getTheme() == BLACK || manage.getTheme() == WHITE) {
            if (layoutId != R.layout.content_l_weather_b) {
                layoutId = R.layout.content_l_weather_b;
                fl_base.removeAllViews();
                View view = View.inflate(getContext(), layoutId, null);
                fl_base.addView(view, MATCH_PARENT, MATCH_PARENT);
                x.view().inject(this, view);
                refreshShow();
            }

            rl_base.setBackgroundResource(manage.getCurrentThemeRes(R.drawable.n_l_item1_bg));

            tv_tianqi.setTextColor(manage.getCurrentThemeColor(R.color.l_text3));
            tv_wendu1.setTextColor(manage.getCurrentThemeColor(R.color.l_text2));
            tv_title.setTextColor(manage.getCurrentThemeColor(R.color.l_text2));

            line1.setBackgroundResource(manage.getCurrentThemeRes(R.drawable.n_line2));

            manage.setTextViewsColor(this, new int[]{
                    R.id.tv_text2,
                    R.id.tv_text3,
                    R.id.tv_text4
            }, R.color.l_text3);

            manage.setTextViewsColor(this, new int[]{
                    R.id.tv_kqsd,
                    R.id.tv_fl,
                    R.id.tv_fx
            }, R.color.l_text4);
        } else {
            layoutId = R.layout.content_l_weather_cb;
            fl_base.removeAllViews();
            View view = View.inflate(getContext(), layoutId, null);
            fl_base.addView(view, MATCH_PARENT, MATCH_PARENT);
            x.view().inject(this, view);

            refreshShow();
        }
    }

    @Override
    protected void initView() {

    }

    private void refreshShow() {
        x.task().autoPost(() -> {
            if (cityWeather != null) {
                iv_tianqi.setImageResource(WeatherIconTemp.getWeatherResId(cityWeather.getWeather()));
                tv_tianqi.setText(cityWeather.getWeather());
                tv_wendu1.setText(String.valueOf(cityWeather.getTemperature() + "°"));
                tv_kqsd.setText(String.valueOf(cityWeather.getHumidity()));
                tv_fl.setText(String.valueOf(cityWeather.getWindpower()));
                tv_fx.setText(String.valueOf(cityWeather.getWinddirection() + "风"));
            }
            if (lastLocation == null || CommonUtil.isNull(lastLocation.getAdCode())) {
                if (CommonUtil.isNull(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_DISTRICT)) || CommonUtil.isNull(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_SHI))) {
                    tv_title.setText("点击设置城市");
                } else {
                    String msg = SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_SHI) + "-" + SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_DISTRICT);
                    tv_title.setText(msg);
                }
            } else {
                String msg = lastLocation.getCity();
                if (CommonUtil.isNotNull(lastLocation.getDistrict())) {
                    msg = msg + "-" + lastLocation.getDistrict();
                }
                tv_title.setText(msg);
            }
        });
    }

    @ViewInject(R.id.fl_base)
    private FrameLayout fl_base;

    @ViewInject(R.id.rl_base)
    private View rl_base;

    @ViewInject(R.id.line1)
    private View line1;

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


    @Event(value = {R.id.tv_title, R.id.fl_base})
    private void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_title: {
                if (CommonUtil.isNull(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_DISTRICT))) {
                    final CityDialog cityDialog = new CityDialog(getContext());
                    cityDialog.setOkclickListener(dialog -> {
                        if (CommonUtil.isNotNull(cityDialog.getCityName()) && CommonUtil.isNotNull(cityDialog.getDistrictName())) {
                            SharedPreUtil.saveSharedPreString(CommonData.SDATA_WEATHER_DISTRICT, cityDialog.getDistrictName());
                            SharedPreUtil.saveSharedPreString(CommonData.SDATA_WEATHER_SHI, cityDialog.getCityName());
                            cityDialog.dismiss();
                            EventBus.getDefault().post(new LCityRefreshEvent());
                            return true;
                        } else {
                            ToastManage.self().show("请选择城市");
                            return false;
                        }
                    });
                    cityDialog.show();
                }
                break;
            }
        }
    }

    private long lastUpdate;
    private boolean runninng = false;
    private WeatherRes.CityWeather cityWeather;

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
        x.task().autoPost(() -> {
            String chengshi = "";
            if (lastLocation == null || CommonUtil.isNull(lastLocation.getAdCode())) {
                if (!CommonUtil.isNull(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_DISTRICT))) {
                    chengshi = SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_DISTRICT);
                }
            } else {
                chengshi = lastLocation.getAdCode();
            }
            //如果定位失败,则使用设置的地理位置
            if (CommonUtil.isNotNull(chengshi)) {
                AMapWebService.getWeatherInfo(chengshi, new AMapWebService.CommonCallback<WeatherRes>() {
                    @Override
                    public void callback(WeatherRes res) {
                        runninng = false;
                        if (Integer.valueOf(1).equals(res.getStatus()) && res.getLives().size() > 0) {
                            lastUpdate = System.currentTimeMillis();
                            WeatherRes.CityWeather cityWeather = res.getLives().get(0);
                            if (cityWeather != null) {
                                LWeatherView.this.cityWeather = cityWeather;
                                refreshShow();
                            }
                        }
                    }
                });
            } else {
                runninng = false;
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(EventNetStateChange event) {
        refreshWeather(true);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(LCityRefreshEvent event) {
        refreshShow();
        refreshWeather(true);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(MTimeMinuteEvent event) {
        refreshWeather(false);
    }

    private MNewLocationEvent lastLocation;

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(MNewLocationEvent event) {
        boolean frist = false;
        if (this.lastLocation == null) {
            frist = true;
        }
        lastLocation = event;
        refreshShow();
        refreshWeather(frist);
    }
}
