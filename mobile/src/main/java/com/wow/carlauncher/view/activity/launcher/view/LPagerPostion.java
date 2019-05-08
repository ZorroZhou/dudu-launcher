package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.ViewPagerOnPageChangeListener;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.view.base.BaseEXView;

import butterknife.BindView;

public class LPagerPostion extends BaseEXView implements ViewPagerOnPageChangeListener {
    public LPagerPostion(@NonNull Context context) {
        super(context);
    }

    public LPagerPostion(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_postion;
    }

    @BindView(R.id.postion)
    LinearLayout postion;

    @Override
    public void changedTheme(ThemeManage manage) {
        for (View post : postionViews) {
            post.setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_l_postion_n));
        }
        if (select < postionViews.length) {
            postionViews[select].setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_l_postion));
        }
        LogEx.d(this, "changedTheme: ");
    }

    private int select = 0;

    public void loadPostion(int num) {
        postionViews = new View[num];

        postion.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewUtils.dip2px(getContext(), 10), ViewUtils.dip2px(getContext(), 10));
        //设置小圆点左右之间的间隔
        params.setMargins(10, 0, 10, 0);
        for (int i = 0; i < postionViews.length; i++) {
            postionViews[i] = new View(getContext());
            if (i == 0) {
                postionViews[i].setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_l_postion));
            } else {
                postionViews[i].setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_l_postion_n));
            }
            postion.addView(postionViews[i], params);
        }

        LogEx.d(this, "loadPostion: ");
    }

    private View[] postionViews;

    @Override
    public void onPageSelected(int i) {
        if (postionViews != null) {
            select = i;
            for (View post : postionViews) {
                post.setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_l_postion_n));
            }
            if (i < postionViews.length) {
                postionViews[i].setBackgroundResource(ThemeManage.self().getCurrentThemeRes(R.drawable.n_l_postion));
            }
        }
    }
}
