package com.wow.carlauncher.common;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.TAG;

/**
 * Created by 10124 on 2017/11/4.
 */

public class LocationManage implements AMapLocationListener {
    private static LocationManage self;

    public static LocationManage self() {
        if (self == null) {
            self = new LocationManage();
        }
        return self;
    }

    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption = null;
    private Context mContext;
    private List<AMapLocationListener> locationListeners;

    private LocationManage() {

    }

    public void init(Context mContext) {
        this.mContext = mContext;
        locationListeners = Collections.synchronizedList(new ArrayList<AMapLocationListener>());

        locationClient = new AMapLocationClient(mContext);

        locationOption = new AMapLocationClientOption();
        locationClient.setLocationListener(this);
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setInterval(1000 * 60 * 5);
        locationClient.setLocationOption(locationOption);
        locationClient.startLocation();
    }

    public void addLocationListener(AMapLocationListener locationListener) {
        locationListeners.add(locationListener);
        locationListener.onLocationChanged(locationClient.getLastKnownLocation());
    }

    public void removeLocationListener(AMapLocationListener locationListener) {
        locationListeners.remove(locationListener);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.e(TAG, "onLocationChanged: " + aMapLocation);
        for (AMapLocationListener locationListener : locationListeners) {
            locationListener.onLocationChanged(aMapLocation);
        }
    }
}
