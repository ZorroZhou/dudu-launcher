package com.wow.carlauncher.view.activity.launcher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.LukuangView;
import com.wow.carlauncher.ex.manage.skin.SkinConstant;
import com.wow.carlauncher.ex.manage.skin.SkinManage;
import com.wow.carlauncher.ex.manage.skin.SkinUtil;
import com.wow.carlauncher.ex.manage.speed.SMEventSendSpeed;
import com.wow.carlauncher.ex.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventNavInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventState;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapLukuangInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapMuteStateInfo;
import com.wow.carlauncher.ex.plugin.amapcar.model.Lukuang;
import com.wow.carlauncher.view.activity.launcher.BaseThemeView;
import com.wow.carlauncher.view.activity.launcher.event.LAMapCloseXunhang;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.AMAP_PACKAGE;
import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.ICONS;

/**
 * Created by 10124 on 2018/4/20.
 */
@SuppressLint("RtlHardcoded")
public class LAMapView extends BaseThemeView {

    public LAMapView(@NonNull Context context) {
        super(context);
    }

    public LAMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_amap;
    }

    private ViewTreeObserver.OnPreDrawListener oldOnPreDrawListener;

    @Override
    public void changedSkin(SkinManage manage) {
        tv_title.setGravity(SkinUtil.analysisItemTitleAlign(manage.getString(R.string.theme_item_title_align)));
        line_daohang.setVisibility(SkinUtil.analysisVisibility(manage.getString(R.string.theme_item_amap_daohang_line)));
        if (loactionOk) {
            line_xunhang.setVisibility(SkinUtil.analysisVisibility(manage.getString(R.string.theme_item_amap_xunhang_line)));
        } else {
            line_xunhang.setVisibility(GONE);
        }
        if (CommonUtil.equals(SkinConstant.AMapBtnLayout.layout2, manage.getString(R.string.theme_amap_btn_layout))) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            btn_nav_j.setLayoutParams(lp);
            btn_nav_gs.setLayoutParams(lp);
            btn_gd_root.setLayoutParams(lp);
        } else {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            btn_gd_root.setLayoutParams(lp);

            if (ll_controller3.getHeight() > 0) {
                int height = ll_controller3.getHeight();
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams((int) (height * 1.2), ViewGroup.LayoutParams.MATCH_PARENT);
                btn_nav_gs.setLayoutParams(lp2);
                btn_nav_j.setLayoutParams(lp2);
            } else {
                ll_controller3.getViewTreeObserver().removeOnPreDrawListener(oldOnPreDrawListener);
                oldOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        if (ll_controller3.getHeight() > 0) {
                            ll_controller3.getViewTreeObserver().removeOnPreDrawListener(this);
                            int height = ll_controller3.getHeight();

                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (height * 1.2), ViewGroup.LayoutParams.MATCH_PARENT);
                            btn_nav_gs.setLayoutParams(lp);
                            btn_nav_j.setLayoutParams(lp);
                        }
                        return true;
                    }
                };
                ll_controller3.getViewTreeObserver().addOnPreDrawListener(oldOnPreDrawListener);
            }
        }
    }

    @Override
    protected void initView() {
        refreshRoad();
        refreshMute();
        LogEx.d(this, "initView: ");
    }

    private boolean mute = false;

    private int roadType = 10;


    @BindView(R.id.ll_controller3)
    View ll_controller3;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rl_base)
    View rl_base;

    @BindView(R.id.line_daohang)
    View line_daohang;

    @BindView(R.id.line_xunhang)
    View line_xunhang;

    @BindView(R.id.rl_navinfo)
    View rl_navinfo;

    @BindView(R.id.rl_xunhang)
    View rl_xunhang;

    @BindView(R.id.rl_che)
    View rl_che;

    @BindView(R.id.fl_xunhang_root)
    View fl_xunhang_root;

    @BindView(R.id.iv_dh)
    ImageView iv_dh;

    @BindView(R.id.iv_dh_gs)
    ImageView iv_dh_gs;

    @BindView(R.id.iv_dh_j)
    ImageView iv_dh_j;

    @BindView(R.id.iv_moren)
    ImageView iv_moren;

    @BindView(R.id.iv_icon)
    ImageView amapIcon;

    @BindView(R.id.iv_road)
    ImageView iv_road;

    @BindView(R.id.tv_next_dis)
    TextView tv_next_dis;

    @BindView(R.id.tv_next_road)
    TextView tv_next_road;

    @BindView(R.id.tv_xiansu)
    TextView tv_xiansu;

    @BindView(R.id.ll_xiansu)
    LinearLayout ll_xiansu;

    @BindView(R.id.tv_msg)
    TextView tv_msg;

    @BindView(R.id.tv_speed)
    TextView tv_speed;

    @BindView(R.id.iv_mute)
    ImageView iv_mute;

    @BindView(R.id.lukuang)
    LukuangView lukuangView;

    @BindView(R.id.base_daohang)
    View base_daohang;

    @BindView(R.id.base_moren)
    View base_moren;

    @BindView(R.id.btn_nav_j)
    View btn_nav_j;

    @BindView(R.id.btn_nav_gs)
    View btn_nav_gs;

    @BindView(R.id.btn_gd_root)
    View btn_gd_root;


    private boolean loactionOk = false;


    private void refreshMute() {
        if (mute) {
            iv_mute.setImageResource(R.drawable.theme_amap_jy);
        } else {
            iv_mute.setImageResource(R.drawable.theme_amap_bjy);
        }
    }

    private void refreshRoad() {
        if (roadType == 0 || roadType == 6) {
            iv_road.setImageResource(R.drawable.theme_amap_road1);
        } else if (roadType == 4 || roadType == 5 || roadType == 9 || roadType == 10) {
            iv_road.setImageResource(R.drawable.theme_amap_road3);
        } else {
            iv_road.setImageResource(R.drawable.theme_amap_road2);
        }
    }

    @OnClick(value = {R.id.rl_base, R.id.btn_close, R.id.btn_mute, R.id.btn_nav_gs, R.id.btn_nav_j, R.id.btn_gd})
    public void clickEvent(View view) {
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
                AMapCarPlugin.self().naviToHome();
                break;
            }
            case R.id.btn_nav_gs: {
                AMapCarPlugin.self().naviToComp();
                break;
            }
            case R.id.btn_close: {
                AMapCarPlugin.self().exitNav();
                break;
            }
            case R.id.btn_mute: {
                AMapCarPlugin.self().mute(!mute);
                break;
            }
            case R.id.btn_gd: {
                AMapCarPlugin.self().testNavi();
                break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PAmapEventState event) {
        if (base_moren.getVisibility() == VISIBLE && event.isRunning()) {
            base_moren.setVisibility(GONE);
            base_daohang.setVisibility(VISIBLE);
        } else if (base_moren.getVisibility() == GONE && !event.isRunning()) {
            base_moren.setVisibility(VISIBLE);
            base_daohang.setVisibility(GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PAmapMuteStateInfo event) {
        if (iv_mute != null) {
            mute = event.isMute();
            refreshMute();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PAmapEventNavInfo event) {
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
            if (event.getSegRemainDis() <= 10) {
                msg = msg + "现在";
            } else {
                if (event.getSegRemainDis() > 1000) {
                    msg = msg + new BigDecimal(((double) event.getSegRemainDis()) / 1000).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue() + "kM后";
                } else {
                    int mmm = event.getSegRemainDis();
                    msg = msg + mmm + "M后";
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
                String msg = new BigDecimal(event.getRouteRemainDis() / 1000f).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue() + "KM";
                tv_msg.setText(msg);
            }
        }
        if (iv_road != null) {
            roadType = event.getRoadType();
            refreshRoad();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PAmapLukuangInfo event) {
        Lukuang lukuang = event.getLukuang();
        if (lukuangView != null) {
            if (lukuang.isTmc_segment_enabled()) {
                lukuangView.setVisibility(VISIBLE);
                List<LukuangView.LukuangModel> models = new ArrayList<>();
                for (Lukuang.TmcInfo tmcInfo : lukuang.getTmc_info()) {
                    models.add(new LukuangView.LukuangModel()
                            .setDistance(tmcInfo.getTmc_segment_distance())
                            .setStatus(tmcInfo.getTmc_status())
                            .setNumber(tmcInfo.getTmc_segment_number()));
                }
                lukuangView.setLukuangs(models);
            } else {
                lukuangView.setVisibility(GONE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final SMEventSendSpeed event) {
        if (event.isUse() && SharedPreUtil.getBoolean(CommonData.SDATA_USE_NAVI_XUNHYANG, false)) {
            if (!loactionOk) {
                loactionOk = true;
                iv_moren.setVisibility(GONE);
                rl_che.setVisibility(VISIBLE);
                if (loactionOk) {
                    line_xunhang.setVisibility(SkinUtil.analysisVisibility(SkinManage.self().getString(R.string.theme_item_amap_xunhang_line)));
                } else {
                    line_xunhang.setVisibility(GONE);
                }
                fl_xunhang_root.setVisibility(VISIBLE);
            }
            //方向取值范围：【0，360】，其中0度表示正北方向，90度表示正东，180度表示正南，270度表示正西
            if (tv_speed != null) {
                tv_speed.setText(String.valueOf(event.getSpeed()));
            }
        } else if (loactionOk) {
            loactionOk = false;
            iv_moren.setVisibility(VISIBLE);
            rl_che.setVisibility(GONE);
            line_xunhang.setVisibility(GONE);
            fl_xunhang_root.setVisibility(GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final LAMapCloseXunhang event) {
        iv_moren.setVisibility(VISIBLE);
        rl_che.setVisibility(GONE);
        line_xunhang.setVisibility(GONE);
        fl_xunhang_root.setVisibility(GONE);
    }
}
