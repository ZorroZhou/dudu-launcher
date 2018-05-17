package com.wow.carlauncher.ex.manage.trip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wow.carlauncher.ex.ContextEx;
import com.wow.carlauncher.ex.manage.location.event.MNewLocationEvent;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarInfo;
import com.wow.carlauncher.repertory.db.model.Trip;
import com.wow.carlauncher.repertory.db.model.TripPoint;
import com.wow.carlauncher.view.activity.driving.DrivingActivity;
import com.wow.carlauncher.view.activity.launcher.LauncherActivity;
import com.wow.frame.repertory.dbTool.DatabaseManage;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.DbManager;
import org.xutils.x;

import static com.wow.carlauncher.common.CommonData.*;

/**
 * 行程管理器
 * Created by 10124 on 2018/5/11.
 */

public class TripManage extends ContextEx {
    private final static String TAG = "WOW_CAR 行程管理器:";

    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static TripManage instance = new TripManage();
    }

    public static TripManage self() {
        return TripManage.SingletonHolder.instance;
    }

    private boolean drivingShow = false;
    private Trip trip;

    public long getTropStartTime() {
        if (trip != null) {
            return trip.getStartTime();
        } else {
            return 0L;
        }
    }

    public int getTropMileage() {
        if (trip != null) {
            return trip.getMileage();
        } else {
            return 0;
        }
    }


    public boolean isDrivingShow() {
        return drivingShow;
    }

    public boolean isTripStart() {
        return trip != null;
    }

    public TripManage setDrivingShow(boolean drivingShow) {
        this.drivingShow = drivingShow;
        return this;
    }

    private TripManage() {
        super();
    }

    public void init(Context context) {
        setContext(context);
        EventBus.getDefault().register(this);
    }

    private long lastSpeedTime = 0;
    private int lastSpeed = -1;

    private final byte[] tripLock = new byte[0];

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventMainThread(PObdEventCarInfo event) {
        synchronized (tripLock) {
            //Log.d(TAG, "onEventMainThread: " + event);
            if (event.getRev() != null && event.getRev() > 400) {
                //这里先这么处理,之后再调整
                if (!drivingShow && SharedPreUtil.getSharedPreBoolean(SDATA_TRIP_AUTO_OPEN_DRIVING, SDATA_TRIP_AUTO_OPEN_DRIVING_DF)) {
                    Intent intent2 = new Intent(getContext(), DrivingActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent2);
                }

                if (trip != null) {
                    trip.setLastMarkTime(System.currentTimeMillis());
                    DatabaseManage.saveSyn(trip);
                } else {
                    Log.d(TAG, "onEventMainThread  adasdasdasd111: " + trip);
                    trip = new Trip()
                            .setStartTime(System.currentTimeMillis())
                            .setMileage(0)
                            .setLastMarkTime(System.currentTimeMillis());
                    DatabaseManage.saveSyn(trip);
                }
            }
            //这里说明是行程开始了,因为插入行程是个异步操作,所以要检查id是否有值(插入操作是否完成)
            Log.d(TAG, "onEventMainThread: " + trip.getId());
            if (event.getSpeed() != null && trip != null && trip.getId() != null) {
                Log.d(TAG, "onEventMainThread: !!!!!");
                //这里计算里程
                if (lastSpeedTime != 0) {
                    long mm = System.currentTimeMillis() - lastSpeedTime;
                    //毫秒转成小时后,乘以速度
                    double mi = (double) mm / 1000 / 60 / 60 * (double) (lastSpeed + (event.getSpeed() - lastSpeed) / 2) * 1000;
                    trip.setMileage(trip.getMileage() + (int) mi);
                    DatabaseManage.saveSyn(trip);
                }
                lastSpeedTime = System.currentTimeMillis();
                lastSpeed = event.getSpeed();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventMainThread(MTimeSecondEvent event) {
        synchronized (tripLock) {
            //如果长时间没有刷新信息,则说明行程该结束了
            if (trip != null && System.currentTimeMillis() - trip.getLastMarkTime() > SharedPreUtil.getSharedPreInteger(SDATA_TRIP_MERGE_TIME, SDATA_TRIP_MERGE_TIME_DF) * 60 * 1000) {
                trip = null;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventMainThread(MNewLocationEvent event) {
        synchronized (tripLock) {
            if (lastSpeedTime > 0 && lastSpeed >= 0 && trip != null && trip.getId() != null) {
                DatabaseManage.saveSyn(new TripPoint()
                        .setTrip(trip.getId())
                        .setLat(event.getLatitude())
                        .setLon(event.getLongitude())
                        .setSpeed(lastSpeed)
                        .setTime(System.currentTimeMillis()));
            }
        }
    }
}
