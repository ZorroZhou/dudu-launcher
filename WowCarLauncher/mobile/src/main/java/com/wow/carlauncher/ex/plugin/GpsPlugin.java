//package com.wow.carlauncher.ex.plugin;
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.maps2d.AMapUtils;
//import com.amap.api.maps2d.model.LatLng;
//import com.wow.carlauncher.R;
//import com.wow.carlauncher.common.view.DashboardView;
//import com.wow.carlauncher.ex.plugin.BasePlugin;
//import com.wow.carlauncher.ex.plugin.PluginManage;
//
//import org.xutils.x;
//
//public class GpsPlugin extends BasePlugin implements AMapLocationListener {
//    private AMapLocationClient locationClient;
//    private DashboardView dashboardView;
//    private TextView tv_haiba, tv_fangxiang, tv_noinfo;
//
//    public GpsPlugin(Context context, PluginManage pluginManage) {
//        super(context, pluginManage);
//
//        locationClient = new AMapLocationClient(context);
//
//        AMapLocationClientOption locationOption = new AMapLocationClientOption();
//        locationClient.setLocationListener(this);
//        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
//        locationOption.setInterval(100);
//        locationOption.setGpsFirst(true);
//        locationOption.setNeedAddress(false);
//        locationOption.setSensorEnable(true);
//        locationClient.setLocationOption(locationOption);
//        locationClient.startLocation();
//    }
//
//
//    public ViewGroup initLauncherView() {
//        ViewGroup launcherView = (ViewGroup) View.inflate(context, R.layout.plugin_gps_launcher, null);
//        dashboardView = (DashboardView) launcherView.findViewById(R.id.speed);
//        tv_haiba = (TextView) launcherView.findViewById(R.id.tv_haiba);
//        tv_fangxiang = (TextView) launcherView.findViewById(R.id.tv_fangxiang);
//        tv_noinfo = (TextView) launcherView.findViewById(R.id.tv_noinfo);
//        return launcherView;
//    }
//
//    @Override
//    public ViewGroup initPopupView() {
//        return null;
//    }
//
//    @Override
//    public void destroy() {
//        super.destroy();
//        locationClient.stopLocation();
//        locationClient.unRegisterLocationListener(this);
//        locationClient = null;
//    }
//
//    private long lastTime = -1;
//    private double lastLat = -1, lastLng = -1;
//    private int lastSpeed = 0;
//    private int speedInterval = 50;
//
//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation.getErrorCode() == AMapLocation.LOCATION_SUCCESS && aMapLocation.getLocationType() == AMapLocation.LOCATION_TYPE_GPS) {
//            if (lastLat == -1 || lastTime == -1 || lastLng == -1) {
//                lastLat = aMapLocation.getLatitude();
//                lastLng = aMapLocation.getLongitude();
//                lastTime = System.currentTimeMillis();
//                return;
//            }
//            tv_noinfo.setVisibility(View.GONE);
//            //先计算出米来
//            int speed = (int) (AMapUtils.calculateLineDistance(new LatLng(lastLat, lastLng), new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())) / ((System.currentTimeMillis() - lastTime) / 1000 / 60 / 60)) / 1000;
//            if (tv_haiba != null) {
//                tv_haiba.setText("海拔:" + aMapLocation.getAltitude() + " 米");
//            }
//            //取值范围：【0，360】，其中0度表示正北方向，90度表示正东，180度表示正南，270度表示正西
//            if (tv_fangxiang != null) {
//                tv_fangxiang.setText("方向:" + getJiaodu(aMapLocation.getBearing()) + "");
//            }
//            if (dashboardView != null) {
//                dashboardView.setVelocity(lastSpeed);
//                double time = (System.currentTimeMillis() - lastTime) / speedInterval;
//                final double speedZ = (double) (speed - lastSpeed) / time;
//                for (int i = 1; i < time; i++) {
//                    final int xx = i;
//                    x.task().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            dashboardView.setVelocity((int) (lastSpeed + speedZ * xx));
//                        }
//                    }, speedInterval * i);
//                }
//            }
//
//            lastLat = aMapLocation.getLatitude();
//            lastLng = aMapLocation.getLongitude();
//            lastTime = System.currentTimeMillis();
//            lastSpeed = speed;
//        }
//    }
//
//    private String getJiaodu(float b) {
//        if ((b < 360 && b > 337.5) || (b > 0 && b < 22.5)) {
//            return "北";
//        } else if (b > 22.5 && b < 67.5) {
//            return "东北";
//        } else if (b > 67.5 && b < 112.5) {
//            return "东";
//        } else if (b > 112.5 && b < 157.5) {
//            return "东南";
//        } else if (b > 157.5 && b < 202.5) {
//            return "南";
//        } else if (b > 202.5 && b < 247.5) {
//            return "西南";
//        } else if (b > 247.5 && b < 292.5) {
//            return "西";
//        } else {
//            return "西北";
//        }
//    }
//}