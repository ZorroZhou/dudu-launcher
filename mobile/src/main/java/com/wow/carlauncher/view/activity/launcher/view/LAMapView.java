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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventNavInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventState;
import com.wow.carlauncher.view.base.BaseEBusView;
import com.wow.frame.util.AppUtil;
import com.wow.frame.util.CommonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;

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

    @ViewInject(R.id.ll_controller)
    private View amapController;

    @ViewInject(R.id.iv_icon)
    private ImageView amapIcon;

    @ViewInject(R.id.ll_navi)
    private LinearLayout amapnavi;

    @ViewInject(R.id.tv_next_dis)
    private TextView tv_next_dis;

    @ViewInject(R.id.tv_next_road)
    private TextView tv_next_road;

    @ViewInject(R.id.tv_xiansu)
    private TextView tv_xiansu;


    @ViewInject(R.id.tv_msg)
    private TextView tv_msg;

    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar;


    @Event(value = {R.id.rl_base, R.id.btn_go_home, R.id.btn_close})
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
            case R.id.btn_go_home: {
                if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
                    Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
                    break;
                }
                AMapCarPlugin.self().getHome();
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
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final PAmapEventState event) {
        if (amapController != null) {
            if (event.isRunning()) {
                amapController.setVisibility(View.GONE);
                amapnavi.setVisibility(View.VISIBLE);
            } else {
                amapController.setVisibility(View.VISIBLE);
                amapnavi.setVisibility(View.GONE);
                amapIcon.setImageResource(0);
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
            msg = msg + event.getNextRoadName();
            tv_next_dis.setText(msg);
        }
        if (tv_next_road != null && CommonUtil.isNotNull(event.getNextRoadName())) {
            String msg = fangxiang + "进入" + event.getNextRoadName();
            tv_next_road.setText(msg);
        }
        if (tv_xiansu != null) {
            if (event.getCameraSpeed() > 0) {
                String msg = event.getCameraSpeed() + "";
                tv_xiansu.setText(msg);
                tv_xiansu.setVisibility(VISIBLE);
            } else {
                tv_xiansu.setVisibility(GONE);
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

        if (progressBar != null) {
            progressBar.setProgress((int) (event.getRouteRemainDis() * 100f / event.getRouteAllDis()));
        }

    }
}
