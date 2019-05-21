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
import com.wow.carlauncher.view.activity.launcher.BaseThemeView;

import butterknife.BindView;

public class LPagerPostion extends BaseThemeView implements ViewPagerOnPageChangeListener {
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

    private int select = 0;

    public void loadPostion(int num) {
        postionViews = new View[num];

        postion.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewUtils.dip2px(getContext(), 10), ViewUtils.dip2px(getContext(), 10));
        //设置小圆点左右之间的间隔
        params.setMargins(10, 0, 10, 0);
        for (int i = 0; i < postionViews.length; i++) {
            postionViews[i] = View.inflate(getContext(), R.layout.content_l_postion_item, null);
            if (i == 0) {
                postionViews[i].setBackgroundResource(R.drawable.theme_pager_postion_select);
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
                post.setBackgroundResource(R.drawable.theme_pager_postion);
            }
            if (select < postionViews.length) {
                postionViews[i].setBackgroundResource(R.drawable.theme_pager_postion_select);
            }
        }
    }
}
