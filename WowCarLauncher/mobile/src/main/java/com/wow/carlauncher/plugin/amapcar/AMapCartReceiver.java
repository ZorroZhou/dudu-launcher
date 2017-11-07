package com.wow.carlauncher.plugin.amapcar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wow.carlauncher.common.BaseDialog;

import org.xutils.x;

import static com.wow.carlauncher.plugin.amapcar.AMapCarConstant.*;

/**
 * Created by 10124 on 2017/11/6.
 */

public class AMapCartReceiver extends BroadcastReceiver {
    private static final String TAG = "AMapCartReceiver";
    private AMapCarPlugin aMapCarPlugin;

    AMapCartReceiver(AMapCarPlugin aMapCarPlugin) {
        this.aMapCarPlugin = aMapCarPlugin;
    }

    @Override
    public void onReceive(Context context, final Intent intent) {
        String action = intent.getAction();
        if (action.equals(RECEIVE_ACTION)) {
            int key = intent.getIntExtra(KEY_TYPE, -1);
            Bundle bundle = intent.getExtras();
//            Log.i("Bundle Content", "start Key=" + key + "-----------------------------------");
//            for (String k : bundle.keySet()) {
//                Log.i("Bundle Content", "Key=" + k + ", content=" + bundle.get(k));
//            }
//            Log.i("Bundle Content", "end Key=" + key + "-----------------------------------");
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
                            new BaseDialog(aMapCarPlugin.getPluginManage().getCurrentActivity())
                                    .setTitle("提示")
                                    .setGravityCenter()
                                    .setMessage("没有设置家的信息，是否前往设置")
                                    .setBtn1("取消", null)
                                    .setBtn2("确定", new BaseDialog.OnBtnClickListener() {
                                        @Override
                                        public boolean onClick(BaseDialog dialog) {
                                            aMapCarPlugin.getAmapSend().toHomeSet();
                                            return true;
                                        }
                                    }).show();
                            return;
                        } else {
                            aMapCarPlugin.getAmapSend().naviToHome();
                        }
                    } else if (type == 2) {
                        if (lon == 0 || lat == 0) {
                            new BaseDialog(aMapCarPlugin.getPluginManage().getCurrentActivity())
                                    .setTitle("提示")
                                    .setGravityCenter()
                                    .setMessage("没有设置公司的信息，是否前往设置")
                                    .setBtn1("取消", null)
                                    .setBtn2("确定", new BaseDialog.OnBtnClickListener() {
                                        @Override
                                        public boolean onClick(BaseDialog dialog) {
                                            aMapCarPlugin.getAmapSend().toCompSet();
                                            return true;
                                        }
                                    }).show();
                            return;
                        } else {
                            aMapCarPlugin.getAmapSend().naviToComp();
                        }
                    }
                    break;
                }
                case NAVI_INFO: {
                    x.task().autoPost(new Runnable() {
                        @Override
                        public void run() {
                            NaviInfo naviBean2 = new NaviInfo(NaviInfo.TYPE_STATE);
                            naviBean2.setState(8);
                            aMapCarPlugin.refreshNaviInfo(naviBean2);

                            NaviInfo naviBean = new NaviInfo(NaviInfo.TYPE_NAVI);
                            naviBean.setDis(intent.getIntExtra(NAVI_INFO_SEG_REMAIN_DIS, -1));
                            naviBean.setIcon(intent.getIntExtra(NAVI_INFO_ICON, -1));
                            naviBean.setWroad(intent.getStringExtra(NAVI_INFO_NEXT_ROAD_NAME));
                            naviBean.setRemainDis(intent.getIntExtra(NAVI_INFO_ROUTE_REMAIN_DIS, -1));
                            naviBean.setRemainTime(intent.getIntExtra(NAVI_INFO_ROUTE_REMAIN_TIME, -1));
                            aMapCarPlugin.refreshNaviInfo(naviBean);
                        }
                    });
                    break;
                }

                case STATE_INFO: {
                    x.task().autoPost(new Runnable() {
                        @Override
                        public void run() {
                            NaviInfo naviBean = new NaviInfo(NaviInfo.TYPE_STATE);
                            naviBean.setState(intent.getIntExtra(EXTRA_STATE, -1));
                            aMapCarPlugin.refreshNaviInfo(naviBean);
                        }
                    });
                    break;
                }
            }
        }
    }
}
