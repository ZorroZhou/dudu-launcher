package com.wow.carlauncher.ex.manage.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.user.LocalUser;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.common.util.NetWorkUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.speed.SMEventReceiveSpeed;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.server.CommonCallback;
import com.wow.carlauncher.repertory.server.CommonService;
import com.wow.carlauncher.repertory.server.UserCarService;
import com.wow.carlauncher.view.event.EventNetStateChange;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.wow.carlauncher.common.CommonData.LOGIN_USER_ID;
import static com.wow.carlauncher.common.CommonData.LOGIN_USER_INFO;
import static com.wow.carlauncher.common.CommonData.SDATA_ALLOW_REPORT_LOCATION;
import static com.wow.carlauncher.common.CommonData.SDATA_HOME_FULL;
import static com.wow.carlauncher.repertory.server.ServerConstant.NET_ERROR;
import static com.wow.carlauncher.repertory.server.ServerConstant.RES_ERROR;

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
        long t1 = System.currentTimeMillis();
        setContext(context);
        EventBus.getDefault().register(this);
        TaskExecutor.self().run(() -> {
            //初始化定位
            mLocationClient = new AMapLocationClient(context);
            //设置定位回调监听
            mLocationClient.setLocationListener(LocationManage.this);
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setInterval(1500);
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
            LogEx.d(this, "init time:" + (System.currentTimeMillis() - t1));

            onEvent(new EventNetStateChange());
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
    private boolean fristSuccess = false;

    private boolean netOk = false;
    private long lastReportTime = 0;

    @Override
    public void onLocationChanged(final AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0 && CommonUtil.isNotNull(aMapLocation.getCity())) {
            if (!fristSuccess) {
                LogEx.d(this, "location success");
                fristSuccess = true;
            }
            postEvent(new LMEventNewLocation()
                    .setLocationType(aMapLocation.getLocationType())
                    .setBearing(aMapLocation.getBearing())
                    .setSpeed(aMapLocation.getSpeed())
                    .setDistrict(aMapLocation.getDistrict())
                    .setCity(aMapLocation.getCity())
                    .setAdCode(aMapLocation.getAdCode())
                    .setLatitude(aMapLocation.getLatitude())
                    .setLongitude(aMapLocation.getLongitude()));

            if (aMapLocation.getLocationType() == AMapLocation.LOCATION_TYPE_GPS) {
                EventBus.getDefault().post(new SMEventReceiveSpeed().setSpeed((int) (aMapLocation.getSpeed() * 60 * 60 / 1000)).setFrom(SMEventReceiveSpeed.SMReceiveSpeedFrom.GPS));
            }

            if (SharedPreUtil.getBoolean(SDATA_ALLOW_REPORT_LOCATION, true) && netOk && System.currentTimeMillis() - lastReportTime > 10 * 1000) {
                lastReportTime = System.currentTimeMillis();
                TaskExecutor.self().run(() -> {
                    long uid = SharedPreUtil.getLong(LOGIN_USER_ID, -1);
                    if (uid > 0) {
                        try {
                            String deviceId = Settings.System.getString(getContext().getContentResolver(), Settings.System.ANDROID_ID);
                            UserCarService.reportLocation(deviceId, aMapLocation.getLatitude() + "-" + aMapLocation.getLongitude(), null);
                        } catch (Exception ignored) {
                        }
                    }
                });
            }
        } else if (aMapLocation != null) {
            LogEx.d(this, "location fail error code:" + aMapLocation.getErrorCode() + "   " + aMapLocation.getErrorInfo());
        } else {
            LogEx.d(this, "location fail ");
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(EventNetStateChange event) {
        netOk = NetWorkUtil.isOnline();
    }
}
