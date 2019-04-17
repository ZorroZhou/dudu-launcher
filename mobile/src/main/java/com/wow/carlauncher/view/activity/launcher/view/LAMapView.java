package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.LukuangView;
import com.wow.carlauncher.ex.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventNavInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventState;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapLukuangInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapMuteStateInfo;
import com.wow.carlauncher.ex.plugin.amapcar.model.Lukuang;
import com.wow.carlauncher.view.base.BaseEBusView;
import com.wow.frame.util.AppUtil;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.AMAP_PACKAGE;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.ICONS;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LAMapView extends BaseEBusView {

    public LAMapView(@NonNull Context context) {
        super(context);
        addContent(R.layout.content_l_amap);
    }

    public LAMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addContent(R.layout.content_l_amap);
    }

    private boolean mute = false;

    @ViewInject(R.id.iv_icon)
    private ImageView amapIcon;

    @ViewInject(R.id.iv_road)
    private ImageView iv_road;

    @ViewInject(R.id.tv_next_dis)
    private TextView tv_next_dis;

    @ViewInject(R.id.tv_next_road)
    private TextView tv_next_road;

    @ViewInject(R.id.tv_xiansu)
    private TextView tv_xiansu;

    @ViewInject(R.id.ll_xiansu)
    private LinearLayout ll_xiansu;

    @ViewInject(R.id.tv_msg)
    private TextView tv_msg;

    @ViewInject(R.id.iv_mute)
    private ImageView iv_mute;

    @ViewInject(R.id.rl_moren)
    private View rl_moren;

    @ViewInject(R.id.rl_daohang)
    private View rl_daohang;

    @ViewInject(R.id.lukuang)
    private LukuangView lukuangView;

    @Event(value = {R.id.rl_base, R.id.btn_go_home, R.id.btn_close, R.id.btn_mute, R.id.btn_nav_gs, R.id.btn_nav_j, R.id.btn_gd})
    private void clickEvent(View view) {
        Log.d(TAG, "clickEvent: " + view);
        switch (view.getId()) {
            case R.id.rl_base: {
                Intent appIntent = getContext().getPackageManager().getLaunchIntentForPackage(AMAP_PACKAGE);
                if (appIntent == null) {
                    Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
                appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(appIntent);
                break;
            }
            case R.id.btn_nav_j: {
                if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
                    Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
                AMapCarPlugin.self().naviToHome();
                break;
            }
            case R.id.btn_nav_gs: {
                if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
                    Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
                AMapCarPlugin.self().naviToComp();
                break;
            }
            case R.id.btn_close: {
                if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
                    Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
                AMapCarPlugin.self().exitNav();
                break;
            }
            case R.id.btn_mute: {
                if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
                    Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
                AMapCarPlugin.self().mute(!mute);
                break;
            }
            case R.id.btn_gd: {
                if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
                    Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
                AMapCarPlugin.self().testNavi();
                break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final PAmapEventState event) {
        rl_moren.setVisibility(event.isRunning() ? GONE : VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final PAmapMuteStateInfo event) {
        if (iv_mute != null) {
            mute = event.isMute();
            if (event.isMute()) {
                iv_mute.setImageResource(R.mipmap.n_dh_jy);
            } else {
                iv_mute.setImageResource(R.mipmap.n_dh_bjy);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final PAmapEventNavInfo event) {
        String fangxiang = "";
        if (amapIcon != null && event.getIcon() - 1 >= 0 && event.getIcon() - 1 < ICONS.length) {
            amapIcon.setImageResource(ICONS[event.getIcon() - 1]);
            switch (event.getIcon()) {
                case 2:
                    fangxiang = "左拐";
                    break;
                case 3:
                    fangxiang = "右拐";
                    break;
                case 4:
                    fangxiang = "左前方";
                    break;
                case 5:
                    fangxiang = "右前方";
                    break;
                case 6:
                    fangxiang = "左后方";
                    break;
                case 7:
                    fangxiang = "右后方";
                    break;
                case 8:
                    fangxiang = "掉头";
                    break;
                case 20:
                    fangxiang = "右方掉头";
                    break;
            }
        }

        if (tv_next_dis != null && CommonUtil.isNotNull(event.getSegRemainDis())) {
            String msg = "";
            if (event.getSegRemainDis() < 10) {
                msg = msg + "现在";
            } else {
                if (event.getSegRemainDis() > 1000) {
                    msg = msg + event.getSegRemainDis() / 1000 + "公里后";
                } else {
                    msg = msg + event.getSegRemainDis() + "米后";
                }
            }
            tv_next_dis.setText(msg);
        }
        if (tv_next_road != null && CommonUtil.isNotNull(event.getNextRoadName())) {
            if ("目的地".equals(event.getNextRoadName())) {
                String msg = fangxiang + "到达" + event.getNextRoadName();
                tv_next_road.setText(msg);
            } else {
                String msg = fangxiang + "进入" + event.getNextRoadName();
                tv_next_road.setText(msg);
            }
        }
        if (tv_xiansu != null) {
            if (event.getCameraSpeed() > 0) {
                String msg = event.getCameraSpeed() + "";
                tv_xiansu.setText(msg);
                ll_xiansu.setVisibility(VISIBLE);
            } else {
                ll_xiansu.setVisibility(GONE);
            }
        }

        if (tv_msg != null && event.getRouteRemainTime() > -1 && event.getRouteRemainDis() > -1) {
            if (event.getRouteRemainTime() == 0 || event.getRouteRemainDis() == 0) {
                tv_msg.setText("到达");
            } else {
                String msg = "剩余" + new BigDecimal(event.getRouteRemainDis() / 1000f).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue() + "公里";
                tv_msg.setText(msg);
            }
        }
        if (iv_road != null) {
            if (SharedPreUtil.getSharedPreInteger(CommonData.SDATA_APP_THEME, R.style.AppThemeWhile) == R.style.AppThemeWhile) {
                if (event.getRoadType() == 0 || event.getRoadType() == 6) {
                    iv_road.setImageResource(R.mipmap.n_road1);
                } else if (event.getRoadType() == 4 || event.getRoadType() == 5 || event.getRoadType() == 9 || event.getRoadType() == 10) {
                    iv_road.setImageResource(R.mipmap.n_road3);
                } else {
                    iv_road.setImageResource(R.mipmap.n_road2);
                }
            } else {
                if (event.getRoadType() == 0 || event.getRoadType() == 6) {
                    iv_road.setImageResource(R.mipmap.n_road1_b);
                } else if (event.getRoadType() == 4 || event.getRoadType() == 5 || event.getRoadType() == 9 || event.getRoadType() == 10) {
                    iv_road.setImageResource(R.mipmap.n_road3_b);
                } else {
                    iv_road.setImageResource(R.mipmap.n_road2_b);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final PAmapLukuangInfo event) {
        Lukuang lukuang = event.getLukuang();
        if (lukuangView != null) {
            //if (lukuang.isTmc_segment_enabled()) {
            lukuangView.setVisibility(VISIBLE);
            List<LukuangView.LukuangModel> models = new ArrayList<>();
            for (Lukuang.TmcInfo tmcInfo : lukuang.getTmc_info()) {
                models.add(new LukuangView.LukuangModel()
                        .setDistance(tmcInfo.getTmc_segment_distance())
                        .setStatus(tmcInfo.getTmc_status())
                        .setNumber(tmcInfo.getTmc_segment_number()));
            }
            lukuangView.setLukuangs(models);
//            } else {
//                lukuangView.setVisibility(GONE);
//            }

        }
    }
}
