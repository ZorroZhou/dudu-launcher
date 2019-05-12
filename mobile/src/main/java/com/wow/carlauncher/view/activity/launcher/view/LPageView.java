package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.view.activity.launcher.LayoutEnum;
import com.wow.carlauncher.view.base.BaseThemeView;

import butterknife.BindView;
import butterknife.OnClick;

public class LPageView extends BaseThemeView {

    public LPageView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_page;
    }

    private View[] item;
    private LayoutEnum layoutEnum = LayoutEnum.LAYOUT1;

    public void setLayoutEnum(LayoutEnum layoutEnum) {
        if (layoutEnum == null) {
            return;
        }
        if (!layoutEnum.equals(this.layoutEnum)) {
            this.layoutEnum = layoutEnum;
            addRefreshItemHandle();
        }
    }

    public void setItem(View[] item) {
        if (item == null) {
            return;
        }
        this.item = item;
        ll_base.removeAllViews();
        int itemIndex = 0;
        int leftMargin = ViewUtils.dip2px(getContext(), 15);
        for (View i : item) {
            if (i == null) {
                i = new View(getContext());
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            if (itemIndex == 0) {
                params.leftMargin = 0;
            } else {
                params.leftMargin = leftMargin;
            }
            i.setLayoutParams(params);
            ll_base.addView(i, params);
            itemIndex++;
        }
        addRefreshItemHandle();
    }

    private ViewTreeObserver.OnPreDrawListener oldOnPreDrawListener;
    private int oldHeight = 0;//用来比对布局发生改变的

    private void addRefreshItemHandle() {
        ll_base.getViewTreeObserver().removeOnPreDrawListener(oldOnPreDrawListener);
        oldOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int hh = ll_base.getHeight();
                if (oldHeight != hh && hh > 0) {
                    oldHeight = hh;
                    ll_base.getViewTreeObserver().removeOnPreDrawListener(this);

                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    int margin4 = ViewUtils.dip2px(getContext(), 4);
                    params.setMargins(margin4, 0, margin4, margin4);
                    if (layoutEnum.equals(LayoutEnum.LAYOUT1)) {
                        int margin10 = ViewUtils.dip2px(getContext(), 10);
                        int margin15 = ViewUtils.dip2px(getContext(), 15);
                        int margin8 = ViewUtils.dip2px(getContext(), 8);
                        params.setMargins(margin10, margin15, margin10, margin8);
                    }
                    ll_base.setLayoutParams(params);
                }
                return true;
            }
        };
        ll_base.getViewTreeObserver().addOnPreDrawListener(oldOnPreDrawListener);
    }

    @BindView(R.id.ll_base)
    LinearLayout ll_base;

    @OnClick(value = {R.id.ll_base})
    public void clickEvent(View view) {
        //空的监听器,防止出现页面点击错乱的情况
    }
}
