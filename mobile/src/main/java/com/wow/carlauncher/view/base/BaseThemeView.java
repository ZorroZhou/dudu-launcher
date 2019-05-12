package com.wow.carlauncher.view.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wow.carlauncher.ex.manage.ThemeManage;

/**
 * Created by 10124 on 2018/4/22.
 */

public abstract class BaseThemeView extends BaseView implements ThemeManage.OnThemeChangeListener {
    public BaseThemeView(@NonNull Context context) {
        super(context);
    }

    public BaseThemeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected ThemeManage.Theme currentTheme;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onThemeChanged(ThemeManage.self());
        ThemeManage.self().registerThemeChangeListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ThemeManage.self().unregisterThemeChangeListener(this);
    }

    @Override
    public void onThemeChanged(ThemeManage manage) {
        if (!manage.getTheme().equals(currentTheme)) {
            currentTheme = manage.getTheme();
            changedTheme(manage);
        }
    }

    public abstract void changedTheme(ThemeManage manage);
}
