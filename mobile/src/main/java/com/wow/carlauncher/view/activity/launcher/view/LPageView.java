package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.view.activity.launcher.ItemInterval;
import com.wow.carlauncher.view.activity.launcher.LayoutEnum;
import com.wow.carlauncher.view.base.BaseView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wow.carlauncher.common.CommonData.SDATA_LAUNCHER_ITEM_INTERVAL;

public class LPageView extends BaseView {

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
        int leftMargin = ViewUtils.dip2px(getContext(), ItemInterval.getSizeById(SharedPreUtil.getInteger(SDATA_LAUNCHER_ITEM_INTERVAL, ItemInterval.XIAO.getId())));
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
                    if (layoutEnum.equals(LayoutEnum.LAYOUT1)) {
                        int margin10 = ViewUtils.dip2px(getContext(), 10);
                        int top = ViewUtils.dip2px(getContext(), ItemInterval.getSizeById(SharedPreUtil.getInteger(SDATA_LAUNCHER_ITEM_INTERVAL, ItemInterval.XIAO.getId())));
                        int bottom = ViewUtils.dip2px(getContext(), 8);
                        ll_base.setPadding(margin10, top, margin10, bottom);
                    } else {
                        int zuoyou = ViewUtils.dip2px(getContext(), ItemInterval.getSizeById(SharedPreUtil.getInteger(SDATA_LAUNCHER_ITEM_INTERVAL, ItemInterval.XIAO.getId())) - 8);
                        int margin4 = ViewUtils.dip2px(getContext(), 4);
                        ll_base.setPadding(zuoyou, 0, zuoyou, margin4);
                    }
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
