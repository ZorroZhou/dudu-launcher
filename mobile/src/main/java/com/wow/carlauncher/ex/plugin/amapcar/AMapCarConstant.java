package com.wow.carlauncher.ex.plugin.amapcar;

import com.wow.carlauncher.R;

/**
 * Created by 10124 on 2017/11/4.
 */

public class AMapCarConstant {
    public static final int[] ICONS = {
            R.mipmap.hud_sou1, R.mipmap.hud_sou2, R.mipmap.hud_sou3,
            R.mipmap.hud_sou4, R.mipmap.hud_sou5, R.mipmap.hud_sou6,
            R.mipmap.hud_sou7, R.mipmap.hud_sou8, R.mipmap.hud_sou9,
            R.mipmap.hud_sou10, R.mipmap.hud_sou11, R.mipmap.hud_sou12,
            R.mipmap.hud_sou13, R.mipmap.hud_sou14, R.mipmap.hud_sou15,
            R.mipmap.hud_sou16, R.mipmap.hud_sou17, R.mipmap.hud_sou18, R.mipmap.hud_sou19, R.mipmap.hud_sou20};

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
    public static final int RECEIVER_RESPONSE_DISTRICT = 10030;
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
    public static final int RECEIVER_RESPONSE_GETHC = 10046;
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

    public static final int REQUEST_EXIT_NAV = 10010;

    //引导信息
    public static final int RECEIVER_NAVI_INFO = 10001;

    public static class NaviInfoConstant {
        //导航类型，对应的值为int类型
        //：GPS导航
        //1：模拟导航
        public static final String TYPE = "TYPE";

        //当前道路名称，对应的值为String类型

        public static final String CUR_ROAD_NAME = "CUR_ROAD_NAME";

        //下一道路名，对应的值为String类型
        public static final String NEXT_ROAD_NAME = "NEXT_ROAD_NAME";


        //距离最近服务区的距离，对应的值为int类型，单位：米
        public static final String SAPA_DIST = "SAPA_DIST";

        //服务区类型，对应的值为int类型
        //0：高速服务区
        //1：其他高速服务设施（收费站等）

        public static final String SAPA_TYPE = "SAPA_TYPE";

        //距离最近的电子眼距离，对应的值为int类型，单位：米
        public static final String CAMERA_DIST = "CAMERA_DIST";

        //电子眼类型，对应的值为int类型
        //0 测速摄像头
        //1为监控摄像头
        //2为闯红灯拍照
        //3为违章拍照
        //4为公交专用道摄像头
        //5为应急车道摄像头

        public static final String CAMERA_TYPE = "CAMERA_TYPE";

        //电子眼限速度，对应的值为int类型，无限速则为0，单位：公里/小时
        public static final String CAMERA_SPEED = "CAMERA_SPEED";


        //下一个将要路过的电子眼编号，若为-1则对应的道路上没有电子眼，对应的值为int类型
        public static final String CAMERA_INDEX = "CAMERA_INDEX";

        //导航转向图标，对应的值为int类型
        public static final String ICON = "ICON";

        //路径剩余距离，对应的值为int类型，单位：米
        public static final String ROUTE_REMAIN_DIS = "ROUTE_REMAIN_DIS";

        //路径剩余时间，对应的值为int类型，单位：秒
        public static final String ROUTE_REMAIN_TIME = "ROUTE_REMAIN_TIME";

        //当前导航段剩余距离，对应的值为int类型，单位：米
        public static final String SEG_REMAIN_DIS = "SEG_REMAIN_DIS";

        //当前导航段剩余时间，对应的值为int类型，单位：秒
        public static final String SEG_REMAIN_TIME = "SEG_REMAIN_TIME";

        //当前位置的前一个形状点号，对应的值为int类型，从0开始
        public static final String CUR_POINT_NUM = "CUR_POINT_NUM";

        //环岛出口序号，对应的值为int类型，从0开始.
        //1.x版本：只有在icon为11和12时有效，其余为无效值0
        //2.x版本：只有在icon为11、12、17、18时有效，其余为无效值0
        public static final String ROUND_ABOUT_NUM = "ROUNG_ABOUT_NUM";

        //路径总距离，对应的值为int类型，单位：米
        public static final String ROUTE_ALL_DIS = "ROUTE_ALL_DIS";

        //路径总时间，对应的值为int类型，单位：秒
        public static final String ROUTE_ALL_TIME = "ROUTE_ALL_TIME";

        //当前车速，对应的值为int类型，单位：公里/小时
        public static final String CUR_SPEED = "CUR_SPEED";

        //红绿灯个数，对应的值为int类型
        public static final String TRAFFIC_LIGHT_NUM = "TRAFFIC_LIGHT_NUM";

        //服务区个数，对应的值为int类型
        public static final String SAPA_NUM = "SAPA_NUM";

        //下一个服务区名称，对应的值为String类型
        public static final String SAPA_NAME = "SAPA_NAME";

        //当前道路类型，对应的值为int类型
        //0：高速公路
        //1：国道
        //2：省道
        //3：县道
        //4：乡公路
        //5：县乡村内部道路
        //6：主要大街、城市快速道
        //7：主要道路
        //8：次要道路
        //9：普通道路
        //10：非导航道路

        public static final String ROAD_TYPE = "ROAD_TYPE";
    }


    //状态信息
    public static final int RECEIVER_STATE_INFO = 10019;

    public static class StateInfoConstant {
        public static final int APP_OPEN = 0;//开始运行，Application启动即为开始运行
        public static final int APP_INIT = 1;//初始化完成，每次创建地图完成通知
        public static final int APP_EXIT = 2;//运行结束，退出程序
        public static final int APP_TO_FRONT = 3;//进入前台，OnStart函数中调用
        public static final int APP_TO_BACK = 4;//进入后台，OnStop函数中调用
        public static final int GET_ROAD = 5;//开始算路
        public static final int GET_ROAD_SUCCESS = 6;//开始算路 成功
        public static final int GET_ROAD_ERROR = 7;//开始算路 失败
        public static final int NAV_START = 8;//开始导航
        public static final int NAV_STOP = 9;//结束导航
        public static final int MNAV_START = 10;//开始模拟导航
        public static final int MNAV_PAUSE = 11;//暂停模拟导航
        public static final int MNAV_STOP = 12;//结束模拟导航
        public static final int TTS_START = 13;//开始TTS播报
        public static final int TTS_STOP = 14;//停止TTS播报
        public static final int XH_START = 24;//进入巡航播报状态
        public static final int XH_STOP = 25;//退出巡航播报状态

        public static final int NAV_GET = 39;//到达目的地通知
        public static final int HREAT = 40;//心跳通知
    }

    public static final int RECEIVER_LUKUANG_INFO = 13011;
    public static final String RECEIVER_LUKUANG_INFO_EXTAR = "EXTRA_TMC_SEGMENT";
    public static final int RECEIVER_LUKUANG_TYPE_NONE = -1;//无效
    public static final int RECEIVER_LUKUANG_TYPE_NO_INFO = 0;//无交通流

    public static final int RECEIVER_LUKUANG_TYPE_R1 = 1;//畅通
    public static final int RECEIVER_LUKUANG_TYPE_R2 = 2;//缓行（黄色）
    public static final int RECEIVER_LUKUANG_TYPE_R3 = 3;// 拥堵（红色）
    public static final int RECEIVER_LUKUANG_TYPE_R4 = 4;//严重拥堵（深红色）
    public static final int RECEIVER_LUKUANG_TYPE_OVER = 10;// 行驶过的路段（灰色）

}
