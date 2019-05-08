package com.wow.carlauncher.view.activity.driving.coolBlack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.DateUtil;
import com.wow.carlauncher.ex.manage.time.event.MTime3SecondEvent;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventNavInfo;
import com.wow.carlauncher.ex.plugin.amapcar.event.PAmapEventState;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventAction;
import com.wow.carlauncher.ex.plugin.fk.event.PFkEventConnect;
import com.wow.carlauncher.view.activity.driving.DrivingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.Date;

import butterknife.BindView;

import static com.wow.carlauncher.ex.plugin.amapcar.AMapCarConstant.ICONS;
import static com.wow.carlauncher.ex.plugin.fk.FangkongProtocolEnum.YLFK;
import static com.wow.carlauncher.ex.plugin.fk.protocol.YiLianProtocol.RIGHT_BOTTOM_CLICK;

/**
 * Created by 10124 on 2018/5/11.
 */

public class CoolBlackView extends DrivingView {
    public CoolBlackView(@NonNull Context context) {
        super(context);
    }

    public CoolBlackView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_driving_cool_black;
    }

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.tv_trip_time)
    TextView tv_trip_time;

    @BindView(R.id.iv_navicon)
    ImageView iv_navicon;

    @BindView(R.id.ll_navinfo)
    LinearLayout ll_navinfo;

    @BindView(R.id.ll_tp)
    FrameLayout ll_tp;

    @BindView(R.id.ll_music)
    FrameLayout ll_music;

    @BindView(R.id.tv_amaproad)
    TextView tv_amaproad;

    @BindView(R.id.tv_amapmsg)
    TextView tv_amapmsg;

    private boolean isFront = true;

    public void setFront(boolean front) {
        isFront = front;
    }

    private boolean fktuoguan = false;
    private boolean showNav = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MTime3SecondEvent event) {
        this.tv_date.setText(DateUtil.dateToString(new Date(), "yyyy-MM-dd"));
        this.tv_time.setText(DateUtil.dateToString(new Date(), "HH:mm:ss"));
        this.tv_trip_time.setText(DateUtil.formatDuring(System.currentTimeMillis() - AppContext.self().getStartTime()));
    }

    @Subscribe(priority = 90)
    public void onEvent(PFkEventAction event) {
        if (!isFront) {
            return;
        }
        if (YLFK.equals(event.getFangkongProtocol())) {
            boolean needCancelEvent = false;
            switch (event.getAction()) {
                case RIGHT_BOTTOM_CLICK:
                    if (inNav) {
                        TaskExecutor.self().autoPost(() -> showNav(!showNav));
                    } else {
                        ToastManage.self().show("没有开启导航!");
                    }
                    needCancelEvent = true;
                    break;
            }
            if (needCancelEvent) {
                EventBus.getDefault().cancelEventDelivery(event);
            }
        }
    }

    @Subscribe
    public void onEvent(PFkEventConnect event) {
        if (YLFK.equals(event.getFangkongProtocol())) {
            fktuoguan = event.isConnected();
        }
    }

    private boolean inNav = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PAmapEventState event) {
        inNav = event.isRunning();
        if (!fktuoguan) {
            showNav(event.isRunning());
        }
    }

    private void showNav(boolean show) {
        showNav = show;
        if (show) {
            ll_tp.setVisibility(View.GONE);
            ll_music.setVisibility(View.GONE);

            iv_navicon.setVisibility(View.VISIBLE);
            ll_navinfo.setVisibility(View.VISIBLE);
        } else {
            ll_tp.setVisibility(View.VISIBLE);
            ll_music.setVisibility(View.VISIBLE);

            iv_navicon.setVisibility(View.GONE);
            ll_navinfo.setVisibility(View.GONE);
            iv_navicon.setImageResource(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PAmapEventNavInfo event) {
        String fangxiang = "";
        if (iv_navicon != null && event.getIcon() - 1 >= 0 && event.getIcon() - 1 < ICONS.length) {
            iv_navicon.setImageResource(ICONS[event.getIcon() - 1]);
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
            if ("目的地".equals(event.getNextRoadName())) {
                msg = msg + fangxiang + "到达" + event.getNextRoadName();
                tv_amaproad.setText(msg);
            } else {
                msg = msg + fangxiang + "进入" + event.getNextRoadName();
                tv_amaproad.setText(msg);
            }
            msg = msg + event.getNextRoadName();
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

