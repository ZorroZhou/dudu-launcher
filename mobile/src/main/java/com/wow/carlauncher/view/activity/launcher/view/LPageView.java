package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.ex.manage.ThemeManage;
import com.wow.carlauncher.view.base.BaseEXView;

import org.xutils.view.annotation.ViewInject;

import static com.wow.carlauncher.view.activity.launcher.view.LShadowView.SizeEnum.TEN;

public class LPageView extends BaseEXView {

    public LPageView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_page;
    }

    @Override
    public void changedTheme(ThemeManage manage) {
        if (item != null) {
            for (View view : item) {
                if (view != null) {
                    if (view.getParent() instanceof ViewGroup) {
                        ((ViewGroup) view.getParent()).removeView(view);
                    }
                }
            }
            setItem(item);
        }
    }

    private View[] item;

    public void setItem(View[] item) {
        if (item == null) {
            return;
        }
        System.out.println(item.length + " !!!!!!!");
        this.item = item;
        ll_base.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        for (View view : item) {
            if (view == null) {
                ll_base.addView(new View(getContext()), params);
            } else {
                if (ThemeManage.self().getTheme() == ThemeManage.Theme.WHITE || ThemeManage.self().getTheme() == ThemeManage.Theme.BLACK) {
                    ll_base.addView(LShadowView.getShadowView(getContext(), view, TEN), params);
                } else {
                    int margin = ViewUtils.dip2px(getContext(), 10);
                    params.setMargins(margin, margin, margin, margin);
                    ll_base.addView(view, params);
                }
            }

        }
    }

    @ViewInject(R.id.ll_base)
    private LinearLayout ll_base;
}
