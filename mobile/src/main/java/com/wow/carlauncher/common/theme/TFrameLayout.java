package com.wow.carlauncher.common.theme;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.zhy.android.percent.support.PercentFrameLayout;

import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

public class TFrameLayout extends PercentFrameLayout implements SkinCompatSupportable {
    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public TFrameLayout(Context context) {
        this(context, null);
    }

    public TFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyle);
    }


    @Override
    public void setBackgroundResource(@DrawableRes int resId) {
        super.setBackgroundResource(resId);
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundResource(resId);
        }
    }

    @Override
    public void applySkin() {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
    }
}
