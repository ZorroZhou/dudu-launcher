package com.wow.carlauncher.ex.plugin.amapcar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventNavInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventState;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.*;

public class AMapCartReceiver extends BroadcastReceiver {
    public static final int GETHC_NEXT_TO_NONE = 0;
    public static final int GETHC_NEXT_TO_NAVI = 1;

    private AMapCarPlugin aMapCarPlugin;


    private int getHcNext = 0;
    private long setGetHcNextTime = -1;

    AMapCartReceiver(AMapCarPlugin aMapCarPlugin) {
        this.aMapCarPlugin = aMapCarPlugin;
    }

    void setGetHcNext(int getHcNext) {
        this.getHcNext = getHcNext;
        setGetHcNextTime = System.currentTimeMillis();
    }

    @Override
    public void onReceive(Context context, final Intent intent) {
        String action = intent.getAction();
        if (RECEIVE_ACTION.equals(action)) {
            int key = intent.getIntExtra(KEY_TYPE, -1);
            switch (key) {
                case RESPONSE_DISTRICT: {
                    intent.getStringExtra(RESPONSE_DISTRICT_PRVINCE_NAME);
                    intent.getStringExtra(RESPONSE_DISTRICT_CITY_NAME);
                    break;
                }
                case RESPONSE_GETHC: {
                    double lon = intent.getDoubleExtra(LON, 0);
                    double lat = intent.getDoubleExtra(LAT, 0);
                    int type = intent.getIntExtra(CATEGORY, -1);
                    if (type == 1) {
                        if (lon == 0 || lat == 0) {
                            Toast.makeText(context, "没有设置家的位置,请打开高德进行设置", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            int next = GETHC_NEXT_TO_NONE;
                            if (System.currentTimeMillis() - setGetHcNextTime < 2000) {
                                next = getHcNext;
                            }
                            switch (next) {
                                case GETHC_NEXT_TO_NAVI: {
                                    aMapCarPlugin.naviToHome();
                                    break;
                                }
                            }
                        }
                    } else if (type == 2) {
                        if (lon == 0 || lat == 0) {
                            Toast.makeText(context, "没有设置公司的位置,请打开高德进行设置", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            int next = GETHC_NEXT_TO_NONE;
                            if (System.currentTimeMillis() - setGetHcNextTime < 2000) {
                                next = getHcNext;
                            }
                            switch (next) {
                                case GETHC_NEXT_TO_NAVI: {
                                    aMapCarPlugin.naviToComp();
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
                case NAVI_INFO: {
                    x.task().autoPost(new Runnable() {
                        @Override
                        public void run() {
                            EventBus.getDefault().post(new PAmapEventState().setState(8));
                            EventBus.getDefault().post(new PAmapEventNavInfo()
                                    .setDis(intent.getIntExtra(NAVI_INFO_SEG_REMAIN_DIS, -1))
                                    .setIcon(intent.getIntExtra(NAVI_INFO_ICON, -1))
                                    .setWroad(intent.getStringExtra(NAVI_INFO_NEXT_ROAD_NAME))
                                    .setRemainDis(intent.getIntExtra(NAVI_INFO_ROUTE_REMAIN_DIS, -1))
                                    .setRemainTime(intent.getIntExtra(NAVI_INFO_ROUTE_REMAIN_TIME, -1)));
                        }
                    });
                    break;
                }

                case STATE_INFO: {
                    x.task().autoPost(new Runnable() {
                        @Override
                        public void run() {
                            EventBus.getDefault().post(new PAmapEventState().setState(intent.getIntExtra(EXTRA_STATE, -1)));
                        }
                    });
                    break;
                }
            }
        }
    }
}
