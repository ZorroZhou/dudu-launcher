package com.wow.carlauncher.plugin.amapcar;

import com.wow.carlauncher.R;

/**
 * Created by 10124 on 2017/11/4.
 */

public class AMapCarConstant {
    public static final int[] ICONS = {
            R.mipmap.gddh_1, R.mipmap.gddh_2, R.mipmap.gddh_3,
            R.mipmap.gddh_4, R.mipmap.gddh_5, R.mipmap.gddh_6,
            R.mipmap.gddh_7, R.mipmap.gddh_8, R.mipmap.gddh_9,
            R.mipmap.gddh_10, R.mipmap.gddh_11, R.mipmap.gddh_12,
            R.mipmap.gddh_13, R.mipmap.gddh_14, R.mipmap.gddh_15,
            R.mipmap.gddh_16, R.mipmap.gddh_17, R.mipmap.gddh_18, R.mipmap.gddh_19, R.mipmap.gddh_20};

    public static final String AMAP_PACKAGE = "com.autonavi.amapauto";

    //接收消息使用的ACTION
    public static final String RECEIVE_ACTION = "AUTONAVI_STANDARD_BROADCAST_SEND";
    //发送消息使用的ACTION
    public static final String SEND_ACTION = "AUTONAVI_STANDARD_BROADCAST_RECV";
    //消息标记
    public static final String KEY_TYPE = "KEY_TYPE";
    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String EXTRA_STATE = "EXTRA_STATE";


    public static final String LON = "LON";
    public static final String LAT = "LAT";
    public static final String EXTRA_M = "EXTRA_M";
    public static final String POINAME = "POINAME";
    public static final String CATEGORY = "CATEGORY";

    //行政区的标记
    public static final int REQUEST_DISTRICT = 10029;
    public static final int RESPONSE_DISTRICT = 10030;
    public static final String RESPONSE_DISTRICT_PRVINCE_NAME = "PRVINCE_NAME";
    public static final String RESPONSE_DISTRICT_CITY_NAME = "CITY_NAME";

    //搜索的标记
    public static final int REQUEST_SEARCH = 10036;
    public static final String REQUEST_SEARCH_KEYWORDS = "KEYWORDS";

    //导航
    public static final int REQUEST_GO = 10038;

    //获取家或者公司的位置设置
    public static final int REQUEST_GETHC = 10045;
    public static final int REQUEST_GETHC_EXTRA_TYPE_HOME = 1;
    public static final int REQUEST_GETHC_EXTRA_TYPE_COMP = 2;
    public static final int RESPONSE_GETHC = 10046;
    public static final String RESPONSE_GETHC_DISTANCE = "DISTANCE";
    public static final String RESPONSE_GETHC_CATEGORY = "CATEGORY";
    public static final String RESPONSE_GETHC_ADDRESS = "ADDRESS";

    //直接导航到家或者公司
    public static final int REQUEST_GO_HC = 10040;
    public static final String REQUEST_GO_HC_DEST = "DEST";
    public static final int REQUEST_GO_HC_DEST_HOME = 0;
    public static final int REQUEST_GO_HC_DEST_COMP = 1;

    //导航切换后台
    public static final int REQUEST_GO_BACK = 10031;

    //跳转到家或者公司的设置界面
    public static final int REQUEST_SET_HC = 10070;
    public static final int REQUEST_SET_HC_HOME = 0;
    public static final int REQUEST_SET_HC_COMP = 1;

    //请求引导信息
    public static final int REQUEST_NAVI_INFO = 10062;

    //引导信息
    public static final int NAVI_INFO = 10001;
    public static final String NAVI_INFO_ICON = "ICON";
    public static final String NAVI_INFO_SEG_REMAIN_DIS = "SEG_REMAIN_DIS";
    public static final String NAVI_INFO_NEXT_ROAD_NAME = "NEXT_ROAD_NAME";
    public static final String NAVI_INFO_ROUTE_REMAIN_DIS = "ROUTE_REMAIN_DIS";
    public static final String NAVI_INFO_ROUTE_REMAIN_TIME = "ROUTE_REMAIN_TIME";
    //状态信息
    public static final int STATE_INFO = 10019;
}
