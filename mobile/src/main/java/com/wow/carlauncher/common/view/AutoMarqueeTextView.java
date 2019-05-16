package com.wow.carlauncher.common.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import skin.support.widget.SkinCompatTextView;

public class AutoMarqueeTextView extends SkinCompatTextView {
    public AutoMarqueeTextView(Context context) {
        super(context);
        setFocusable(true);//在每个构造方法中，将TextView设置为可获取焦点
    }

    public AutoMarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(true);
    }

    public AutoMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(true, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(true);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}
