package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.wow.carlauncher.R;

public class LShadowView extends FrameLayout {
    private FrameLayout shadowView;

    public LShadowView(@NonNull Context context) {
        super(context);
        shadowView = (FrameLayout) View.inflate(getContext(), R.layout.content_l_shadow, null);
        this.addView(shadowView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public LShadowView addViewEx(View view) {
        shadowView.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        return this;
    }
}
