package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.ViewUtils;
import com.wow.carlauncher.ex.manage.ThemeManage;
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

        Log.e(TAG + getClass().getSimpleName(), "changedTheme: ");
    }

    private View[] item;

    public void setItem(View[] item) {
        if (item == null) {
            return;
        }
        this.item = item;
        ll_base.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        for (View view : item) {
            if (view == null) {
                ll_base.addView(new View(getContext()), params);
            } else {
                int margin = ViewUtils.dip2px(getContext(), 10);
                params.setMargins(margin, margin, margin, margin);
                ll_base.addView(view, params);
            }
        }
        Log.e(TAG + getClass().getSimpleName(), "setItem: ");
    }

    @ViewInject(R.id.ll_base)
    private LinearLayout ll_base;


    @Event(value = {R.id.ll_base})
    private void clickEvent(View view) {
        //空的监听器,防止出现页面点击错乱的情况
    }
}
