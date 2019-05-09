package com.wow.carlauncher.ex.plugin.amapcar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventNavInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventState;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapLukuangInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapMuteStateInfo;
import com.wow.carlauncher.ex.plugin.amapcar.model.Lukuang;

import org.greenrobot.eventbus.EventBus;

import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.CATEGORY;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.EXTRA_STATE;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.KEY_TYPE;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.LAT;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.LON;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.NaviInfoConstant;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.RECEIVER_LUKUANG_INFO;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.RECEIVER_LUKUANG_INFO_EXTAR;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.RECEIVER_NAVI_INFO;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.RECEIVER_RESPONSE_DISTRICT;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.RECEIVER_RESPONSE_GETHC;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.RECEIVER_STATE_INFO;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.RECEIVE_ACTION;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.RECEIVE_NAVI_MUTE_STATE;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.RECEIVE_NAVI_MUTE_STATE_CASUAL_MUTE;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.RECEIVE_NAVI_MUTE_STATE_MUTE;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.RESPONSE_DISTRICT_CITY_NAME;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.RESPONSE_DISTRICT_PRVINCE_NAME;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.StateInfoConstant;

public class AMapCartReceiver extends BroadcastReceiver {
    public static final String TAG = "WOW_CAR_AMAP";

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
            aMapCarPlugin.noticeHeartbeatTime();
            int key = intent.getIntExtra(KEY_TYPE, -1);
            LogEx.d(this, "onReceive key:" + key);
            switch (key) {
                case RECEIVER_RESPONSE_DISTRICT: {
                    intent.getStringExtra(RESPONSE_DISTRICT_PRVINCE_NAME);
                    intent.getStringExtra(RESPONSE_DISTRICT_CITY_NAME);
                    break;
                }
                case RECEIVER_RESPONSE_GETHC: {
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
                case RECEIVER_NAVI_INFO: {
                    int icon = intent.getIntExtra(NaviInfoConstant.ICON, -1);
                    PAmapEventNavInfo info = new PAmapEventNavInfo()
                            .setRoadType(intent.getIntExtra(NaviInfoConstant.ROAD_TYPE, -1))
                            .setType(intent.getIntExtra(NaviInfoConstant.TYPE, -1))
                            .setSegRemainDis(intent.getIntExtra(NaviInfoConstant.SEG_REMAIN_DIS, -1))
                            .setIcon(intent.getIntExtra(NaviInfoConstant.ICON, -1))
                            .setNextRoadName(intent.getStringExtra(NaviInfoConstant.NEXT_ROAD_NAME))
                            .setCurRoadName(intent.getStringExtra(NaviInfoConstant.CUR_ROAD_NAME))
                            .setRouteRemainDis(intent.getIntExtra(NaviInfoConstant.ROUTE_REMAIN_DIS, -1))
                            .setRouteRemainTime(intent.getIntExtra(NaviInfoConstant.ROUTE_REMAIN_TIME, -1))
                            .setRouteAllDis(intent.getIntExtra(NaviInfoConstant.ROUTE_ALL_DIS, -1))
                            .setRouteAllTime(intent.getIntExtra(NaviInfoConstant.ROUTE_ALL_TIME, -1))
                            .setCurSpeed(intent.getIntExtra(NaviInfoConstant.CUR_SPEED, -1))
                            .setCameraSpeed(intent.getIntExtra(NaviInfoConstant.CAMERA_SPEED, -1));

                    if (icon != -1
                            && info.getSegRemainDis() != 0
                            && info.getRouteAllDis() != 0
                            && CommonUtil.isNotNull(info.getNextRoadName())
                            && CommonUtil.isNotNull(info.getCurRoadName())) {
                        LogEx.d(this, "onReceive PAmapEventNavInfo:" + info);
                        EventBus.getDefault().post(new PAmapEventState().setRunning(true));
                        EventBus.getDefault().post(info);
                    } else {
                        LogEx.d(this, "onReceive no nav info");
                        EventBus.getDefault().post(new PAmapEventState().setRunning(false));
                    }
                    break;
                }

                case RECEIVER_STATE_INFO: {
                    int state = intent.getIntExtra(EXTRA_STATE, -1);
                    LogEx.d(this, "onReceive state:" + state);
                    if (state == StateInfoConstant.NAV_START
                            || state == StateInfoConstant.MNAV_START) {
                        EventBus.getDefault().post(new PAmapEventState().setRunning(true));
                    } else if (state == StateInfoConstant.NAV_STOP
                            || state == StateInfoConstant.MNAV_STOP
                            || state == StateInfoConstant.APP_EXIT
                            || state == StateInfoConstant.NAV_DAODA
                            || state == StateInfoConstant.XH_STOP) {
                        EventBus.getDefault().post(new PAmapEventState().setRunning(false));
                    }
                    //如果APP启动和APP退到后台了,则同步一下静音状态
                    if (state == StateInfoConstant.APP_OPEN || state == StateInfoConstant.APP_TO_BACK) {
                        aMapCarPlugin.getMute();
                    }
                    break;
                }
                case RECEIVER_LUKUANG_INFO: {
                    String info = intent.getStringExtra(RECEIVER_LUKUANG_INFO_EXTAR);
                    LogEx.d(this, "onReceive Lukuang:" + info);
                    Lukuang lukuang = GsonUtil.getGson().fromJson(info, Lukuang.class);
                    if (lukuang != null) {
                        EventBus.getDefault().post(new PAmapLukuangInfo().setLukuang(lukuang));
                    }
                    break;
                }

                case RECEIVE_NAVI_MUTE_STATE: {
                    int state1 = intent.getIntExtra(RECEIVE_NAVI_MUTE_STATE_MUTE, 0);
                    int state2 = intent.getIntExtra(RECEIVE_NAVI_MUTE_STATE_CASUAL_MUTE, 0);
                    EventBus.getDefault().post(new PAmapMuteStateInfo().setMute(state1 == 1 || state2 == 1));
                    LogEx.d(this, "onReceive mute:" + (state1 == 1 || state2 == 1));
                    break;
                }
            }
        }
    }
}
