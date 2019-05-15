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
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.WeatherIconTemp;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.location.LMEventNewLocation;
import com.wow.carlauncher.ex.manage.skin.SkinManage;
import com.wow.carlauncher.ex.manage.skin.SkinUtil;
import com.wow.carlauncher.ex.manage.time.event.TMEventMinute;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.web.amap.AMapWebService;
import com.wow.carlauncher.repertory.web.amap.res.WeatherRes;
import com.wow.carlauncher.view.activity.launcher.BaseThemeView;
import com.wow.carlauncher.view.activity.launcher.event.LCityRefreshEvent;
import com.wow.carlauncher.view.dialog.CityDialog;
import com.wow.carlauncher.view.event.EventNetStateChange;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LWeatherView extends BaseThemeView {

    public LWeatherView(@NonNull Context context) {
        super(context);
    }

    public LWeatherView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_weather;
    }

    private int layoutId = -1;

    @Override
    public void changedSkin(SkinManage manage) {
        if (fl_base == null) {
            return;
        }
        int nowLayoutId = SkinUtil.analysisWeatherLayout(manage.getString(R.string.theme_item_weather_layout));
        if (layoutId != nowLayoutId) {
            layoutId = nowLayoutId;
            fl_base.removeAllViews();
            View view = View.inflate(getContext(), layoutId, null);
            fl_base.addView(view, MATCH_PARENT, MATCH_PARENT);
            ButterKnife.bind(this, view);
            refreshShow();
        }
    }

    @Override
    protected void initView() {
        fl_base = findViewById(R.id.fl_base);
        LogEx.d(this, "initView: ");
    }

    private void refreshShow() {
        TaskExecutor.self().autoPost(() -> {
            if (iv_tianqi == null ||
                    tv_kqsd == null ||
                    tv_tianqi == null ||
                    tv_wendu1 == null ||
                    tv_title == null ||
                    tv_fl == null ||
                    tv_fx == null) {
                return;
            }

            if (cityWeather != null) {
                iv_tianqi.setImageDrawable(SkinManage.self().getDrawable(WeatherIconTemp.getWeatherResId(cityWeather.getWeather())));
                tv_tianqi.setText(cityWeather.getWeather());
                tv_wendu1.setText(String.valueOf(cityWeather.getTemperature() + "°"));
                tv_kqsd.setText(String.valueOf(cityWeather.getHumidity()));
                tv_fl.setText(String.valueOf(cityWeather.getWindpower()));
                tv_fx.setText(String.valueOf(cityWeather.getWinddirection() + "风"));
            }
            if (lastLocation == null || CommonUtil.isNull(lastLocation.getAdCode())) {
                if (CommonUtil.isNull(SharedPreUtil.getString(CommonData.SDATA_WEATHER_DISTRICT)) || CommonUtil.isNull(SharedPreUtil.getString(CommonData.SDATA_WEATHER_SHI))) {
                    tv_title.setText("点击设置城市");
                } else {
                    String msg = SharedPreUtil.getString(CommonData.SDATA_WEATHER_SHI) + "-" + SharedPreUtil.getString(CommonData.SDATA_WEATHER_DISTRICT);
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

    FrameLayout fl_base;

    @BindView(R.id.rl_base)
    @Nullable
    View rl_base;

    @BindView(R.id.tv_tianqi)
    @Nullable
    TextView tv_tianqi;

    @BindView(R.id.tv_wendu1)
    @Nullable
    TextView tv_wendu1;

    @BindView(R.id.tv_kqsd)
    @Nullable
    TextView tv_kqsd;

    @BindView(R.id.tv_title)
    @Nullable
    TextView tv_title;

    @BindView(R.id.iv_tianqi)
    @Nullable
    ImageView iv_tianqi;

    @BindView(R.id.tv_fl)
    @Nullable
    TextView tv_fl;

    @BindView(R.id.tv_fx)
    @Nullable
    TextView tv_fx;


    @OnClick(value = {R.id.tv_title, R.id.fl_base})
    @Optional
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_title: {
                if (CommonUtil.isNull(SharedPreUtil.getString(CommonData.SDATA_WEATHER_DISTRICT))) {
                    final CityDialog cityDialog = new CityDialog(getContext());
                    cityDialog.setOkclickListener(dialog -> {
                        if (CommonUtil.isNotNull(cityDialog.getCityName()) && CommonUtil.isNotNull(cityDialog.getDistrictName())) {
                            SharedPreUtil.saveString(CommonData.SDATA_WEATHER_DISTRICT, cityDialog.getDistrictName());
                            SharedPreUtil.saveString(CommonData.SDATA_WEATHER_SHI, cityDialog.getCityName());
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
        TaskExecutor.self().autoPost(() -> {
            String chengshi = "";
            if (lastLocation == null || CommonUtil.isNull(lastLocation.getAdCode())) {
                if (!CommonUtil.isNull(SharedPreUtil.getString(CommonData.SDATA_WEATHER_DISTRICT))) {
                    chengshi = SharedPreUtil.getString(CommonData.SDATA_WEATHER_DISTRICT);
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
                        if (res != null && Integer.valueOf(1).equals(res.getStatus()) && res.getLives().size() > 0) {
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
    public void onEvent(TMEventMinute event) {
        refreshWeather(false);
    }

    private LMEventNewLocation lastLocation;

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(LMEventNewLocation event) {
        boolean frist = false;
        if (this.lastLocation == null) {
            frist = true;
        } else {
            if (!CommonUtil.equals(lastLocation.getAdCode(), event.getAdCode())) {
                frist = true;
            }
        }
        if (frist) {
            lastLocation = event;
            refreshShow();
        }
        refreshWeather(frist);
    }
}
