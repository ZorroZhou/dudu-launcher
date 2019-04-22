package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.meetsl.scardview.SCardView;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.util.ViewUtils;

public class LShadowView extends FrameLayout {
    private SCardView shadowView;

    public LShadowView(@NonNull Context context, int shadow) {
        super(context);
        shadowView = (SCardView) View.inflate(getContext(), R.layout.content_l_shadow, null);
        shadowView.setCardElevation(ViewUtils.dip2px(getContext(), shadow));
        shadowView.setRadius(ViewUtils.dip2px(getContext(), shadow));
        this.addView(shadowView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public LShadowView addViewEx(View view) {
        shadowView.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        return this;
    }

    public static View getShadowView(Context context, View view, int shadow) {
        return new LShadowView(context, shadow).addViewEx(view);
    }
}
