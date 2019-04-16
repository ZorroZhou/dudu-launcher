package com.wow.carlauncher.view.activity.driving.coolBlack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventNavInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventState;
import com.wow.carlauncher.view.base.BaseEBusView;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.DateUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.util.Date;

import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.ICONS;

/**
 * Created by 10124 on 2018/5/11.
 */

public class CoolBlackView extends BaseEBusView {
    public CoolBlackView(@NonNull Context context) {
        super(context);
        addContent(R.layout.content_driving_cool_black);
    }

    public CoolBlackView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addContent(R.layout.content_driving_cool_black);
    }

    @ViewInject(R.id.tv_time)
    private TextView tv_time;

    @ViewInject(R.id.tv_date)
    private TextView tv_date;

    @ViewInject(R.id.tv_trip_time)
    private TextView tv_trip_time;

    @ViewInject(R.id.tv_driver_distance)
    private TextView tv_driver_distance;

    @ViewInject(R.id.iv_naving)
    private ImageView iv_naving;

    @ViewInject(R.id.ll_navinfo)
    private LinearLayout ll_navinfo;

    @ViewInject(R.id.ll_info_shunshiyouhao)
    private LinearLayout ll_info_shunshiyouhao;

    @ViewInject(R.id.ll_info_dis)
    private LinearLayout ll_info_dis;

    @ViewInject(R.id.tv_amaproad)
    private TextView tv_amaproad;

    @ViewInject(R.id.tv_amapmsg)
    private TextView tv_amapmsg;

    private long startTime = System.currentTimeMillis();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final MTimeSecondEvent event) {
        this.tv_date.setText(DateUtil.dateToString(new Date(), "yyyy-MM-dd"));
        this.tv_time.setText(DateUtil.dateToString(new Date(), "HH:mm:ss"));
        this.tv_trip_time.setText(DateUtil.formatDuring(System.currentTimeMillis() - startTime));
        //this.tv_driver_distance.setText(String.format(Locale.CHINA, "%.1f", (double) TripManage.self().getTropMileage() / 1000d));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final PAmapEventState event) {
        if (event.isRunning()) {
            ll_info_shunshiyouhao.setVisibility(View.GONE);
            ll_info_dis.setVisibility(View.GONE);

            iv_naving.setVisibility(View.VISIBLE);
            ll_navinfo.setVisibility(View.VISIBLE);

        } else {
            ll_info_shunshiyouhao.setVisibility(View.VISIBLE);
            ll_info_dis.setVisibility(View.VISIBLE);

            iv_naving.setVisibility(View.GONE);
            ll_navinfo.setVisibility(View.GONE);

            iv_naving.setImageResource(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final PAmapEventNavInfo event) {
        if (iv_naving != null && event.getIcon() - 1 >= 0 && event.getIcon() - 1 < ICONS.length) {
            iv_naving.setImageResource(ICONS[event.getIcon() - 1]);
        }
        if (tv_amaproad != null && CommonUtil.isNotNull(event.getNextRoadName())) {
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
            tv_amaproad.setText(msg);
        }
        if (tv_amapmsg != null && event.getRouteRemainTime() > -1 && event.getRouteRemainDis() > -1) {
            if (event.getRouteRemainTime() == 0 || event.getRouteRemainDis() == 0) {
                tv_amapmsg.setText("到达");
            } else {
                String msg = "剩余" + new BigDecimal(event.getRouteRemainDis() / 1000f).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue() + "公里  " +
                        event.getRouteRemainTime() / 60 + "分钟";
                tv_amapmsg.setText(msg);
            }
        }
    }
}

