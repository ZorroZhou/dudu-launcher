package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.meetsl.scardview.SCardView;
import com.wow.carlauncher.R;

public class LShadowView extends FrameLayout {
    private SCardView shadowView;

    public LShadowView(@NonNull Context context, SizeEnum shadow) {
        super(context);
        if (shadow == SizeEnum.FIVE) {
            shadowView = (SCardView) View.inflate(getContext(), R.layout.content_l_shadow5, null);
        } else {
            shadowView = (SCardView) View.inflate(getContext(), R.layout.content_l_shadow10, null);
        }
        this.addView(shadowView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public LShadowView addViewEx(View view) {
        shadowView.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        return this;
    }

    public static View getShadowView(Context context, View view, SizeEnum shadow) {
        return new LShadowView(context, shadow).addViewEx(view);
    }

    public enum SizeEnum {
        FIVE,
        TEN
    }
}
