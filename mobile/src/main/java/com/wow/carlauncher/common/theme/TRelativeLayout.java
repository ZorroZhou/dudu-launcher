package com.wow.carlauncher.common.theme;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.zhy.android.percent.support.PercentRelativeLayout;

import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

public class TRelativeLayout extends PercentRelativeLayout implements SkinCompatSupportable {
    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public TRelativeLayout(Context context) {
        this(context, null);
    }

    public TRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
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
