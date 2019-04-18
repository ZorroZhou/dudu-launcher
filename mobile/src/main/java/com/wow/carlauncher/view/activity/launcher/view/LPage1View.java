package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseView;

import org.xutils.view.annotation.ViewInject;

public class LPage1View extends BaseView {

    public LPage1View(@NonNull Context context) {
        super(context);
    }

    public LPage1View(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_page1;
    }

    @ViewInject(R.id.lamap)
    private LAMapView lamap;

    @ViewInject(R.id.lmusic)
    private LMusicView lmusic;

    @ViewInject(R.id.lwearther)
    private LWeatherView lwearther;
}
