package com.wow.carlauncher.plugin.gps;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.view.DashboardView;
import com.wow.carlauncher.plugin.BasePlugin;
import com.wow.carlauncher.plugin.PluginManage;

import static com.wow.carlauncher.common.CommonData.TAG;

public class GpsPlugin extends BasePlugin implements AMapLocationListener {
    private AMapLocationClient locationClient;
    private DashboardView dashboardView;
    private TextView tv_haiba, tv_fangxiang, tv_noinfo;
    private View rl_speed;

    public GpsPlugin(Context context, PluginManage pluginManage) {
        super(context, pluginManage);

        locationClient = new AMapLocationClient(context);

        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        locationClient.setLocationListener(this);
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setInterval(1000);
        locationClient.setLocationOption(locationOption);
        locationClient.startLocation();
    }


    public ViewGroup initLauncherView() {
        ViewGroup launcherView = (ViewGroup) View.inflate(context, R.layout.plugin_gps_launcher, null);
        dashboardView = (DashboardView) launcherView.findViewById(R.id.speed);
        tv_haiba = (TextView) launcherView.findViewById(R.id.tv_haiba);
        tv_fangxiang = (TextView) launcherView.findViewById(R.id.tv_fangxiang);
        rl_speed = launcherView.findViewById(R.id.rl_speed);
        tv_noinfo = (TextView) launcherView.findViewById(R.id.tv_noinfo);
        return launcherView;
    }

    @Override
    public ViewGroup initPopupView() {
        return null;
    }

    @Override
    public void destroy() {
        super.destroy();
        locationClient.stopLocation();
        locationClient.unRegisterLocationListener(this);
        locationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode() == AMapLocation.LOCATION_SUCCESS) {
            Log.e(TAG, "onLocationChanged: !!");
            tv_noinfo.setVisibility(View.GONE);

            if (tv_haiba != null) {
                tv_haiba.setText("海拔:" + aMapLocation.getAltitude() + " 米");
            }
            //取值范围：【0，360】，其中0度表示正北方向，90度表示正东，180度表示正南，270度表示正西
            if (tv_fangxiang != null) {
                tv_fangxiang.setText("方向:" + getJiaodu(aMapLocation.getBearing()) + "");
            }
            if (dashboardView != null) {
                dashboardView.setVelocity((int) aMapLocation.getSpeed() / 1000);
            }
        }
    }

    private String getJiaodu(float b) {
        if ((b < 360 && b > 337.5) || (b > 0 && b < 22.5)) {
            return "北";
        } else if (b > 22.5 || b < 67.5) {
            return "东北";
        } else if (b > 67.5 || b < 112.5) {
            return "东";
        } else if (b > 112.5 || b < 157.5) {
            return "东南";
        } else if (b > 157.5 || b < 202.5) {
            return "南";
        } else if (b > 202.5 || b < 247.5) {
            return "西南";
        } else if (b > 247.5 || b < 292.5) {
            return "西";
        } else {
            return "西北";
        }
    }
}
