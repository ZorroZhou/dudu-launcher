package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.view.base.BaseEXView;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LObdView extends BaseEXView {

    public LObdView(@NonNull Context context) {
        super(context);
    }

    public LObdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_obd;
    }

    @Override
    public void onThemeChanged(ThemeManage manage) {
        Context context = getContext();
        fl_base.setBackgroundResource(manage.getCurrentThemeRes(context, R.drawable.n_l_item2_bg));
        tv_text1.setTextColor(manage.getCurrentThemeColor(context, R.color.l_text1));

        manage.setViewsBackround(this, new int[]{R.id.ll_cell1, R.id.ll_cell2, R.id.ll_cell3, R.id.ll_cell4}, R.drawable.n_cell_bg);


        manage.setTextViewsColor(this, new int[]{
                R.id.tv_text21,
                R.id.tv_text22,
                R.id.tv_text23,
                R.id.tv_text24,
                R.id.tv_sd,
                R.id.tv_zs,
                R.id.tv_sw,
                R.id.tv_yl
        }, R.color.l_text2);

    }

    @ViewInject(R.id.fl_base)
    private View fl_base;


    @ViewInject(R.id.tv_text1)
    private TextView tv_text1;
}
