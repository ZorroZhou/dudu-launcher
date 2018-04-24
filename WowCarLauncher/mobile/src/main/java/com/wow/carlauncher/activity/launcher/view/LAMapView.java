package com.wow.carlauncher.activity.launcher.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wow.carlauncher.R;
import com.wow.carlauncher.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.plugin.amapcar.AMapCarPluginListener;
import com.wow.carlauncher.plugin.amapcar.NaviInfo;
import com.wow.carlauncher.plugin.amapcar.event.PAmapEventNavInfo;
import com.wow.carlauncher.plugin.amapcar.event.PAmapEventState;
import com.wow.carlauncher.plugin.music.event.PMusicEventCover;
import com.wow.frame.util.AppUtil;
import com.wow.frame.util.CommonUtil;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;

import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.plugin.amapcar.AMapCarConstant.AMAP_PACKAGE;
import static com.wow.carlauncher.plugin.amapcar.AMapCarConstant.ICONS;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LAMapView extends LBaseView {

    public LAMapView(@NonNull Context context) {
        super(context);
        initView();
    }

    public LAMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private View amapController;
    private ImageView amapIcon;
    private LinearLayout amapnavi;
    private TextView amaproad;
    private TextView amapmsg;

    private void initView() {
        RelativeLayout amapView = (RelativeLayout) View.inflate(getContext(), R.layout.plugin_amap_launcher, null);
        this.addView(amapView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        View.OnClickListener amapclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    case R.id.btn_go_company: {
                        if (!AppUtil.isInstall(getContext(), AMAP_PACKAGE)) {
                            Toast.makeText(getContext(), "没有安装高德地图", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        AMapCarPlugin.self().getComp();
                        break;
                    }
                }
            }
        };


        amapView.findViewById(R.id.rl_base).setOnClickListener(amapclick);
        amapView.findViewById(R.id.btn_go_home).setOnClickListener(amapclick);
        amapView.findViewById(R.id.btn_go_company).setOnClickListener(amapclick);

        amapIcon = (ImageView) amapView.findViewById(R.id.iv_icon);
        amapController = amapView.findViewById(R.id.ll_controller);
        amapnavi = (LinearLayout) amapView.findViewById(R.id.ll_navi);
        amaproad = (TextView) amapView.findViewById(R.id.tv_road);
        amapmsg = (TextView) amapView.findViewById(R.id.tv_msg);
    }

    @Subscribe
    public void onEventMainThread(final PAmapEventState event) {
        if (amapController != null) {
            if (event.getState() == 8 || event.getState() == 10) {
                amapController.setVisibility(View.GONE);
                amapnavi.setVisibility(View.VISIBLE);
            } else if (event.getState() == 9 || event.getState() == 11) {
                amapController.setVisibility(View.VISIBLE);
                amapnavi.setVisibility(View.GONE);
                amapIcon.setImageResource(R.mipmap.ic_amap);
            }
        }
    }

    @Subscribe
    public void onEventMainThread(final PAmapEventNavInfo event) {
        if (amapIcon != null && event.getIcon() - 1 >= 0 && event.getIcon() - 1 < ICONS.length) {
            amapIcon.setImageResource(ICONS[event.getIcon() - 1]);
        }
        if (amaproad != null && CommonUtil.isNotNull(event.getWroad())) {
            String msg = "";
            if (event.getDis() < 10) {
                msg = msg + "现在";
            } else {
                if (event.getDis() > 1000) {
                    msg = msg + event.getDis() / 1000 + "公里后";
                } else {
                    msg = msg + event.getDis() + "米后";
                }
            }
            msg = msg + event.getWroad();
            amaproad.setText(msg);
        }
        if (amapmsg != null && event.getRemainTime() > -1 && event.getRemainDis() > -1) {
            if (event.getRemainTime() == 0 || event.getRemainDis() == 0) {
                amapmsg.setText("到达");
            } else {
                String msg = "剩余" + new BigDecimal(event.getRemainDis() / 1000f).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue() + "公里  " +
                        event.getRemainTime() / 60 + "分钟";
                amapmsg.setText(msg);
            }
        }
    }
}
