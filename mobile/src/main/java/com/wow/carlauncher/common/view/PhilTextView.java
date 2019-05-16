package com.wow.carlauncher.common.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;

import java.io.File;

import skin.support.widget.SkinCompatTextView;

/**
 * @author Mr.Qiu
 * @date 2018/3/12
 */
public class PhilTextView extends SkinCompatTextView {

    public PhilTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        String file = "tonts" + File.separator + "digital-7.ttf";

        AssetManager assets = context.getAssets();
        Typeface font = Typeface.createFromAsset(assets, file);
        setTypeface(font);
    }
}
