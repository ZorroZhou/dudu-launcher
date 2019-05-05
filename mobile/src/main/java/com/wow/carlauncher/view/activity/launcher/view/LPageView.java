package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.view.activity.launcher.LayoutEnum;
import com.wow.carlauncher.view.base.BaseEXView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static com.wow.carlauncher.common.CommonData.TAG;

public class LPageView extends BaseEXView {

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
            if (item != null && item.length > 0) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 1;
                int margin4 = ViewUtils.dip2px(getContext(), 4);
                int margin10 = ViewUtils.dip2px(getContext(), 10);
                params.setMargins(margin4, 0, margin4, margin10);
                if (layoutEnum.equals(LayoutEnum.LAYOUT1)) {
                    params.setMargins(margin10, margin10, margin10, margin10);
                }
                for (View i : item) {
                    i.setLayoutParams(params);
                }
            }
        }
    }

    public void setItem(View[] item) {
        if (item == null) {
            return;
        }
        this.item = item;
        ll_base.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;

        if (layoutEnum == null) {
            layoutEnum = LayoutEnum.LAYOUT1;
        }

        for (View view : item) {
            if (view == null) {
                ll_base.addView(new View(getContext()), params);
            } else {
                int margin4 = ViewUtils.dip2px(getContext(), 4);
                int margin10 = ViewUtils.dip2px(getContext(), 10);
                params.setMargins(margin4, 0, margin4, margin10);
                if (layoutEnum.equals(LayoutEnum.LAYOUT1)) {
                    params.setMargins(margin10, margin10, margin10, margin10);
                }
                ll_base.addView(view, params);
            }
            Log.e(TAG + getClass().getSimpleName(), "setItem: ");
        }
    }

    @ViewInject(R.id.ll_base)
    private LinearLayout ll_base;


    @Event(value = {R.id.ll_base})
    private void clickEvent(View view) {
        //空的监听器,防止出现页面点击错乱的情况
    }
}
