package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.DateUtil;
import com.wow.carlauncher.common.util.LunarUtil;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.view.base.BaseEXView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;

import java.util.Calendar;
import java.util.Date;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.wow.carlauncher.common.CommonData.DAY_MILL;
import static com.wow.carlauncher.common.CommonData.MINUTE_MILL;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.BLACK;
import static com.wow.carlauncher.ex.manage.ThemeManage.Theme.WHITE;
import static com.wow.carlauncher.view.activity.launcher.view.LShadowView.SizeEnum.FIVE;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LTimeView extends BaseEXView {


    public LTimeView(@NonNull Context context) {
        super(context);
    }

    public LTimeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_time;
    }

    @Override
    public void changedTheme(ThemeManage manage) {
        Context context = getContext();

        rl_base.setBackgroundResource(manage.getCurrentThemeRes(context, R.drawable.n_l_item1_bg));
        tv_title.setTextColor(manage.getCurrentThemeColor(context, R.color.l_text1));
        tv_shijian.setBackgroundResource(manage.getCurrentThemeRes(context, R.drawable.n_cell_bg));
        tv_shijian.setTextColor(manage.getCurrentThemeColor(context, R.color.l_text2));
        tv_week.setTextColor(manage.getCurrentThemeColor(context, R.color.l_text2));
        tv_day.setTextColor(manage.getCurrentThemeColor(context, R.color.l_text2));
        tv_lunar.setTextColor(manage.getCurrentThemeColor(context, R.color.l_text2));


        //时间组件的处理
        fl_time_root.removeAllViews();
        if (fl_time.getParent() != null) {
            ((ViewGroup) fl_time.getParent()).removeView(fl_time);
        }

        if (currentTheme == WHITE || currentTheme == BLACK) {
            tv_title.setGravity(Gravity.CENTER);

            fl_time_root.addView(LShadowView.getShadowView(getContext(), fl_time, FIVE), MATCH_PARENT, MATCH_PARENT);
        } else {
            tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            int margin = ViewUtils.dip2px(getContext(), 5);
            params.setMargins(margin, margin, margin, margin);

            fl_time_root.addView(fl_time, params);
        }
    }

    @Override
    protected void initView() {
        onEvent(null);
    }

    @ViewInject(R.id.fl_time)
    private FrameLayout fl_time;

    @ViewInject(R.id.fl_time_root)
    private FrameLayout fl_time_root;

    @ViewInject(R.id.rl_base)
    private View rl_base;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;


    @ViewInject(R.id.tv_shijian)
    private TextView tv_shijian;

    @ViewInject(R.id.tv_week)
    private TextView tv_week;

    @ViewInject(R.id.tv_day)
    private TextView tv_day;

    @ViewInject(R.id.tv_lunar)
    private TextView tv_lunar;

    private long cur_min = 0L;
    private long cur_day = 0L;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MTimeSecondEvent event) {
        long time = System.currentTimeMillis();
        long time1 = time / MINUTE_MILL;
        if (time1 != cur_min) {
            cur_min = time1;
            Date d = new Date();
            if (tv_shijian != null) {
                tv_shijian.setText(DateUtil.dateToString(d, "HH:mm"));
            }
            time1 = time / DAY_MILL;
            if (time1 != cur_day) {
                cur_day = time1;
                if (tv_day != null) {
                    tv_day.setText(DateUtil.dateToString(d, "yyyy年MM月dd日"));
                }
                if (tv_week != null) {
                    tv_week.setText(DateUtil.getWeekOfDate(d));
                }
                if (tv_lunar != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(d);
                    tv_lunar.setText(new LunarUtil(calendar).toString());
                }
            }
        }
    }
}
