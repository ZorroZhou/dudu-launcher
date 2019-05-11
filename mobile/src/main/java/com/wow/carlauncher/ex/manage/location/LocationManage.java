package com.wow.carlauncher.ex.manage.location;

import android.annotation.SuppressLint;
import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.location.event.MNewLocationEvent;

/**
 * Created by 10124 on 2018/5/12.
 */

public class LocationManage extends ContextEx implements AMapLocationListener {

    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static LocationManage instance = new LocationManage();
    }

    public static LocationManage self() {
        return LocationManage.SingletonHolder.instance;
    }

    private LocationManage() {
        super();
    }

    public void init(Context context) {
        setContext(context);
        TaskExecutor.self().run(() -> {
            //初始化定位
            mLocationClient = new AMapLocationClient(context);
            //设置定位回调监听
            mLocationClient.setLocationListener(LocationManage.this);
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setInterval(2000);
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //获取一次定位结果：
            //该方法默认为false。
            mLocationOption.setOnceLocation(false);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            //设置是否返回地址信息（默认返回地址信息）
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
            LogEx.d(LocationManage.this, "init ");
        });
    }

    /**
     * 声明AMapLocationClient类对象
     */
    private AMapLocationClient mLocationClient = null;
    /**
     * 声明定位回调监听器
     */
    private AMapLocationClientOption mLocationOption = null;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0 && CommonUtil.isNotNull(aMapLocation.getCity())) {
            LogEx.d(this, "location success");
            postEvent(new MNewLocationEvent()
                    .setLocationType(aMapLocation.getLocationType())
                    .setBearing(aMapLocation.getBearing())
                    .setSpeed(aMapLocation.getSpeed())
                    .setDistrict(aMapLocation.getDistrict())
                    .setCity(aMapLocation.getCity())
                    .setAdCode(aMapLocation.getAdCode())
                    .setLatitude(aMapLocation.getLatitude())
                    .setLongitude(aMapLocation.getLongitude()));
        } else {
            LogEx.d(this, "location fail");
        }
    }
}
