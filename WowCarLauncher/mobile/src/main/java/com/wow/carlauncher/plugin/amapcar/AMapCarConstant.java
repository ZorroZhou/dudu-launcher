package com.wow.carlauncher.plugin.amapcar;

/**
 * Created by 10124 on 2017/11/4.
 */

public class AMapCarConstant {
    //接收消息使用的ACTION
    public static final String RECEIVE_ACTION = "AUTONAVI_STANDARD_BROADCAST_SEND";
    //发送消息使用的ACTION
    public static final String SEND_ACTION = "AUTONAVI_STANDARD_BROADCAST_RECV";
    //消息标记
    public static final String KEY_TYPE = "KEY_TYPE";

    //行政区的标记
    public static final int REQUEST_DISTRICT = 10029;
    public static final int RESPONSE_DISTRICT = 10030;
    public static final String RESPONSE_DISTRICT_PRVINCE_NAME = "PRVINCE_NAME";
    public static final String RESPONSE_DISTRICT_CITY_NAME = "CITY_NAME";

    //搜索的标记
    public static final int REQUEST_SEARCH = 10036;


}
