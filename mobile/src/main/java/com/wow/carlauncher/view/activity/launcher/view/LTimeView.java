package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.util.DateUtil;
import com.wow.carlauncher.common.util.LunarUtil;
import com.wow.carlauncher.ex.manage.skin.SkinManage;
import com.wow.carlauncher.ex.manage.skin.SkinUtil;
import com.wow.carlauncher.ex.manage.time.event.TMEvent3Second;
import com.wow.carlauncher.view.activity.launcher.BaseThemeView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wow.carlauncher.common.CommonData.DAY_MILL;
import static com.wow.carlauncher.common.CommonData.MINUTE_MILL;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LTimeView extends BaseThemeView {


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
    public void changedSkin(SkinManage manage) {
        tv_title.setGravity(SkinUtil.analysisItemTitleAlign(manage.getString(R.string.theme_item_title_align)));
    }

    @OnClick(value = {R.id.rl_base})
    public void clickEvent(View view) {

    }

    @Override
    protected void initView() {
        onEvent(null);

        LogEx.d(this, "initView: ");
    }


    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_shijian)
    TextView tv_shijian;

    @BindView(R.id.tv_week)
    TextView tv_week;

    @BindView(R.id.tv_day)
    TextView tv_day;

    @BindView(R.id.tv_lunar)
    TextView tv_lunar;

    private long cur_min = 0L;
    private long cur_day = 0L;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TMEvent3Second event) {
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
