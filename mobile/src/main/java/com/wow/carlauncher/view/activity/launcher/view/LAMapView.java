package com.wow.carlauncher.view.activity.launcher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.speed.SMEventSendSpeed;
import com.wow.carlauncher.ex.plugin.amapcar.AMapCarPlugin;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventNavInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventState;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapLukuangInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapMuteStateInfo;
import com.wow.carlauncher.ex.plugin.amapcar.model.Lukuang;
import com.wow.carlauncher.view.activity.launcher.event.LAMapCloseXunhang;
import com.wow.carlauncher.view.activity.launcher.BaseThemeView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.BLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.WHITE;
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

    @Override
    public void changedTheme(ThemeManage manage) {
        rl_base.setBackgroundResource(manage.getCurrentThemeRes(R.drawable.n_l_item1_bg));
        ll_xiansu.setBackgroundResource(manage.getCurrentThemeRes(R.mipmap.n_dh_quan));
        iv_moren.setImageResource(manage.getCurrentThemeRes(R.mipmap.n_dh_moren1));

        tv_title.setTextColor(manage.getCurrentThemeColor(R.color.l_title));

        manage.setTextViewsColor(this, new int[]{
                R.id.tv_xiansu,
                R.id.tv_text1,
                R.id.tv_msg
        }, R.color.l_text1);

        //横线
        manage.setViewsBackround(this, new int[]{
                R.id.line1,
                R.id.line7,
                R.id.line11,
                R.id.line4
        }, R.drawable.n_line2);

        //竖线
        manage.setViewsBackround(this, new int[]{
                R.id.line2,
                R.id.line3,
                R.id.line5,
                R.id.line6
        }, R.drawable.n_line3);

        iv_dh.setImageResource(manage.getCurrentThemeRes(R.mipmap.n_dh_dh));
        iv_dh_j.setImageResource(manage.getCurrentThemeRes(R.mipmap.n_dh_j));
        iv_dh_gs.setImageResource(manage.getCurrentThemeRes(R.mipmap.n_dh_gs));
        rl_navinfo.setBackgroundResource(manage.getCurrentThemeRes(R.drawable.n_dh2_bg));
        rl_xunhang.setBackgroundResource(manage.getCurrentThemeRes(R.drawable.n_dh2_bg));

        refreshMute();
        refreshRoad();

        iv_close.setImageResource(manage.getCurrentThemeRes(R.mipmap.n_dh_close));
        iv_car.setImageResource(manage.getCurrentThemeRes(R.mipmap.n_car));
        iv_car2.setImageResource(manage.getCurrentThemeRes(R.mipmap.n_car));
        iv_road2.setImageResource(manage.getCurrentThemeRes(R.mipmap.n_road1));

        if (currentTheme == WHITE || currentTheme == BLACK) {
            tv_title.setGravity(Gravity.CENTER);

            line7.setVisibility(GONE);
            line11.setVisibility(GONE);
        } else {
            tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            line7.setVisibility(VISIBLE);
            if (loactionOk) {
                line11.setVisibility(VISIBLE);
            }

        }

        LogEx.d(this, "changedTheme: ");
    }

    @Override
    protected void initView() {
        LogEx.d(this, "initView: ");
    }

    private boolean mute = false;

    private int roadType = 10;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rl_base)
    View rl_base;

    @BindView(R.id.line7)
    View line7;

    @BindView(R.id.line11)
    View line11;

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

    @BindView(R.id.iv_close)
    ImageView iv_close;

    @BindView(R.id.iv_dh_j)
    ImageView iv_dh_j;

    @BindView(R.id.iv_moren)
    ImageView iv_moren;

    @BindView(R.id.iv_car)
    ImageView iv_car;

    @BindView(R.id.iv_car2)
    ImageView iv_car2;

    @BindView(R.id.iv_icon)
    ImageView amapIcon;

    @BindView(R.id.iv_road)
    ImageView iv_road;

    @BindView(R.id.iv_road2)
    ImageView iv_road2;

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

    private boolean loactionOk = false;


    private void refreshMute() {
        if (mute) {
            if (ThemeManage.self().getTheme() == WHITE) {
                iv_mute.setImageResource(R.mipmap.n_dh_jy);
            } else {
                iv_mute.setImageResource(R.mipmap.n_dh_jy_b);
            }
        } else {
            if (ThemeManage.self().getTheme() == WHITE) {
                iv_mute.setImageResource(R.mipmap.n_dh_bjy);
            } else {
                iv_mute.setImageResource(R.mipmap.n_dh_bjy_b);
            }
        }
    }

    private void refreshRoad() {
        if (ThemeManage.self().getTheme() == WHITE) {
            if (roadType == 0 || roadType == 6) {
                iv_road.setImageResource(R.mipmap.n_road1);
            } else if (roadType == 4 || roadType == 5 || roadType == 9 || roadType == 10) {
                iv_road.setImageResource(R.mipmap.n_road3);
            } else {
                iv_road.setImageResource(R.mipmap.n_road2);
            }
        } else if (ThemeManage.self().getTheme() == BLACK) {
            if (roadType == 0 || roadType == 6) {
                iv_road.setImageResource(R.mipmap.n_road1_b);
            } else if (roadType == 4 || roadType == 5 || roadType == 9 || roadType == 10) {
                iv_road.setImageResource(R.mipmap.n_road3_b);
            } else {
                iv_road.setImageResource(R.mipmap.n_road2_b);
            }
        } else {
            if (roadType == 0 || roadType == 6) {
                iv_road.setImageResource(R.mipmap.n_road1_cb);
            } else if (roadType == 4 || roadType == 5 || roadType == 9 || roadType == 10) {
                iv_road.setImageResource(R.mipmap.n_road3_cb);
            } else {
                iv_road.setImageResource(R.mipmap.n_road2_cb);
            }
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
                    int mmm = event.getSegRemainDis() - 10;
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
                if (currentTheme == WHITE || currentTheme == BLACK) {
                    line11.setVisibility(GONE);
                } else {
                    line11.setVisibility(VISIBLE);
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
            line11.setVisibility(GONE);
            fl_xunhang_root.setVisibility(GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final LAMapCloseXunhang event) {
        iv_moren.setVisibility(VISIBLE);
        rl_che.setVisibility(GONE);
        line11.setVisibility(GONE);
        fl_xunhang_root.setVisibility(GONE);
    }
}
